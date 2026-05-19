package com.example.contactsapp.domain.usecase

import com.example.contactsapp.domain.model.Contact
import com.example.contactsapp.domain.repository.ContactsRepository

class GetContactByIdUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(contactId: Long): Contact? {
        return repository.getContactById(contactId)
    }
}