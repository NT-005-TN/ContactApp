package com.example.contactsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactsapp.ui.screens.dashboard.model.QuickAccessIcon
import com.example.contactsapp.ui.theme.TextPrimary

@Composable
fun QuickAccessCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    iconBackgroundColor: Color,
    icon: ImageVector,
    title: String,
    subtitle: String,
    subtitleColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .aspectRatio(1.15f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = iconBackgroundColor,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = subtitleColor,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun getIconForQuickAccessType(type: QuickAccessIcon): ImageVector {
    return when (type) {
        QuickAccessIcon.GROUP -> androidx.compose.material.icons.Icons.Default.Group
        QuickAccessIcon.STAR -> androidx.compose.material.icons.Icons.Default.Star
        QuickAccessIcon.SCHEDULE -> androidx.compose.material.icons.Icons.Default.Schedule
        QuickAccessIcon.GRID -> androidx.compose.material.icons.Icons.Default.GridOn
    }
}