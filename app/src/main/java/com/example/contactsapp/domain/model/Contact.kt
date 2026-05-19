package com.example.contactsapp.domain.model

data class Contact(
    val id: Long,
    val displayName: String,
    val phoneNumber: String?,
    val email: String?,
    val address: String? = null,
    val company: String? = null,
    val department: String? = null,
    val jobTitle: String? = null,
    val notes: String? = null
)