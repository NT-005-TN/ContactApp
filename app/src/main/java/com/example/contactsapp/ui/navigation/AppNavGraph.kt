package com.example.contactsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.contactsapp.ui.screens.contactdetail.ContactDetailScreen
import com.example.contactsapp.ui.screens.contactlist.ContactListScreen
import com.example.contactsapp.ui.screens.dashboard.DashboardScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.contactsapp.ui.screens.contactslist.ContactDetailViewModelFactory

object Routes {
    const val DASHBOARD = "dashboard"
    const val CONTACT_LIST = "contact_list"
    const val CONTACT_DETAIL = "contact_detail/{contactId}"

    fun contactDetailRoute(contactId: String) = "contact_detail/$contactId"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = Routes.DASHBOARD) {

        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onNavigateToList = { navController.navigate(Routes.CONTACT_LIST) }
            )
        }

        composable(Routes.CONTACT_LIST) {
            ContactListScreen(
                onBackClick = { navController.popBackStack() },
                onAddContactClick = { /* TODO */ },
                onContactClick = { contactId ->
                    navController.navigate(Routes.contactDetailRoute(contactId))
                }
            )
        }

        composable(
            route = Routes.CONTACT_DETAIL,
            arguments = listOf(
                navArgument("contactId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            val savedStateHandle = androidx.lifecycle.SavedStateHandle(
                mapOf("contactId" to contactId)
            )

            ContactDetailScreen(
                viewModel = viewModel(factory = ContactDetailViewModelFactory(
                    context,
                    savedStateHandle
                )
                ),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}