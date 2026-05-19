package com.example.contactsapp.ui.components.preview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.contactsapp.ui.components.QuickAccessCard
import com.example.contactsapp.ui.theme.*

@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5)
@Composable
fun QuickAccessCardPreview() {
    ContactsAppTheme {
        QuickAccessCard(
            modifier = Modifier,
            backgroundColor = BlueLightBackground,
            iconBackgroundColor = BluePrimary,
            icon = Icons.Default.Group,
            title = "Все",
            subtitle = "156 контактов",
            subtitleColor = BluePrimary
        )
    }
}