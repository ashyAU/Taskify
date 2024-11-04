package com.example.remindme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import com.example.remindme.database.TasksViewModel
import kotlinx.coroutines.launch

data class StoreGroup(val id: Int = 0, val groupName: String = "")

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksParent(navBackStackEntry: NavBackStackEntry) {
    val tasksViewModel: TasksViewModel = hiltViewModel(navBackStackEntry)
    val tasks by tasksViewModel.allTasks.collectAsState(initial = emptyList())
    var selectedTask by remember {
        mutableStateOf(StoreGroup())
    }


    var bottomSheetTasks by remember {
        mutableStateOf(BottomSheetTasks.Default)
    }

    var isSheetOpen by remember {
        mutableStateOf(false)
    }

    // this is the delegate for BottomSheets
    BottomSheetDelegate(
        bottomSheetTasks = bottomSheetTasks,
        tasksViewModel = tasksViewModel,
        isSheetOpen = isSheetOpen,
        onSheetOpen = { isSheetOpen = it },
        selectedTask = selectedTask
    )

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { tasks.size })

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.targetPage) {
        selectedTabIndex = pagerState.targetPage
    }
    var currentGroup by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            tasks.forEachIndexed { index, task ->
                Tab(
                    text = {
                        Text(
                            text = task.groupName,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                )
            }
            LeadingIconTab(
                selected = false,
                onClick = {
                    isSheetOpen = true
                    bottomSheetTasks = BottomSheetTasks.AddGroup
                },
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
                .background(MaterialTheme.colorScheme.surfaceContainerLowest),
        ) { index ->
            selectedTask = StoreGroup(tasks[index].id, tasks[index].groupName)
            currentGroup = tasks[index].groupName
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {

                Card(modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceContainerLow),
                            verticalArrangement = Arrangement.Top
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tasks[index].groupName,
                                    style = MaterialTheme.typography.titleLarge,
                                )

                                IconButton(onClick = {
                                    isSheetOpen = true
                                    bottomSheetTasks = BottomSheetTasks.ConfigureGroup
                                },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.MoreVert,
                                            contentDescription = "List Options"
                                        )
                                    }
                                )
                            }
                            tasksList.forEachIndexed { _, item ->

                                var isComplete by rememberSaveable {
                                    mutableStateOf(false)
                                }

                                AnimatedVisibility(
                                    visible = !isComplete,
                                    exit = shrinkVertically(animationSpec = tween(600))
                                ) {

                                    ListItem(
                                        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
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
                    }
                )
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 0.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLowest),
                    content = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.surfaceContainerLow)
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
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                }
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                placeholder = {
                    Text(
                        text = "New Task",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                maxLines = 3,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                )
            )

            if (descriptionOpen) {
                TextField(
                    textStyle = MaterialTheme.typography.bodyMedium,
                    value = descriptionText,
                    onValueChange = { descriptionText = it },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                    ),

                    placeholder = {
                        Text(
                            "Description",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,

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
                    IconButton(onClick = {
                        descriptionOpen = !descriptionOpen

                    }, content = {
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGroup(
    isSheetOpen: Boolean,
    onSheetOpen: (Boolean) -> Unit,
    tasksViewModel: TasksViewModel
) {
    var text by rememberSaveable { mutableStateOf("") }
    val focusRequester by remember {
        mutableStateOf(FocusRequester())
    }
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                onSheetOpen(false)
                text = ""
            },
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            content = {
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
                Column(Modifier.fillMaxWidth()) {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        placeholder = {
                            Text(
                                text = "New List",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        maxLines = 3,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd)
                    {
                        TextButton(
                            content = {
                                Text(
                                    text = "Add",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            onClick = {
                                tasksViewModel.addTaskGroup(groupName = text)
                                onSheetOpen(false)
                            }
                        )
                    }
                }
            })
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ConfigureGroup(
    isSheetOpen: Boolean,
    onSheetOpen: (Boolean) -> Unit,
    tasksViewModel: TasksViewModel,
    selectedTask: StoreGroup
) {
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { onSheetOpen(false) },
            content = {
                ListItem(
                    headlineContent = { Text(text = "Rename List") }, modifier =
                    Modifier.clickable(
                        onClick = {

                        })
                )
                ListItem(
                    headlineContent = { Text(text = "Delete List") },
                    modifier = Modifier.clickable(onClick = {
                        tasksViewModel.deleteTaskGroup(selectedTask.groupName)
                        onSheetOpen(false)
                    })
                )
            },
        )

    }
}

val tasksList = mutableListOf(
    "Do the dishes",
    "Make the bed",
    "Go for a run"
)


enum class BottomSheetTasks {
    AddGroup,
    AddTasks,
    ConfigureGroup,
    Default
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDelegate(
    bottomSheetTasks: BottomSheetTasks,
    tasksViewModel: TasksViewModel,
    isSheetOpen: Boolean,
    onSheetOpen: (Boolean) -> Unit,
    selectedTask: StoreGroup
) {
    val sheetState = rememberModalBottomSheetState()


    when (bottomSheetTasks) {
        BottomSheetTasks.AddTasks -> {
            AddTask(sheetState = sheetState)
        }

        BottomSheetTasks.AddGroup -> {
            AddGroup(
                isSheetOpen = isSheetOpen,
                onSheetOpen = onSheetOpen,
                tasksViewModel = tasksViewModel
            )
        }

        BottomSheetTasks.ConfigureGroup -> {
            ConfigureGroup(
                isSheetOpen = isSheetOpen,
                onSheetOpen = onSheetOpen,
                tasksViewModel = tasksViewModel,
                selectedTask = selectedTask
            )
        }

        BottomSheetTasks.Default -> {}
    }
}


