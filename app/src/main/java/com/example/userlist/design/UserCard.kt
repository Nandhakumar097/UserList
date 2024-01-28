package com.example.userlist.design

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.userlist.UserEvent
import com.example.userlist.model.UserData
import com.example.userlist.model.UserState
import com.example.userlist.viewmodel.MyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(
        state: UserState,
        modifier: Modifier,
        event: (event: UserEvent) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Male","Female")
    var textfieldSize by remember { mutableStateOf(Size.Zero)}

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            event(UserEvent.hideDialog)
        },
        title = { Text(text = "Add User",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary) },
        text = {
            Column(modifier = modifier
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {

                OutlinedTextField(
                    value = state.userName,
                    singleLine = true,

                    onValueChange = { event(UserEvent.setUserName(it)) },
                    label = { Text("UserName",
                        color = MaterialTheme.colorScheme.secondary) }
                )
                Spacer(modifier = modifier.height(10.dp))
                OutlinedTextField(
                    value = state.email,
                    singleLine = true,
                    onValueChange = { event(UserEvent.setEmail(it)) },
                    label = { Text("Email Id",
                        color = MaterialTheme.colorScheme.secondary) }
                )
                Column {
                    Spacer(modifier = modifier.height(10.dp))
                    OutlinedTextField(
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = true,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true, value = state.mobile,
                        onValueChange = {
                            event(UserEvent.setMobile(it.take(10)))
                        },
                        label = { Text("Mobile",
                            color = MaterialTheme.colorScheme.secondary) },
                    )
                }
                Spacer(modifier = modifier.height(10.dp))

                Box() {
                    OutlinedTextField(
                        value = state.gender,
                        onValueChange = { },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                textfieldSize = coordinates.size.toSize()
                            },
                        label = {Text("Gender",
                            color = MaterialTheme.colorScheme.secondary)},
                        trailingIcon = {
                            Icon(icon,"contentDescription",
                                Modifier.clickable { expanded = !expanded })
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(with(LocalDensity.current){textfieldSize.width.toDp()})
                    ) {
                        suggestions.forEach { label ->
                            DropdownMenuItem(
                                text = { Text(text = label,
                                    color = MaterialTheme.colorScheme.secondary) },
                                onClick = {
                                   event(UserEvent.setGender(label))
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = modifier.height(18.dp))

                Row {
                    Button(
                        onClick = {
                            event(UserEvent.saveUser)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "Submit")
                    }
                    Spacer(modifier = modifier.width(10.dp))

                    Button(
                        onClick = {
                            event(UserEvent.hideDialog)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "Cancel")
                    }
                }

            }

        },
        confirmButton = {

        }
    )


}

@Composable
fun UserList(userData: UserData) {

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = userData.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Email: ${userData.email}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Gender: ${userData.gender}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    modifier = Modifier,
                    text = "Mobile: ${userData.mobile}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

}

@Composable
fun NoData() {

    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "No Data"
        )
    }

}


