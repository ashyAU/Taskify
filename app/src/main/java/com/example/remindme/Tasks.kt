package com.example.remindme

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Preview
@Composable
fun TasksParent() {
    var buttonClicked by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
    {
        Button(onClick = { buttonClicked = true }) {
            Text("Click Me!")
        }
    }
    if (buttonClicked) {
        TasksModalBottomSheet()
    }
}

@Composable
fun TasksModalBottomSheet() {
    CustomModalBottomSheet(
        body = { AddTaskBody(supportingText = "New Task") },
        footer = { hideSheet ->
            AddTaskFooter(text = "Add", hideSheet = hideSheet )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModalBottomSheet(
    header: @Composable (() -> Unit)? = null,
    body: @Composable (() -> Unit)? = null,
    footer: @Composable ((hideModalBottomSheet: () -> Unit) -> Unit)? = null
) {
    val modelBottomSheetState = rememberModalBottomSheetState()

    val coroutineScope = rememberCoroutineScope()
    val hideModalBottomSheet: () -> Unit =
        { coroutineScope.launch { modelBottomSheetState.hide() } }

    ModalBottomSheet(
        sheetState = modelBottomSheetState,
        onDismissRequest = {},
    )
    {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            header?.let {
                it()
            }
            body?.let {
                it()
            }
            footer?.let {
                it(hideModalBottomSheet)
            }
        }
    }
}
@Composable
fun AddTaskFooter(text: String, hideSheet: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp), contentAlignment = Alignment.CenterEnd
    ) {
        TextButton(
            onClick = hideSheet
        ) {
            Text(text = text)
        }
    }
}

@Composable
fun AddTaskHeader(text: String) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun AddTaskBody(supportingText: String) {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = supportingText) }
        )

    }
}





