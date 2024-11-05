package com.example.remindme.taskcomponents

import android.graphics.drawable.Icon
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch


@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen() {
    val sheetState: SheetState = rememberModalBottomSheetState()
    var isSheetOpen by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    var bottomSheetContent by remember { mutableStateOf(BottomSheetContent.DEFAULT) }

    TasksTabRow(
        bottomSheetContent = { bottomSheetContent = it },
        onSheetOpen = { isSheetOpen = it })

    if (isSheetOpen) {
        SlotModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                }
                isSheetOpen = false
            },
            content = {
                BottomSheetContentHeader(bottomSheetContent = bottomSheetContent,
                    onDismiss = {
                        coroutineScope.launch {
                            sheetState.hide()
                            isSheetOpen = false
                        }
                    })
            }
        )
    }

}

@Composable
fun BottomSheetContentHeader(bottomSheetContent: BottomSheetContent, onDismiss: () -> Unit) {
    when (bottomSheetContent) {
        BottomSheetContent.GROUPADD -> {
            GroupAddContent(onDismiss)
        }
        BottomSheetContent.DEFAULT -> {
        }
        BottomSheetContent.TASKSADD -> {
        }
        BottomSheetContent.GROUPRENAME -> {
        }
        BottomSheetContent.GROUPOPTIONS -> {
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TasksTabRow(
    bottomSheetContent: ((BottomSheetContent) -> Unit),
    onSheetOpen: (Boolean) -> Unit
) {
    var selectedTabIndex by remember {
        mutableIntStateOf(0)
    }
    val pagerState = rememberPagerState { tabRowMock.size }
    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        selectedTabIndex = pagerState.targetPage
    }
    // todo, remove the use of dummy data and access the db directly
    /*
        val tasksViewModel: TasksViewModel = hiltViewModel()
        val tasks by tasksViewModel.allTasks.collectAsState(initial = emptyList())
        */

    Column(modifier = Modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex
        ) {
            tabRowMock.forEachIndexed { index, groupName ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(text = groupName) }
                )
            }
            LeadingIconTab(
                selected = false,
                onClick = {
                    onSheetOpen(true)
                    bottomSheetContent(BottomSheetContent.GROUPADD)
                },
                text = { Text("New list") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New group list"
                    )
                }
            )
        }
        HorizontalPager(
            state = pagerState, modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { index ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(tabRowMock[index])
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotModalBottomSheet(
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit,
    footerContent: @Composable (() -> Unit)? = null
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        content()
    }
}

enum class BottomSheetContent {
    DEFAULT, TASKSADD, GROUPADD, GROUPRENAME, GROUPOPTIONS
}


val tabRowMock = listOf(
    "Medication",
    "Cleaning",
    "Work"
)

