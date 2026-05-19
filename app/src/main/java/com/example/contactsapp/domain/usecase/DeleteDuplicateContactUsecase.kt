package com.example.contactsapp.domain.usecase

import com.example.contactsapp.domain.repository.ContactsRepository

class DeleteDuplicateContactsUseCase(
    private val repository: ContactsRepository
) {
    suspend operator fun invoke(): Int {
        return repository.deleteDuplicateContacts()
    }
}