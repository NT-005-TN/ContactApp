package com.example.contactsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.contactsapp.ui.screens.dashboard.model.ContactAction
import com.example.contactsapp.ui.screens.dashboard.model.DashboardUiState
import com.example.contactsapp.ui.screens.dashboard.model.QuickAccessIcon
import com.example.contactsapp.ui.screens.dashboard.model.QuickAccessItem
import com.example.contactsapp.ui.screens.dashboard.model.RecentContact
import com.example.contactsapp.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.update {
            it.copy(
                totalContacts = 156,
                quickAccessItems = createQuickAccessItems(),
                recentContacts = createRecentContacts()
            )
        }
    }

    private fun createQuickAccessItems(): List<QuickAccessItem> {
        return listOf(
            QuickAccessItem("Все", "156 контактов", QuickAccessIcon.GROUP,
                BlueLightBackground, BluePrimary, BluePrimary),
            QuickAccessItem("Избранное", "23 контакта", QuickAccessIcon.STAR,
                OrangeBackground, OrangePrimary, OrangePrimary),
            QuickAccessItem("Недавние", "12 контактов", QuickAccessIcon.SCHEDULE,
                GreenBackground, GreenPrimary, GreenPrimary),
            QuickAccessItem("Группы", "8 групп", QuickAccessIcon.GRID,
                PurpleBackground, PurplePrimary, PurplePrimary)
        )
    }

    private fun createRecentContacts(): List<RecentContact> {
        return listOf(
            RecentContact(
                id = "1", initials = "АЛ", name = "Анна Лопес",
                timeAgo = "2 часа назад",
                avatarColor = BlueLightBackground,  
                actionType = ContactAction.PHONE
            ),
            RecentContact(
                id = "2", initials = "ПС", name = "Пётр Санчес",
                timeAgo = "5 часов назад",
                avatarColor = GreenBackground,      
                actionType = ContactAction.EMAIL
            ),
            RecentContact(
                id = "3", initials = "ЛГ", name = "Лаура Гарсия",
                timeAgo = "Вчера",
                avatarColor = PurpleBackground,     
                actionType = ContactAction.PHONE
            )
        )
    }
}