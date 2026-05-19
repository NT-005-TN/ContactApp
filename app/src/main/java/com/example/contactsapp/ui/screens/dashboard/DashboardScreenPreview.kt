package com.example.contactsapp.ui.screens.dashboard

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.contactsapp.ui.screens.dashboard.model.*
import com.example.contactsapp.ui.theme.*

@Preview(showBackground = true, device = "id:pixel_8", name = "Dashboard - Заполненный")
@Composable
fun DashboardScreenPreview() {
    ContactsAppTheme {
        DashboardContent(
            uiState = sampleDashboardState(),
            onQuickAccessClick = {}
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dashboard - Тёмная тема")
@Composable
fun DashboardScreenDarkPreview() {
    ContactsAppTheme(darkTheme = true) {
        DashboardContent(
            uiState = sampleDashboardState(),
            onQuickAccessClick = {}
        )
    }
}

@Preview(name = "Dashboard - Пустой список")
@Composable
fun DashboardScreenEmptyPreview() {
    ContactsAppTheme {
        DashboardContent(
            uiState = DashboardUiState(
                totalContacts = 0,
                quickAccessItems = listOf(
                    QuickAccessItem("Все", "0 контактов", QuickAccessIcon.GROUP,
                        BlueLightBackground, BluePrimary, BluePrimary),
                    QuickAccessItem("Избранное", "0 контактов", QuickAccessIcon.STAR,
                        OrangeBackground, OrangePrimary, OrangePrimary),
                    QuickAccessItem("Недавние", "0 контактов", QuickAccessIcon.SCHEDULE,
                        GreenBackground, GreenPrimary, GreenPrimary),
                    QuickAccessItem("Группы", "0 групп", QuickAccessIcon.GRID,
                        PurpleBackground, PurplePrimary, PurplePrimary)
                ),
                recentContacts = emptyList()
            ),
            onQuickAccessClick = {}
        )
    }
}

private fun sampleDashboardState() = DashboardUiState(
    totalContacts = 156,
    quickAccessItems = listOf(
        QuickAccessItem("Все", "156 контактов", QuickAccessIcon.GROUP,
            BlueLightBackground, BluePrimary, BluePrimary),
        QuickAccessItem("Избранное", "23 контакта", QuickAccessIcon.STAR,
            OrangeBackground, OrangePrimary, OrangePrimary),
        QuickAccessItem("Недавние", "12 контактов", QuickAccessIcon.SCHEDULE,
            GreenBackground, GreenPrimary, GreenPrimary),
        QuickAccessItem("Группы", "8 групп", QuickAccessIcon.GRID,
            PurpleBackground, PurplePrimary, PurplePrimary)
    ),
    recentContacts = listOf(
        RecentContact("1", "АЛ", "Анна Лопес", "2 часа назад",
            BlueLightBackground, ContactAction.PHONE),
        RecentContact("2", "ПС", "Пётр Санчес", "5 часов назад",
            GreenBackground, ContactAction.EMAIL),
        RecentContact("3", "ЛГ", "Лаура Гарсия", "Вчера",
            PurpleBackground, ContactAction.PHONE)
    )
)