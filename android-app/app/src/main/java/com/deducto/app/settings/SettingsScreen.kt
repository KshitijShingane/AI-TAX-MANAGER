package com.deducto.app.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.deducto.app.devdata.OptInPreferences

@Composable
fun SettingsScreen(context: Context, onClose: () -> Unit) {
    var optedIn by remember { mutableStateOf(OptInPreferences.isOptedIn(context)) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Settings")
        Spacer(modifier = Modifier.height(8.dp))

        // Dev dataset opt-in toggle
        Text("Share anonymized parsed receipts to help improve Deducto")
        Spacer(modifier = Modifier.height(4.dp))
        RowWithSwitch(checked = optedIn, onCheckedChange = {
            optedIn = it
            OptInPreferences.setOptIn(context, it)
        })

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = onClose) {
            Text("Close")
        }
    }
}

@Composable
fun RowWithSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    androidx.compose.foundation.layout.Row(modifier = Modifier.padding(top = 8.dp)) {
        Text(text = if (checked) "Opted in" else "Opted out", modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}