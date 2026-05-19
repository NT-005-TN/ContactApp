package com.example.contactsapp.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contactsapp.ui.components.ContactRow
import com.example.contactsapp.ui.components.QuickAccessCard
import com.example.contactsapp.ui.components.SectionHeader
import com.example.contactsapp.ui.components.getIconForContactAction
import com.example.contactsapp.ui.components.getIconForQuickAccessType
import com.example.contactsapp.ui.screens.dashboard.model.DashboardUiState
import com.example.contactsapp.ui.screens.dashboard.model.RecentContact
import com.example.contactsapp.ui.theme.*
import com.example.contactsapp.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onNavigateToList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    DashboardContent(
        uiState = uiState,
        onQuickAccessClick = { onNavigateToList() }
    )
}

@Composable
internal fun DashboardContent(
    uiState: DashboardUiState,
    onQuickAccessClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(modifier = Modifier.fillMaxSize(), color = BackgroundGray) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        color = BluePrimary,
                        shape = RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp)
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 48.dp, start = 24.dp, end = 24.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Меню",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color = BlueLight, shape = CircleShape)
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                    Text(
                        text = "Мои контакты",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${uiState.totalContacts} контактов сохранено",
                        fontSize = 14.sp,
                        color = TextLightBlue
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(190.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Поиск",
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Поиск контактов...",
                            color = TextSecondary,
                            fontSize = 15.sp
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                SectionHeader(
                    title = "Быстрый доступ",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.quickAccessItems.getOrNull(0)?.let { item ->
                        QuickAccessCardItem(
                            item = item,
                            onClick = onQuickAccessClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    uiState.quickAccessItems.getOrNull(1)?.let { item ->
                        QuickAccessCardItem(
                            item = item,
                            onClick = onQuickAccessClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    uiState.quickAccessItems.getOrNull(2)?.let { item ->
                        QuickAccessCardItem(
                            item = item,
                            onClick = onQuickAccessClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    uiState.quickAccessItems.getOrNull(3)?.let { item ->
                        QuickAccessCardItem(
                            item = item,
                            onClick = onQuickAccessClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                SectionHeader(
                    title = "Недавние контакты",
                    showArrow = true,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                uiState.recentContacts.forEach { contact ->
                    DashboardContactRow(contact = contact)
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}


@Composable
private fun QuickAccessCardItem(
    item: com.example.contactsapp.ui.screens.dashboard.model.QuickAccessItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    QuickAccessCard(
        modifier = modifier,
        backgroundColor = item.backgroundColor,
        iconBackgroundColor = item.iconBackgroundColor,
        icon = getIconForQuickAccessType(item.iconType),
        title = item.title,
        subtitle = item.count,
        subtitleColor = item.countColor,
        onClick = onClick
    )
}


@Composable
private fun DashboardContactRow(contact: RecentContact) {
    ContactRow(
        modifier = Modifier.padding(horizontal = 24.dp),
        initials = contact.initials,
        name = contact.name,
        time = contact.timeAgo,
        avatarColor = contact.avatarColor,
        initialsColor = Color.White,
        actionIcon = getIconForContactAction(contact.actionType),
        onClick = {},
        onActionClick = {}
    )
}