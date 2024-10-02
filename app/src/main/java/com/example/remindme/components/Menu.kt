package com.example.remindme.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.remindme.AppRoute
import com.example.remindme.R

@Composable
fun DropDownMenuMain(
    dropdownMenuOpen: Boolean,
    isOpen: (Boolean) -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopEnd)
    )
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
                        when (it.text) {
                            "Settings" -> {
                                navController.navigate(AppRoute.Settings.route)
                            }
                        }

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
    val index: Menu
)

val dropdownMenuList = listOf(
    DropdownMenuData(R.drawable.settings, "Settings", Menu.settings)
)

enum class Menu {
    default,
    settings
}

