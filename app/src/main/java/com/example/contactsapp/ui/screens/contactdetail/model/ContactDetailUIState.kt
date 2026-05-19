package com.example.contactsapp.ui.screens.contactdetail.model

import androidx.compose.ui.graphics.Color

data class ContactDetail(
    val id: String,
    val initials: String,
    val fullName: String,
    val jobTitle: String,
    val phoneNumber: String,
    val email: String,
    val address: String,
    val notes: String,
    val company: String,
    val department: String,
    val avatarColor: Color,
    val isFavorite: Boolean
)

data class ContactDetailUiState(
    val isLoading: Boolean = false,
    val contact: ContactDetail? = null,
    val isFavorite: Boolean = false
)