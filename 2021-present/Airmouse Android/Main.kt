package com.yoloapps.airmouse

import android.view.MotionEvent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yoloapps.airmouse.ui.theme.AirMouseTheme

private var navigated = false

@ExperimentalComposeUiApi
@Composable
fun Main(navController: NavController?, inputManager: InputManager?) {
    //TODO("fix")
    val serverState = ConnectionHelper.server?.status?.collectAsState(ServerHelper.ServerStatus.ERROR)
    if (!navigated && serverState?.value == ServerHelper.ServerStatus.DISCONNECTED) {
        navigated = true
        navController?.navigate("setup")
    }
    Column(
        Modifier
            .verticalScroll(enabled = true, state = ScrollState(0))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Spacer(Modifier.height(8.dp))
        Text("Sensitivity")
        var sensitivity by remember { mutableStateOf(0.5f) }
        Slider(value = sensitivity, onValueChange = { sensitivity = it;inputManager?.sensitivity = it * 7 })
        Spacer(Modifier.height(8.dp))

        Spacer(Modifier.height(8.dp))
        Text("Input Rate")
        var rate by remember { mutableStateOf(0.5f) }
        Slider(value = rate, onValueChange = { rate = it;ConnectionHelper.server?.inputRate = ServerHelper.MIN_INPUT_RATE + it * (ServerHelper.MAX_INPUT_RATE - ServerHelper.MIN_INPUT_RATE) })
        Spacer(Modifier.height(8.dp))

        Spacer(Modifier.height(8.dp))
        Text("Volume")
        var volume by remember { mutableStateOf(inputManager?.initialVolume) }
        Slider(value = volume ?: 0.5f, onValueChange = { volume = it }, onValueChangeFinished = { inputManager?.changeVolume(volume ?: 0.5f) })
        Spacer(Modifier.height(8.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            var color0 by remember { mutableStateOf(Color.Gray) }
            Surface(
                color = color0,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
//                    .fillMaxHeight(0.25f)
                    .padding(2.dp)
                    .weight(1f)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                color0 = Color.DarkGray
                                inputManager?.button0 = 1
                                return@pointerInteropFilter true
                            }
                            MotionEvent.ACTION_UP -> {
                                color0 = Color.Gray
                                inputManager?.button0 = 0
                                return@pointerInteropFilter true
                            }
                            else -> false
                        }
                    }
            ) {
                Text("left", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }
            var color1 by remember { mutableStateOf(Color.Gray) }
            Surface(
                color = color1,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
//                    .fillMaxHeight(0.25f)
                    .padding(2.dp)
                    .weight(0.5f)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                color1 = Color.DarkGray
                                inputManager?.button1 = 1
                                return@pointerInteropFilter true
                            }
                            MotionEvent.ACTION_UP -> {
                                color1 = Color.Gray
                                inputManager?.button1 = 0
                                return@pointerInteropFilter true
                            }
                            else -> false
                        }
                    }
            ) {
                Text("middle", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }
            var color2 by remember { mutableStateOf(Color.Gray) }
            Surface(
                color = color2,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
//                    .fillMaxHeight(0.25f)
                    .padding(2.dp)
                    .weight(1f)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                color2 = Color.DarkGray
                                inputManager?.button2 = 1
                                return@pointerInteropFilter true
                            }
                            MotionEvent.ACTION_UP -> {
                                color2 = Color.Gray
                                inputManager?.button2 = 0
                                return@pointerInteropFilter true
                            }
                            else -> false
                        }
                    }
            ) {
                Text("right", modifier = Modifier.fillMaxSize(), textAlign = TextAlign.Center)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { inputManager?.offset() }) {
            Text("Recenter")
        }
        Spacer(modifier = Modifier.height(16.dp))
        //TODO("horizontal scrollview with more buttons")
        Row() {
            Button(onClick = { inputManager?.backspace() }) {
                Text("Backspace")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = { inputManager?.enter() }) {
                Text("Enter")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row() {
            var text by remember { mutableStateOf(TextFieldValue("")) }
            TextField(placeholder = { Text("Tap here to type on the host") }, value = text, onValueChange = {
                inputManager?.appendString(it.text)
            })
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text("Quick Launch Apps")
        inputManager?.appList?.forEachIndexed { idx, app ->
            Spacer(Modifier.height(8.dp))
            Button(onClick = { inputManager.launchApp(idx) }) {
                Text(app)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { ConnectionHelper.server?.disconnect() }) {
            Text("Disconnect")
        }
        Spacer(Modifier.height(16.dp))
    }
}

@ExperimentalComposeUiApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AirMouseTheme {
        Main(null, null)
    }
}