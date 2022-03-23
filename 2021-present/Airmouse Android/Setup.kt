package com.yoloapps.airmouse

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yoloapps.airmouse.ui.theme.AirMouseTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private fun onConnectButton(navController: NavController?, inputManager: InputManager) {
    CoroutineScope(Dispatchers.Default).launch {
        ConnectionHelper.connect(inputManager)
    }
}

private var navigated = false

@Composable
fun Setup(navController: NavController?, inputManager: InputManager?) {
    val connected = ConnectionHelper.status.collectAsState()
    //TODO("fix")
    if (!navigated && connected.value == ConnectionHelper.ConnectionStatus.CONNECTED) {
        navigated = true
        navController?.navigate("main")
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var host by remember { mutableStateOf(TextFieldValue("")) }
        TextField(label = { Text("host IP") }, value = host, onValueChange = { host = it; ConnectionHelper.host = it.text })
        var password by remember { mutableStateOf(TextFieldValue("")) }
        TextField(label = { Text("password") }, value = password, onValueChange = { password = it; ConnectionHelper.password = it.text })
        Spacer(Modifier.height(8.dp))
        val state = ConnectionHelper.status.collectAsState()
        Text(state.value.msg)
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            if (inputManager != null) {
                onConnectButton(navController, inputManager)
            }
        }, enabled = when(state.value) {
            ConnectionHelper.ConnectionStatus.CONNECTING -> { false }
            ConnectionHelper.ConnectionStatus.INIT_SUCCESS -> { false }
            else -> true}
        ) {
            Text("Connect")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SetupPreview() {
    AirMouseTheme {
        Setup(null, null)
    }
}
