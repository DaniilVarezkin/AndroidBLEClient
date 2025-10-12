package com.example.blescantest1.remotecontrol.presentation.device

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


@Composable
fun DeviceScreen(
    viewModel: DeviceScreenViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DeviceScreenContent(
        name = state.name ?: "Неизвестно",
        address = state.address ?: "Неизвестно",
        isConnected = state.isConnected,
        messages = state.messages,
        messageText = state.messageText,
        onMessageValueChange = viewModel::onMessageValueChange,
        onClickSendMessage = viewModel::onClickSendMessage
    )
}

@Composable
fun DeviceScreenContent(
    name: String,
    address: String,
    isConnected: Boolean,
    messages: List<MessageUiModel>,
    messageText: String,
    onMessageValueChange: (String) -> Unit,
    onClickSendMessage: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        Text("Управление устройством $name", fontSize = 20.sp)
        Text("MAC адрес: $address", fontSize = 16.sp)
        Text("Состояние: $isConnected", fontSize = 16.sp)

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            LazyColumn( //TODO Подумать над дизайном, может сделать как чат
                modifier = Modifier
                    .padding(10.dp)
                    .heightIn(max = 500.dp)
            ) {
                items(messages) { message ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            if (message.isAnswer) Arrangement.Start
                            else Arrangement.End
                    ) {
                        Card(
                            modifier = Modifier.padding(3.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                modifier = Modifier.padding(5.dp),
                                text = message.text,
                                )
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = messageText,
                onValueChange = onMessageValueChange,

                placeholder = {
                    Text("Сообщение")
                },
                trailingIcon = {
                    IconButton(
                        onClick = { onClickSendMessage(messageText) }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Отправить сообщение на устройство"
                        )
                    }
                }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DeviceScreenContentPreview() {
    DeviceScreenContent(
        name = "HMSoft",
        address = "D4:36:39:B6:34:30",
        isConnected = true,
        messages = listOf(
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),
            MessageUiModel("Привет"),
            MessageUiModel("Как ты?", true),

        ),
        messageText = "",
        onMessageValueChange = {},
        onClickSendMessage = {}
    )
}