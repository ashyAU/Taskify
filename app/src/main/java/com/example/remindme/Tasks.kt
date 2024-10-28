package com.example.remindme

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


val tabList = listOf(
    "List1",
    "List2",
    "List3",
    "List4",
    "List5",
    "List6"
)

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun TasksParent() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState {
        tabList.size
    }
    var isOpen by remember { mutableStateOf(false) }

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
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
        ) { index ->
            Card(modifier = Modifier.fillMaxSize().padding(20.dp)) {

            }
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(10.dp)
    )
    {
        TasksButton(onOpen = { isOpen = it })
    }
}


@Composable
fun TasksButton(
    onOpen: (Boolean) -> Unit
) {
    FilledTonalIconButton(
        onClick = {
            onOpen(true)
        },
        modifier = Modifier.size(110.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = null,
            modifier = Modifier.fillMaxSize(0.4f)
        )
    }
}
