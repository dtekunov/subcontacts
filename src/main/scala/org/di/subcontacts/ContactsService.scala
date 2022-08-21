package org.di.subcontacts

import akka.actor.{Actor, ActorLogging}
import org.di.subcontacts.ContactsServiceProtocol.ContactId
import org.di.subcontacts.logic.LookupOperations

import scala.collection.concurrent.TrieMap

/**
 * Actor, responsible for add new contacts and get second degree contacts
 */
class ContactsService extends Actor with ActorLogging with LookupOperations {
  implicit val system = context.system

  private val contactsHolder = TrieMap.empty[ContactId, Array[ContactId]]

  def receive = {
    case AddContactsRequest(userId, contacts) =>
      log.info(s"Acquired request to add new contacts for $userId")
      contactsHolder.addOne(userId, contacts)

    case GetSecondDegreeRequest(userId) =>
      val result = getSecondDegreeContacts(userId, contactsHolder)
      log.info(s"Calculated second-degree contacts for $userId")
      sender() ! SecondDegreeContactsResponse(result)
  }

}