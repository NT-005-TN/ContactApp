package com.example.contactsapp.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactsapp.domain.usecase.GetContactByIdUseCase
import com.example.contactsapp.ui.screens.contactdetail.model.ContactDetail
import com.example.contactsapp.ui.screens.contactdetail.model.ContactDetailUiState
import com.example.contactsapp.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContactDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getContactByIdUseCase: GetContactByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactDetailUiState())
    val uiState: StateFlow<ContactDetailUiState> = _uiState.asStateFlow()

    init {
        loadContactDetails()
    }

    private fun loadContactDetails() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            try {
                val contactIdStr = savedStateHandle.get<String>("contactId")
                val contactId = contactIdStr?.toLongOrNull() ?: return@launch

                val contact = getContactByIdUseCase(contactId)

                if (contact != null) {
                    val contactDetail = ContactDetail(
                        id = contact.id.toString(),
                        initials = getInitials(contact.displayName),
                        fullName = contact.displayName.ifBlank { "Без имени" },
                        jobTitle = contact.jobTitle ?: "",
                        phoneNumber = contact.phoneNumber ?: "Не указан",
                        email = contact.email ?: "Не указан",
                        address = contact.address ?: "Не указан",
                        notes = contact.notes ?: "Нет заметок",
                        company = contact.company ?: "Не указана",
                        department = contact.department ?: "Не указан",
                        avatarColor = getAvatarColor(contact.displayName),
                        isFavorite = false
                    )

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            contact = contactDetail,
                            isFavorite = false
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            contact = null,
                            isFavorite = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        contact = null,
                        isFavorite = false
                    )
                }
            }
        }
    }

    private fun getInitials(name: String): String {
        if (name.isBlank()) return "?"
        val parts = name.trim().split(" ")
        return when {
            parts.size >= 2 -> "${parts[0].firstOrNull()?.uppercaseChar()}${parts[1].firstOrNull()?.uppercaseChar()}"
            parts.size == 1 -> parts[0].take(2).uppercase()
            else -> "?"
        }
    }

    private fun getAvatarColor(name: String): androidx.compose.ui.graphics.Color {
        return when (name.hashCode() % 4) {
            0 -> BlueLightBackground
            1 -> GreenBackground
            2 -> PurpleBackground
            else -> OrangeBackground
        }
    }

    fun toggleFavorite() {
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }
}