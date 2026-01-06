package com.deducto.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deducto.app.firebase.FirebaseModule
import com.google.firebase.auth.FirebaseAuth
import com.deducto.app.receipts.ReceiptScanScreen
import com.deducto.app.receipts.ManualEditScreen
import com.deducto.app.receipts.ParsedReceipt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase
        FirebaseModule.init(this)

        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null) {
                    SignInScreen(onSignedIn = { /* Recompose handled by FirebaseAuth state */ })
                } else {
                    // Show home with scan flow and Settings
                    var parsedReceipt by remember { mutableStateOf<ParsedReceipt?>(null) }
                    var showSettings by remember { mutableStateOf(false) }

                    if (showSettings) {
                        com.deducto.app.settings.SettingsScreen(context = this@MainActivity) { showSettings = false }
                    } else {
                        if (parsedReceipt == null) {
                            Column {
                                ReceiptScanScreen(onParsed = { parsedReceipt = it }, onOpenSettings = { showSettings = true })
                            }
                        } else {
                            ManualEditScreen(parsed = parsedReceipt!!, onSaved = { id ->
                                // After save, go back to scanner
                                parsedReceipt = null
                            }, onCancel = {
                                parsedReceipt = null
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SignInScreen(onSignedIn: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Sign in to Deducto", style = androidx.compose.material.MaterialTheme.typography.h6)
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.padding(top = 8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.padding(top = 8.dp))
        Button(onClick = {
            message = "Signing in..."
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        message = "Signed in"
                        onSignedIn()
                    } else {
                        // Try to create user automatically for easy testing
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { createTask ->
                                if (createTask.isSuccessful) {
                                    message = "Account created and signed in"
                                    onSignedIn()
                                } else {
                                    message = createTask.exception?.localizedMessage ?: "Sign-in failed"
                                }
                            }
                    }
                }
        }, modifier = Modifier.padding(top = 12.dp)) {
            Text("Sign in")
        }
        if (message.isNotEmpty()) {
            Text(message, modifier = Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
fun HomeScreen(email: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Welcome, $email")
        Button(onClick = { FirebaseAuth.getInstance().signOut() }, modifier = Modifier.padding(top = 12.dp)) {
            Text("Sign out")
        }
    }
}
