package com.example.contactsapp.ui.screens.contactslist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.contactsapp.data.repository.ContactsRepositoryImpl
import com.example.contactsapp.domain.usecase.DeleteDuplicateContactsUseCase
import com.example.contactsapp.domain.usecase.GetContactByIdUseCase
import com.example.contactsapp.viewmodel.ContactDetailViewModel
import com.example.contactsapp.viewmodel.ContactListViewModel

class ContactListViewModelFactory(
    private val applicationContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactListViewModel::class.java)) {
            val repository = ContactsRepositoryImpl(applicationContext)
            val useCase = DeleteDuplicateContactsUseCase(repository)
            return ContactListViewModel(repository, useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}

class ContactDetailViewModelFactory(
    private val applicationContext: Context,
    private val savedStateHandle: androidx.lifecycle.SavedStateHandle
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactDetailViewModel::class.java)) {
            val repository = ContactsRepositoryImpl(applicationContext)
            val getContactByIdUseCase = GetContactByIdUseCase(repository)
            return ContactDetailViewModel(savedStateHandle, getContactByIdUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}