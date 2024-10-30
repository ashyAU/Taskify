package com.example.remindme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch


val tabList = listOf(
    "List1",
    "List2",
    "List3",
    "List4",
    "List5",
    "List6"
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TasksParent() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState {
        tabList.size
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.targetPage) {
        selectedTabIndex = pagerState.targetPage
    }

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(selectedTabIndex = selectedTabIndex) {

            tabList.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                )
            }
            LeadingIconTab(
                selected = false,
                onClick = { /* todo add a custom page to add a new item to the list */ },
                text = { Text("New List") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add item to List"
                    )
                })
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) { index ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                val sheetState = rememberModalBottomSheetState()
                var isSheetOpen by rememberSaveable {
                    mutableStateOf(false)
                }

                if (isSheetOpen) {
                    ModalBottomSheet(
                        onDismissRequest = { isSheetOpen = false },
                        content = {
                            ListItem(
                                headlineContent = { Text(text = "Rename List") }, modifier =
                                Modifier.clickable(
                                    onClick = {

                                    })
                            )
                            ListItem(
                                headlineContent = { Text(text = "Delete List") },
                                modifier = Modifier.clickable { })
                        },
                        sheetState = sheetState
                    )
                }

                Card(modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth()
                    .padding(10.dp),
                    content = {
                        Column(
                            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tabList[index],
                                    style = MaterialTheme.typography.titleLarge,
                                )
                                IconButton(onClick = { isSheetOpen = true },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "List Options"
                                        )
                                    }
                                )
                            }
                            tasksList.forEachIndexed { index, item ->
                                var isComplete by rememberSaveable {
                                    mutableStateOf(false)
                                }
                                ListItem(
                                    colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                    // todo i am currently using dummy data
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // todo
                                        },
                                    leadingContent = {
                                        IconButton(onClick = { isComplete = true },
                                            content = {
                                                Icon(
                                                    painterResource(id = if (!isComplete) R.drawable.radio_unchecked else R.drawable.check_circle),
                                                    contentDescription = "Task completion check"
                                                )
                                            }
                                        )
                                    },

                                    headlineContent = {
                                        Text(
                                            text = item,
                                            style = MaterialTheme.typography.bodyMedium,
                                            textDecoration = if (isComplete) TextDecoration.LineThrough else TextDecoration.None
                                        )
                                    },
                                    supportingContent = {
                                        Text(
                                            text = "Supporting Text",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                )
                            }
                        }
                    }
                )
                Card(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(10.dp),
                    content = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        )
                        {
                            Text(
                                text = "Completed",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTask(sheetState: SheetState) {
    var text by rememberSaveable {
        mutableStateOf("")
    }
    val focusRequester by remember {
        mutableStateOf(FocusRequester())
    }
    var descriptionOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var descriptionText by rememberSaveable {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(!sheetState.isVisible) {
        text = ""
        descriptionText = ""
        descriptionOpen = false
    }

    if (sheetState.isVisible) {
        ModalBottomSheet(onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }
        }) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = { Text("New Task") },
                maxLines = 3,
            )
            if (descriptionOpen) {
                TextField(
                    textStyle = MaterialTheme.typography.bodyMedium,
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            "Description",
                            style = MaterialTheme.typography.bodyMedium,
                            // todo figure out the correct ime padding values to get rid of the bug
                            modifier = Modifier.imePadding()
                        )
                    }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Row {
                    IconButton(onClick = { descriptionOpen = !descriptionOpen }, content = {
                        Icon(
                            painterResource(id = if (descriptionOpen) R.drawable.description_filled else R.drawable.description),
                            contentDescription = "Opens a text field for a description"
                        )
                    })
                    IconButton(
                        onClick = {/*todo */ },
                        content = {
                            Icon(
                                painterResource(id = R.drawable.schedule),
                                contentDescription = "Schedule Button"
                            )
                        })
                }
                Text(
                    text = "Save",
                    modifier = Modifier.clickable(enabled = text.isNotEmpty())
                    {
                        scope.launch {
                            if (sheetState.isVisible) {
                                sheetState.hide()
                            }
                        }

                        /* todo add task to the database. */
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

val tasksList = mutableListOf(
    "Do the dishes",
    "Make the bed",
    "Go for a run"
)



