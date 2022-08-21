package org.di.subcontacts

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.di.subcontacts.ContactsServiceProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.{BadRequest, InternalServerError, OK}
import akka.http.scaladsl.server.{PathMatcher, PathMatcher1}
import akka.pattern.ask

import scala.concurrent.duration.DurationInt
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Main extends App {

  implicit val system = ActorSystem("subcontacts-system")

  private val config = ConfigFactory.load()
  private val host = config.getString("http.host")
  private val port = config.getInt("http.port")

  private val matcher: PathMatcher[(Int, String)] =
    "user" / IntNumber / Segment

  implicit val timeout = Timeout(10.seconds)
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  private val subContactsService = system.actorOf(Props(new ContactsService()), "subcontacts-service")

  val routes =
    path(matcher) {
      case (userId, "contacts") =>
        post {
          entity(as[ContactIds]) { request =>
            subContactsService ! AddContactsRequest(userId, request.contacts)
            complete(s"New contacts for $userId: ${formatResponse(request.contacts.toSet)}")
          }
        }
      case (userId, "sd-contacts") =>
        get {
          val response: Future[SecondDegreeContactsResponse] =
            (subContactsService ? GetSecondDegreeRequest(userId)).mapTo[SecondDegreeContactsResponse]
          onComplete(response) {
            case Success(res) =>
              complete(HttpResponse(OK, entity = formatResponse(res.result)))
            case Failure(ex) =>
              complete(HttpResponse(InternalServerError, entity = s"An exception occurred: ${ex.getMessage}"))
          }
        }
      case (_, postfix) =>
        complete(HttpResponse(
          BadRequest,
          entity = s"Invalid url segment: $postfix. Should be /contacts or /sd-contacts")
        )
    }

    Http().bindAndHandle(routes, host, port).onComplete {
      case Success(address) => println(s"Successfully connected to $address")
      case Failure(t) =>
        println(s"Could not connect to $host:$port, ${t.getMessage}")
        system.terminate()
    }
  }
