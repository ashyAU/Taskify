package com.example.remindme.taskcomponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.remindme.R


@Composable
fun GroupAddContent(onDismiss: () -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "New group") }

        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            contentAlignment = Alignment.CenterEnd
        )
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


@Composable
fun GroupOptions(onDismiss: () -> Unit) {
    val listItems = listOf(
        Item(name = "Rename", contentDescription = "Rename the currently selected group"),
        Item(
            name = "Delete",
            contentDescription = "Delete the currently selected group and all its tasks"
        )
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        listItems.forEach { item ->
            ListItem(
                modifier = Modifier.clickable {
                    onDismiss()
                    //todo, add the click events for adding and deleting
                },
                headlineContent = {

                    Text(text = item.name)
                }
            )
        }
        Spacer(modifier = Modifier.padding(top = 32.dp))
    }
}

data class Item(
    val name: String,
    val contentDescription: String?,
    val icon: Int? = 0,
)



@Composable
fun TaskAddContent(
    onDismiss: () -> Unit
) {
    val icons = TaskIcons.icons

    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var descriptionOpen by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = taskName,
            onValueChange = { taskName = it },
            placeholder = { Text(text = "Task Name") }
        )
        if (descriptionOpen) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = taskDescription,
                onValueChange = { taskDescription = it },
                placeholder = { Text(text = "Description") }
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically)
        {
            icons.forEach { icon ->
                IconButton(
                    onClick = {
                        when (icon.name) {
                            "Description" -> {
                                descriptionOpen = !descriptionOpen

                            }
                        }
                    },
                    content = {
                        Icon(
                            painterResource(id = if (descriptionOpen) icon.iconFilled else icon.iconUnfilled),
                            contentDescription = icon.name
                        )
                    }
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
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
}

data class AddTaskIcons(
    val name: String,
    val iconUnfilled: Int,
    val iconFilled: Int
)
object TaskIcons {
    val icons = listOf(
        AddTaskIcons(
            "Description",
            iconUnfilled = R.drawable.description,
            iconFilled = R.drawable.description_filled
        ),
        AddTaskIcons(
            "Time",
            iconUnfilled = R.drawable.alarm,
            iconFilled = R.drawable.alarm
        )
    )
}