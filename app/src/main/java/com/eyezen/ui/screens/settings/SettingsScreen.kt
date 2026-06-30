package com.eyezen.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Settings screen.
 *
 * Displays:
 * - Notification preferences
 * - Theme settings
 * - Break schedule
 * - Water goal
 * - About
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigate: (String) -> Unit = {}
) {
    var notificationsEnabled by remember { mutableStateOf(true) }
    var breakRemindersEnabled by remember { mutableStateOf(true) }
    var waterRemindersEnabled by remember { mutableStateOf(true) }
    var darkModeEnabled by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Notifications Section
            item {
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SettingCard(
                    title = "Enable Notifications",
                    icon = Icons.Default.Notifications,
                    isToggle = true,
                    value = notificationsEnabled,
                    onToggle = { notificationsEnabled = it }
                )
            }

            item {
                SettingCard(
                    title = "Break Reminders",
                    description = "Notify every 20 minutes",
                    isToggle = true,
                    value = breakRemindersEnabled,
                    onToggle = { breakRemindersEnabled = it },
                    enabled = notificationsEnabled
                )
            }

            item {
                SettingCard(
                    title = "Water Reminders",
                    description = "Notify every hour",
                    isToggle = true,
                    value = waterRemindersEnabled,
                    onToggle = { waterRemindersEnabled = it },
                    enabled = notificationsEnabled
                )
            }

            // Display Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Display",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SettingCard(
                    title = "Dark Mode",
                    isToggle = true,
                    value = darkModeEnabled,
                    onToggle = { darkModeEnabled = it }
                )
            }

            // Goals Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Goals",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SettingCard(
                    title = "Daily Break Goal",
                    description = "8 breaks/day",
                    onClick = { onNavigate("break_goal") }
                )
            }

            item {
                SettingCard(
                    title = "Daily Water Goal",
                    description = "8 glasses/day",
                    onClick = { onNavigate("water_goal") }
                )
            }

            // About Section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                SettingCard(
                    title = "Version",
                    description = "1.0.0"
                )
            }

            item {
                SettingCard(
                    title = "Privacy Policy",
                    onClick = { onNavigate("privacy") }
                )
            }

            item {
                SettingCard(
                    title = "Terms of Service",
                    onClick = { onNavigate("terms") }
                )
            }
        }
    }
}

/**
 * Setting card component
 */
@Composable
private fun SettingCard(
    title: String,
    description: String? = null,
    icon: androidx.compose.material.icons.Icons? = null,
    isToggle: Boolean = false,
    value: Boolean = false,
    onToggle: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    enabled: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null && !isToggle && enabled) {
                    Modifier.clickable { onClick() }
                } else {
                    Modifier
                }
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (description != null) {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                    )
                }
            }

            if (isToggle && onToggle != null) {
                Switch(
                    checked = value,
                    onCheckedChange = onToggle,
                    enabled = enabled
                )
            } else if (!isToggle) {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        }
    }
}
