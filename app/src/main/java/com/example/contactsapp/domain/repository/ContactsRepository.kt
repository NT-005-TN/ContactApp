package com.example.contactsapp.domain.repository

import com.example.contactsapp.domain.model.Contact

interface ContactsRepository {
    suspend fun getContacts(): List<Contact>
    suspend fun deleteDuplicateContacts(): Int
    suspend fun getContactById(contactId: Long): Contact?
}