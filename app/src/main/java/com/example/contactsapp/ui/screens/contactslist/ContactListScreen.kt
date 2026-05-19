package com.example.contactsapp.ui.screens.contactlist

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contactsapp.data.repository.ContactsRepositoryImpl
import com.example.contactsapp.domain.usecase.DeleteDuplicateContactsUseCase
import com.example.contactsapp.ui.screens.contactlist.components.AlphabetHeader
import com.example.contactsapp.ui.screens.contactlist.components.ContactDetailsRow
import com.example.contactsapp.ui.screens.contactlist.components.FilterTabs
import com.example.contactsapp.ui.screens.contactlist.model.ContactGroup
import com.example.contactsapp.ui.screens.contactlist.model.ContactListUiState
import com.example.contactsapp.ui.theme.BluePrimary
import com.example.contactsapp.viewmodel.ContactListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    viewModel: ContactListViewModel = viewModel(factory = ContactListViewModelFactory(LocalContext.current.applicationContext)),
    onBackClick: () -> Unit,
    onAddContactClick: () -> Unit,
    onContactClick: (String) -> Unit 
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.all { it.value }
        if (allGranted) {
            
        }
    }

    LaunchedEffect(Unit) {
        val hasRead = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_CONTACTS
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        val hasCall = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CALL_PHONE
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasRead || !hasCall) {
            permissionLauncher.launch(arrayOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.CALL_PHONE
            ))
        }
    }

    ContactListContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onAddContactClick = onAddContactClick,
        onFilterSelected = viewModel::onFilterSelected,
        onContactClick = onContactClick,
        onDeleteDuplicatesClick = { viewModel.onDeleteDuplicatesClick() },
        onSnackbarDismissed = { viewModel.hideSnackbar() },
        onMakeCall = { phoneNumber ->
            makePhoneCall(context, phoneNumber)
        }
    )
}

private fun makePhoneCall(context: Context, phoneNumber: String) {
    val cleanedNumber = phoneNumber.replace(Regex("[^\\d+]"), "")

    if (ContextCompat.checkSelfPermission(
            context, Manifest.permission.CALL_PHONE
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    ) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$cleanedNumber")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: SecurityException) {
        } catch (e: Exception) {
        }
    } else {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$cleanedNumber")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ContactListContent(
    uiState: ContactListUiState,
    onBackClick: () -> Unit,
    onAddContactClick: () -> Unit,
    onFilterSelected: (String) -> Unit,
    onContactClick: (String) -> Unit,
    onDeleteDuplicatesClick: () -> Unit,
    onSnackbarDismissed: () -> Unit,
    onMakeCall: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.showSnackbar, uiState.deletionMessage) {
        if (uiState.showSnackbar && uiState.deletionMessage != null) {
            snackbarHostState.showSnackbar(uiState.deletionMessage)
            onSnackbarDismissed()
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFFF9800)
                )
            },
            title = { Text("Удаление дубликатов") },
            text = {
                Text("Будут удалены контакты с полностью совпадающими полями (имя, телефон, email). Это действие нельзя отменить.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onDeleteDuplicatesClick()
                    }
                ) {
                    Text("Удалить", color = BluePrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Contactos", fontWeight = FontWeight.Bold)
                        Text(
                            text = "${getTotalCount(uiState.groups)} en total",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showConfirmDialog = true },
                        enabled = !uiState.isDeletingDuplicates && !uiState.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteSweep,
                            contentDescription = "Delete duplicates",
                            tint = if (uiState.isDeletingDuplicates) Color.Gray else Color.Unspecified
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddContactClick,
                containerColor = BluePrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    actionColor = BluePrimary,
                    containerColor = Color(0xFF333333),
                    contentColor = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterTabs(
                filters = listOf("Todos", "Favoritos", "Trabajo"),
                selectedFilter = uiState.selectedFilter,
                onFilterSelected = onFilterSelected
            )

            if (uiState.isDeletingDuplicates) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = BluePrimary,
                    trackColor = BluePrimary.copy(alpha = 0.24f)
                )
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = BluePrimary)
                }
            } else {
                LazyColumn {
                    uiState.groups.forEach { group ->
                        item(key = "header_${group.letter}") {
                            AlphabetHeader(letter = group.letter)
                        }
                        items(group.contacts, key = { it.id }) { contact ->
                            ContactDetailsRow(
                                contact = contact,
                                onClick = {
                                    
                                    onMakeCall(contact.contactInfo)
                                },
                                onDetailsClick = {
                                    
                                    onContactClick(contact.id)
                                },
                                onFavoriteClick = {
                                    
                                }
                            )
                        }
                    }
                    item {
                        Spacer(Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

private fun getTotalCount(groups: List<ContactGroup>): Int {
    return groups.sumOf { it.contacts.size }
}

class ContactListViewModelFactory(
    private val applicationContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactListViewModel::class.java)) {
            val repository = ContactsRepositoryImpl(applicationContext)
            val useCase = DeleteDuplicateContactsUseCase(repository)
            return ContactListViewModel(repository, useCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}