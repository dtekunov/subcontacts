package org.di.subcontacts

import org.di.subcontacts.ContactsServiceProtocol.ContactId
import spray.json.DefaultJsonProtocol.{IntJsonFormat, arrayFormat, jsonFormat1}

import scala.language.implicitConversions

/**
 * Provides marshalling of the requests
 */
object ContactsServiceProtocol {
  type ContactId = Int
  case class ContactIds(contacts: Array[ContactId])

  implicit val requestFormat = jsonFormat1(ContactIds)

  def formatResponse(result: Set[ContactId]): String =
    "[" + result.mkString(", ") + "]"
}

trait ServiceRequest
case class AddContactsRequest(userId: ContactId, contacts: Array[ContactId]) extends ServiceRequest
case class GetSecondDegreeRequest(userId: ContactId) extends ServiceRequest

case class SecondDegreeContactsResponse(result: Set[ContactId])