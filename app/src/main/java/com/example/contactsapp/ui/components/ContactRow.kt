package com.example.contactsapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactsapp.ui.screens.dashboard.model.ContactAction
import com.example.contactsapp.ui.theme.*

@Composable
fun ContactRow(
    modifier: Modifier = Modifier,
    initials: String,
    name: String,
    time: String,
    avatarColor: Color,          
    initialsColor: Color,        
    actionIcon: ImageVector,
    onClick: () -> Unit = {},
    onActionClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(color = avatarColor, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = initials,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = initialsColor
            )
        }

        Spacer(Modifier.width(14.dp))

        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Text(
                text = time,
                fontSize = 12.sp,
                color = TextSecondary,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        
        Icon(
            imageVector = actionIcon,
            contentDescription = "Действие",
            tint = BluePrimary,
            modifier = Modifier
                .size(22.dp)
                .clickable(onClick = onActionClick)
        )
    }
}

@Composable
fun getIconForContactAction(action: ContactAction): ImageVector {
    return when (action) {
        ContactAction.PHONE -> Icons.Default.Phone
        ContactAction.EMAIL -> Icons.Default.Email
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun ContactRowPreview() {
    ContactsAppTheme {
        ContactRow(
            initials = "AL",
            name = "Ana López",
            time = "Hace 2 horas",
            avatarColor = BlueLightBackground,
            initialsColor = Color.White,  
            actionIcon = Icons.Default.Phone
        )
    }
}