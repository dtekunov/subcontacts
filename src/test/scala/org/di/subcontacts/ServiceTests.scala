package org.di.subcontacts

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.testkit.{ImplicitSender, TestKit}
import akka.util.Timeout
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

class ServiceTests extends TestKit(ActorSystem("TestSpec"))
  with ImplicitSender
  with AnyWordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  implicit val timeout = Timeout(10.seconds)
  implicit val executionContext = system.dispatcher

  val secondsToWait = 5.seconds

  val aggregateService: ActorRef = system.actorOf(Props(new ContactsService()), "test-contacts")

  override def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "ContactsService actor" must {

    "work correctly with no contacts in memory" in {
      val testRequest = GetSecondDegreeRequest(1)

      val answer = aggregateService ? testRequest

      Await.result(answer, secondsToWait) match {
        case SecondDegreeContactsResponse(res) =>
          res shouldBe Set.empty[Int]
      }
    }

    "work correctly with basic requests" in {
      val addRequests = List(
        AddContactsRequest(1, Array(2,3,4,5)),
        AddContactsRequest(2, Array(1,6,8)),
        AddContactsRequest(3, Array(1,4)),
        AddContactsRequest(4, Array(1,3)),
        AddContactsRequest(5, Array(1,6))
      )

      val getRequest = GetSecondDegreeRequest(1)
      val result = Set(6,8)

      addRequests.foreach(req => aggregateService ! req)

      val answer = aggregateService ? getRequest

      Await.result(answer, secondsToWait) match {
        case SecondDegreeContactsResponse(res) =>
          res shouldBe result
      }
    }
  }
}
