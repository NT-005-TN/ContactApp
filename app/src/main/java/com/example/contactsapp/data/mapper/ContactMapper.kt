package com.example.contactsapp.data.mapper

import com.example.contactsapp.domain.model.Contact
import com.example.contactsapp.ui.screens.contactslist.model.ContactUiModel
import com.example.contactsapp.ui.theme.*
import androidx.compose.ui.graphics.Color

object ContactMapper {
    
    fun toUiModel(contact: Contact): ContactUiModel {
        val initials = getInitials(contact.displayName)
        val avatarColor = getAvatarColor(contact.displayName)

        return ContactUiModel(
            id = contact.id.toString(),
            initials = initials,
            fullName = contact.displayName.ifBlank { "Без имени" },
            contactInfo = contact.phoneNumber ?: contact.email ?: "",
            avatarColor = avatarColor,
            isFavorite = false
        )
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

    private fun getAvatarColor(name: String): Color {
        return when (name.hashCode() % 4) {
            0 -> BlueLightBackground
            1 -> GreenBackground
            2 -> PurpleBackground
            else -> OrangeBackground
        }
    }
}