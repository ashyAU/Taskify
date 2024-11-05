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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.example.remindme.database.TasksViewModel
import kotlinx.coroutines.launch


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
        sheetState = sheetState
    ) {
        content()
        Spacer(modifier = Modifier.padding(16.dp))
        footerContent?.invoke()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen() {
    val sheetState: SheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var bottomSheetContent by remember { mutableStateOf(BottomSheetContent.DEFAULT) }

}

enum class BottomSheetContent {
    DEFAULT, TASKSADD, GROUPADD, GROUPRENAME, GROUPOPTIONS
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun TasksTabRow() {
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
    /*    val tasksViewModel: TasksViewModel = hiltViewModel()
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
                    // todo add the groupname bottomsheet here.
                },
                text = { Text("New list")},
                icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "New group list") }
            )
        }
        HorizontalPager(state = pagerState, modifier = Modifier
            .fillMaxWidth().weight(1f)){ index ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(tabRowMock[index])
            }
        }
    }
}


data class TabItem(
    val title: String
)

val tabRowMock = listOf(
    "Medication",
    "Cleaning",
    "Work"
)

