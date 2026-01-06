package com.deducto.app.receipts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ManualEditScreen(parsed: ParsedReceipt, onSaved: (String) -> Unit, onCancel: () -> Unit) {
    var merchant by remember { mutableStateOf(parsed.merchant) }
    var date by remember { mutableStateOf(parsed.date) }
    var amount by remember { mutableStateOf(parsed.amount.toString()) }
    var currency by remember { mutableStateOf(parsed.currency) }
    var saving by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Review parsed receipt")
        OutlinedTextField(value = merchant, onValueChange = { merchant = it }, label = { Text("Merchant") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Date") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))
        OutlinedTextField(value = currency, onValueChange = { currency = it }, label = { Text("Currency") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp))

        Button(onClick = {
            saving = true
            message = "Saving..."
            val receipt = Receipt(merchant = merchant, date = date, amount = amount.toDoubleOrNull() ?: 0.0, currency = currency, rawText = parsed.rawText)
            CoroutineScope(Dispatchers.IO).launch {
                val repo = ReceiptRepository()
                val id = repo.addReceipt(receipt)
                CoroutineScope(Dispatchers.Main).launch {
                    saving = false
                    message = "Saved"
                    onSaved(id)
                }
            }
        }, modifier = Modifier.padding(top = 12.dp)) {
            Text(if (saving) "Saving..." else "Save")
        }

        Button(onClick = onCancel, modifier = Modifier.padding(top = 8.dp)) {
            Text("Cancel")
        }

        if (message.isNotEmpty()) {
            Text(message, modifier = Modifier.padding(top = 8.dp))
        }
    }
}
