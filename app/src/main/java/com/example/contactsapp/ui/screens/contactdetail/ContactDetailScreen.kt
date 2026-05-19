package com.example.contactsapp.ui.screens.contactdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contactsapp.ui.screens.contactdetail.model.ContactDetail
import com.example.contactsapp.ui.screens.contactdetail.model.ContactDetailUiState
import com.example.contactsapp.ui.theme.*
import com.example.contactsapp.viewmodel.ContactDetailViewModel
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(
    viewModel: ContactDetailViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ContactDetailContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onFavoriteClick = { viewModel.toggleFavorite() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContactDetailContent(
    uiState: ContactDetailUiState,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (uiState.isFavorite)
                                Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Избранное",
                            tint = if (uiState.isFavorite) Color(0xFFFFD700) else Color.White
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Меню",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PurplePrimary
                )
            )
        }
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            uiState.contact?.let { contact ->
                ContactDetailBody(
                    contact = contact,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(paddingValues)
                )
            }
        }
    }
}

@Composable
private fun ContactDetailBody(
    contact: ContactDetail,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(PurplePrimary)
                .padding(bottom = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = contact.initials,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = contact.fullName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = contact.jobTitle,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ActionButton(icon = Icons.Default.Phone, label = "Позвонить", onClick = {})
                ActionButton(icon = Icons.Default.ChatBubbleOutline, label = "Сообщение", onClick = {})
                ActionButton(icon = Icons.Default.Videocam, label = "Видео", onClick = {})
            }
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Информация о контакте",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        ContactInfoItem(
            icon = Icons.Default.Phone,
            title = "Номер телефона",
            content = contact.phoneNumber,
            iconBackgroundColor = PurplePrimary
        )
        ContactInfoItem(
            icon = Icons.Default.Email,
            title = "Почта",
            content = contact.email,
            iconBackgroundColor = PurplePrimary
        )
        ContactInfoItem(
            icon = Icons.Default.LocationOn,
            title = "Адрес",
            content = contact.address,
            iconBackgroundColor = PurplePrimary
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Заметки",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextSecondary,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = OrangeBackground.copy(alpha = 0.3f)
            )
        ) {
            Text(
                text = contact.notes,
                fontSize = 14.sp,
                color = TextPrimary,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompanyInfoCard(title = "Компания", content = contact.company, modifier = Modifier.weight(1f))
            CompanyInfoCard(title = "Отдел", content = contact.department, modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun ActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(PurpleBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = PurplePrimary,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.height(8.dp))
        Text(text = label, fontSize = 12.sp, color = TextSecondary)
    }
}

@Composable
private fun ContactInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    content: String,
    iconBackgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 11.sp, color = TextSecondary, fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(4.dp))
                Text(text = content, fontSize = 14.sp, color = TextPrimary)
            }
        }
    }
}

@Composable
private fun CompanyInfoCard(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 11.sp, color = TextSecondary)
            Spacer(Modifier.height(4.dp))
            Text(text = content, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
        }
    }
}