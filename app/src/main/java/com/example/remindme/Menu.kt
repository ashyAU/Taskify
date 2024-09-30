package com.example.remindme

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

@Composable
fun DropDownMenuMain(dropdownMenuOpen: Boolean, isOpen: (Boolean) -> Unit) {

    Box(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.TopEnd))
    {
        DropdownMenu(
            expanded = dropdownMenuOpen,
            onDismissRequest = { isOpen(false) },
            offset = DpOffset(x = (-20).dp, y = (100).dp)
        ) {
            dropdownMenuList.forEach {
                DropdownMenuItem(
                    text = { Text(it.text) },
                    onClick = {
                        isOpen(false)
                        // todo, add the composable for the settings, this will open a page for the setting options
                        // this just needs to be a boolean
                    },
                    leadingIcon = {
                        Icon(
                            painterResource(id = it.icon),
                            contentDescription = it.text
                        )
                    }
                )
            }
        }
    }
}

data class DropdownMenuData(
    val icon: Int,
    val text: String,
)

val dropdownMenuList = listOf(
    DropdownMenuData(R.drawable.settings, "Settings")
)

