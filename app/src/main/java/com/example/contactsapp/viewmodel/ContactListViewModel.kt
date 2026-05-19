package com.example.contactsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsapp.data.mapper.ContactMapper
import com.example.contactsapp.data.repository.ContactsRepositoryImpl
import com.example.contactsapp.data.service.ContactsService
import com.example.contactsapp.domain.usecase.DeleteDuplicateContactsUseCase
import com.example.contactsapp.ui.screens.contactslist.model.ContactGroup
import com.example.contactsapp.ui.screens.contactslist.model.ContactListUiState
import com.example.contactsapp.ui.screens.contactslist.model.ContactUiModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactListViewModel(
    private val repository: ContactsRepositoryImpl,
    private val deleteUseCase: DeleteDuplicateContactsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactListUiState())
    val uiState: StateFlow<ContactListUiState> = _uiState.asStateFlow()

    init {
        loadContacts()
    }

    private fun loadContacts() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val domainContacts = repository.getContacts()
                val uiContacts = domainContacts.map { ContactMapper.toUiModel(it) }
                val filtered = applyFilter(uiContacts, _uiState.value.selectedFilter)
                val grouped = groupContactsByLetter(filtered)

                _uiState.update {
                    it.copy(isLoading = false, groups = grouped)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

    fun onDeleteDuplicatesClick() {
        _uiState.update { it.copy(isDeletingDuplicates = true) }

        viewModelScope.launch {
            val resultCode = deleteUseCase()

            val message = when (resultCode) {
                ContactsService.RESULT_SUCCESS -> "Повторяющиеся контакты удалены успешно"
                ContactsService.RESULT_ERROR -> "Произошла ошибка"
                ContactsService.RESULT_NOT_FOUND -> "Повторяющиеся контакты не найдены"
                else -> "Произошла ошибка"
            }

            _uiState.update {
                it.copy(
                    isDeletingDuplicates = false,
                    showSnackbar = true,
                    deletionMessage = message
                )
            }

            loadContacts()

            delay(3000)
            hideSnackbar()
        }
    }

    fun hideSnackbar() {
        _uiState.update {
            it.copy(
                showSnackbar = false,
                deletionMessage = null
            )
        }
    }

    fun onFilterSelected(filter: String) {
        _uiState.update { it.copy(selectedFilter = filter) }
        loadContacts()
    }

    private fun applyFilter(
        contacts: List<ContactUiModel>,
        filter: String
    ) = when (filter) {
        "Favoritos" -> contacts.filter { it.isFavorite }
        "Trabajo" -> contacts.filter { it.contactInfo.contains("@") }
        else -> contacts
    }

    private fun groupContactsByLetter(
        contacts: List<ContactUiModel>
    ): List<ContactGroup> = contacts
        .groupBy { it.fullName.firstOrNull()?.uppercaseChar()?.toString() ?: "#" }
        .map { (letter, groupContacts) -> ContactGroup(letter, groupContacts) }
        .sortedBy { it.letter }
}