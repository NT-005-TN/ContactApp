package com.example.contactsapp.ui.screens.contactlist

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.contactsapp.ui.screens.contactlist.model.*
import com.example.contactsapp.ui.theme.*

@Preview(showBackground = true, device = "id:pixel_8", name = "ContactList - Заполненный")
@Composable
fun ContactListScreenPreview() {
    ContactsAppTheme {
        ContactListContent(
            uiState = sampleContactListState(),
            onBackClick = {},
            onAddContactClick = {},
            onFilterSelected = {},
            onContactClick = {},
            onDeleteDuplicatesClick = {},
            onSnackbarDismissed = {},
            onMakeCall = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "ContactList - Тёмная тема")
@Composable
fun ContactListScreenDarkPreview() {
    ContactsAppTheme(darkTheme = true) {
        ContactListContent(
            uiState = sampleContactListState(),
            onBackClick = {},
            onAddContactClick = {},
            onFilterSelected = {},
            onContactClick = {},
            onDeleteDuplicatesClick = {},   
            onSnackbarDismissed = {},
            onMakeCall = {}
        )
    }
}

@Preview(name = "ContactList - Загрузка")
@Composable
fun ContactListScreenLoadingPreview() {
    ContactsAppTheme {
        ContactListContent(
            uiState = ContactListUiState(isLoading = true),
            onBackClick = {},
            onAddContactClick = {},
            onFilterSelected = {},
            onContactClick = {},
            onDeleteDuplicatesClick = {},   
            onSnackbarDismissed = {},
            onMakeCall = {}
        )
    }
}

@Preview(name = "ContactList - Пустой список")
@Composable
fun ContactListScreenEmptyPreview() {
    ContactsAppTheme {
        ContactListContent(
            uiState = ContactListUiState(
                selectedFilter = "Todos",
                groups = emptyList()
            ),
            onBackClick = {},
            onAddContactClick = {},
            onFilterSelected = {},
            onContactClick = {},
            onDeleteDuplicatesClick = {},   
            onSnackbarDismissed = {},
            onMakeCall = {}
        )
    }
}

@Preview(name = "ContactList - Только избранное")
@Composable
fun ContactListScreenFavoritesPreview() {
    ContactsAppTheme {
        ContactListContent(
            uiState = ContactListUiState(
                selectedFilter = "Favoritos",
                groups = listOf(
                    ContactGroup("A", listOf(
                        ContactUiModel("1", "AL", "Ana López Martínez", "+34 612 345 678",
                            BlueLightBackground, true)
                    )),
                    ContactGroup("C", listOf(
                        ContactUiModel("3", "CM", "Carmen Martín", "+34 623 456 789",
                            PurpleBackground, true)
                    )),
                    ContactGroup("L", listOf(
                        ContactUiModel("5", "LG", "Laura García", "+34 634 567 890",
                            GreenBackground, true)
                    ))
                )
            ),
            onBackClick = {},
            onAddContactClick = {},
            onFilterSelected = {},
            onContactClick = {},
            onDeleteDuplicatesClick = {},   
            onSnackbarDismissed = {},
            onMakeCall = {}
        )
    }
}

private fun sampleContactListState() = ContactListUiState(
    selectedFilter = "Todos",
    groups = listOf(
        ContactGroup("A", listOf(
            ContactUiModel("1", "AL", "Ana López Martínez", "+34 612 345 678",
                BlueLightBackground, true),
            ContactUiModel("2", "AR", "Antonio Ruiz", "antonio.ruiz@email.com",
                GreenBackground, false)
        )),
        ContactGroup("C", listOf(
            ContactUiModel("3", "CM", "Carmen Martín", "+34 623 456 789",
                PurpleBackground, true),
            ContactUiModel("4", "CR", "Carlos Rodríguez", "carlos.r@empresa.io",
                BlueLightBackground, false)
        )),
        ContactGroup("L", listOf(
            ContactUiModel("5", "LG", "Laura García", "+34 634 567 890",
                GreenBackground, true),
            ContactUiModel("6", "LM", "Luis Moreno", "luis.m@startup.io",
                PurpleBackground, false)
        )),
        ContactGroup("M", listOf(
            ContactUiModel("7", "MF", "María Fernández", "+34 645 678 901",
                BlueLightBackground, false)
        ))
    )
)