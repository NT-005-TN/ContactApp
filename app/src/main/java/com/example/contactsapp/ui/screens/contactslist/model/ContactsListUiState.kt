package com.example.contactsapp.ui.screens.contactslist.model

import androidx.compose.ui.graphics.Color

data class ContactUiModel(
    val id: String,
    val initials: String,
    val fullName: String,
    val contactInfo: String,
    val avatarColor: Color,
    val isFavorite: Boolean
)

data class ContactGroup(
    val letter: String,
    val contacts: List<ContactUiModel>
)

data class ContactListUiState(
    val isLoading: Boolean = false,
    val isDeletingDuplicates: Boolean = false,
    val selectedFilter: String = "Todos",
    val groups: List<ContactGroup> = emptyList(),
    val errorMessage: String? = null,
    val showSnackbar: Boolean = false,
    val deletionMessage: String? = null
)