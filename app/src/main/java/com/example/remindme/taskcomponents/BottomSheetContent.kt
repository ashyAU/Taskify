package com.example.remindme.taskcomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.remindme.database.TasksViewModel


@Composable
fun GroupAddContent(onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }
    val tasksViewModel: TasksViewModel

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "New group") }

        )
        Box(modifier = Modifier.fillMaxWidth().padding(10.dp), contentAlignment = Alignment.CenterEnd)
        {
            TextButton(
                onClick = {
                    // todo integrate task view model
                    onDismiss()

                },
            ) {
                Text(text = "Add")
            }
        }
    }
}