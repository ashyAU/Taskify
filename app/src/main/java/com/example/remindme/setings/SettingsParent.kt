package com.example.remindme.setings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingsParent() {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter)
    {
        Text(text = "Settings Placeholder")
    }
}