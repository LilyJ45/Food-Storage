package com.example.foodstorage

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    viewModel: FoodViewModel,
    sessionManager: SessionManager,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var showEmailDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var input by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)
        Text("Logged in as: ${sessionManager.getCurrentEmail()}", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onLogout, modifier = Modifier.fillMaxWidth()) { Text("Logout") }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = { input = ""; showEmailDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Change Email")
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = { input = ""; showPasswordDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Change Password")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.deleteUserAccount()
                onLogout()
                Toast.makeText(context, "Account Deleted", Toast.LENGTH_SHORT).show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Delete Account")
        }
    }

    if (showEmailDialog) {
        AlertDialog(
            onDismissRequest = { showEmailDialog = false },
            title = { Text("Change Email") },
            text = { OutlinedTextField(value = input, onValueChange = { input = it }, label = { Text("New Email") }) },
            confirmButton = {
                TextButton(onClick = {
                    if (input.isNotBlank()) {
                        viewModel.changeUserEmail(input)
                        showEmailDialog = false
                        Toast.makeText(context, "Email Updated! You may need to restart the app to see changes.", Toast.LENGTH_LONG).show()
                    }
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showEmailDialog = false }) { Text("Cancel") } }
        )
    }

    if (showPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showPasswordDialog = false },
            title = { Text("Change Password") },
            text = { OutlinedTextField(value = input, onValueChange = { input = it }, label = { Text("New Password") }) },
            confirmButton = {
                TextButton(onClick = {
                    if (input.isNotBlank()) {
                        viewModel.changeUserPassword(input)
                        showPasswordDialog = false
                        Toast.makeText(context, "Password Updated", Toast.LENGTH_SHORT).show()
                    }
                }) { Text("Save") }
            },
            dismissButton = { TextButton(onClick = { showPasswordDialog = false }) { Text("Cancel") } }
        )
    }
}