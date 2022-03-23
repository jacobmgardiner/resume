//package com.yoloapps.airmouse
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.MotionEvent
//import android.view.View
//import android.view.ViewGroup
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInteropFilter
//import androidx.compose.ui.platform.ComposeView
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.navigation.fragment.NavHostFragment
//import com.yoloapps.airmouse.ConnectionHelper.server
//import com.yoloapps.airmouse.ui.theme.AirMouseTheme
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//
//class MainFragment : NavHostFragment() {
//
//    companion object {
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            MainFragment().apply {
//                arguments = Bundle().apply {
//                }
//            }
//    }
//
//    val inputManager by lazy { InputManager.getInstance(requireContext()) }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        CoroutineScope(Dispatchers.Default).launch {
//            server.status.collect {
//                when(it) {
//                    ServerHelper.ServerStatus.RUNNING -> {  }
//                    ServerHelper.ServerStatus.DISCONNECTING -> {  }
//                    ServerHelper.ServerStatus.DISCONNECTED -> {
//                        navigate
//                    }
//                }
//            }
//        }
//    }
//
//    @ExperimentalComposeUiApi
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
////        startConnection()
//        return ComposeView(requireContext()).apply {
//            setContent {
//                AirMouseTheme {
//                    Surface(color = MaterialTheme.colors.background) {
//                        Main(inputManager)
//                    }
//                }
//            }
//        }
//    }
//}
//
