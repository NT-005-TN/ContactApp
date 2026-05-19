package com.example.contactsapp.ui.screens.contactdetail

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.contactsapp.ui.screens.contactdetail.model.ContactDetail
import com.example.contactsapp.ui.screens.contactdetail.model.ContactDetailUiState
import com.example.contactsapp.ui.theme.*

@Preview(showBackground = true, device = "id:pixel_8", name = "Contact Detail - Заполненный")
@Composable
private fun ContactDetailScreenPreview() {
    ContactsAppTheme {
        ContactDetailContent(
            uiState = sampleContactDetailState(isFavorite = true),
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_8", name = "Contact Detail - Не в избранном")
@Composable
private fun ContactDetailScreenNotFavoritePreview() {
    ContactsAppTheme {
        ContactDetailContent(
            uiState = sampleContactDetailState(isFavorite = false),
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_8", name = "Contact Detail - Загрузка")
@Composable
private fun ContactDetailScreenLoadingPreview() {
    ContactsAppTheme {
        ContactDetailContent(
            uiState = ContactDetailUiState(
                isLoading = true,
                contact = null,
                isFavorite = false
            ),
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    device = "id:pixel_8",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "Contact Detail - Тёмная тема"
)
@Composable
private fun ContactDetailScreenDarkPreview() {
    ContactsAppTheme(darkTheme = true) {
        ContactDetailContent(
            uiState = sampleContactDetailState(isFavorite = true).copy(
                contact = sampleContactDetailState(isFavorite = true).contact?.copy(
                    initials = "LG",
                    fullName = "Laura García",
                    jobTitle = "Diseñadora UX",
                    phoneNumber = "+34 634 567 890",
                    email = "laura.garcia@design.io",
                    address = "Calle Serrano 89, Madrid",
                    notes = "Experta en diseño de interfaces.",
                    company = "Design Studio",
                    department = "Creative",
                    avatarColor = GreenBackground
                )
            ),
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_8", name = "Contact Detail - Пустой")
@Composable
private fun ContactDetailScreenEmptyPreview() {
    ContactsAppTheme {
        ContactDetailContent(
            uiState = ContactDetailUiState(
                isLoading = false,
                contact = null,
                isFavorite = false
            ),
            onBackClick = {},
            onFavoriteClick = {}
        )
    }
}

private fun sampleContactDetailState(isFavorite: Boolean) = ContactDetailUiState(
    isLoading = false,
    contact = ContactDetail(
        id = "1",
        initials = "MG",
        fullName = "María González",
        jobTitle = "Directora de Marketing",
        phoneNumber = "+34 612 345 678",
        email = "maria.gonzalez@empresa.com",
        address = "Calle Mayor 123, 3° A\n28013 Madrid, España",
        notes = "Contacto preferido para temas de marketing digital.\nDisponible de lunes a viernes de 9:00 a 18:00.",
        company = "Tech Solutions S.L.",
        department = "Marketing",
        avatarColor = PurpleBackground,
        isFavorite = isFavorite
    ),
    isFavorite = isFavorite
)