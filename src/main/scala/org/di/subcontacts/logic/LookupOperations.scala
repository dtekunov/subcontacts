package org.di.subcontacts.logic

import org.di.subcontacts.ContactsServiceProtocol.ContactId

import scala.collection.concurrent.TrieMap
import scala.collection.immutable.HashSet

trait LookupOperations {

  /**
   * Safely returns all contacts by a given userId
   */
  def getContacts(userId: ContactId, target: TrieMap[ContactId, Array[ContactId]]): Set[ContactId] = {
    target.get(userId) match {
      case Some(contacts) => contacts.toSet
      case None => HashSet.empty[ContactId]
    }
  }

  /**
   * Returns direct contacts for a given set without repetitions
   */
  def getSubContacts(contacts: Set[ContactId], target: TrieMap[ContactId, Array[ContactId]]): Set[ContactId] = {
    contacts.foldLeft(Set.empty[ContactId]) { (acc, elem) =>
      acc ++ getContacts(elem, target)
    }
  }

  /**
   * Only return 2nd-degree contacts:
   *
   * Direct contacts of the user as well as the user herself should not be returned
   */
  def getSecondDegreeContacts(userId: ContactId, target: TrieMap[ContactId, Array[ContactId]]): Set[ContactId] = {
    val firstDegreeContacts = getContacts(userId, target)
    val subContacts = getSubContacts(firstDegreeContacts, target)

    val secondDegreeContacts = subContacts.foldLeft(Set.empty[ContactId]) { (acc, elem) =>
      if (firstDegreeContacts.contains(elem)) acc
      else acc + elem
    }

    if (secondDegreeContacts.contains(userId)) secondDegreeContacts - userId
    else secondDegreeContacts
  }

}
