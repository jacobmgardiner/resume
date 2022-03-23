package com.yoloapps.airmouse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.NavHostFragment
import com.yoloapps.airmouse.ConnectionHelper.errorMsg
import com.yoloapps.airmouse.ConnectionHelper.host
import com.yoloapps.airmouse.ConnectionHelper.hostName
import com.yoloapps.airmouse.ConnectionHelper.password
import com.yoloapps.airmouse.ConnectionHelper.port
import com.yoloapps.airmouse.ConnectionHelper.server
import com.yoloapps.airmouse.ConnectionHelper.socket
import com.yoloapps.airmouse.ConnectionHelper.status
import com.yoloapps.airmouse.ConnectionHelper.updateStatus
import com.yoloapps.airmouse.ui.theme.AirMouseTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.ServerSocket
import java.net.Socket

class MainActivity : ComponentActivity() {
    private val inputManager by lazy { InputManager.getInstance(this) }

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            AirMouseApp()
            AirMouseTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "setup") {
                        composable("setup") { Setup(navController, inputManager) }
                        composable("main") { Main(navController, inputManager) }
                    }
                }
            }
        }
    }
}