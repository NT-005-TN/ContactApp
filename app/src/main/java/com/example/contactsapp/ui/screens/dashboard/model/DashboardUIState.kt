package com.example.contactsapp.ui.screens.dashboard.model

import androidx.compose.ui.graphics.Color

enum class QuickAccessIcon { GROUP, STAR, SCHEDULE, GRID }

data class QuickAccessItem(
    val title: String,
    val count: String,
    val iconType: QuickAccessIcon,
    val backgroundColor: Color,
    val iconBackgroundColor: Color,
    val countColor: Color
)

data class RecentContact(
    val id: String,
    val initials: String,
    val name: String,
    val timeAgo: String,
    val avatarColor: Color,
    val actionType: ContactAction
)

enum class ContactAction { PHONE, EMAIL }

data class DashboardUiState(
    val totalContacts: Int = 0,
    val quickAccessItems: List<QuickAccessItem> = emptyList(),
    val recentContacts: List<RecentContact> = emptyList()
)