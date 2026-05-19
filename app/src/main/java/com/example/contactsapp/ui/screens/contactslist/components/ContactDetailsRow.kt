package com.example.contactsapp.ui.screens.contactslist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactsapp.ui.screens.contactslist.model.ContactUiModel
import com.example.contactsapp.ui.theme.*

@Composable
fun ContactDetailsRow(
    contact: ContactUiModel,
    onClick: () -> Unit,        
    onDetailsClick: () -> Unit, 
    onFavoriteClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick) 
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(contact.avatarColor),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = contact.initials,
                color = getContrastTextColor(contact.avatarColor),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }

        Spacer(Modifier.width(16.dp))

        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = contact.fullName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary
            )
            Text(
                text = contact.contactInfo,
                fontSize = 14.sp,
                color = TextSecondary
            )
        }

        
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Показать информацию",
            tint = BluePrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    onClick = onDetailsClick,
                    onClickLabel = "Показать информацию о контакте"
                )
                .padding(end = 8.dp)
        )

        
        Icon(
            imageVector = if (contact.isFavorite)
                Icons.Default.Star else Icons.Outlined.StarBorder,
            contentDescription = if (contact.isFavorite) "В избранном" else "Добавить в избранное",
            tint = if (contact.isFavorite) Color(0xFFFFB300) else TextSecondary,
            modifier = Modifier
                .size(24.dp)
                .clickable(
                    onClick = onFavoriteClick,
                    onClickLabel = "Переключить избранное"
                )
        )
    }
}

private fun getContrastTextColor(backgroundColor: Color): Color {
    return if (backgroundColor.luminance() > 0.5f) Color.Black else Color.White
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ContactDetailsRowPreview() {
    ContactsAppTheme {
        ContactDetailsRow(
            contact = ContactUiModel(
                id = "1",
                initials = "АЛ",
                fullName = "Анна Лопес",
                contactInfo = "+34 612 345 678",
                avatarColor = BlueLightBackground,
                isFavorite = true
            ),
            onClick = {},
            onDetailsClick = {},
            onFavoriteClick = {}
        )
    }
}