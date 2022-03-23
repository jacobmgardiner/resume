package com.yoloapps.pitchtrainer.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.yoloapps.pitchtrainer.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    var presentationResId = R.drawable.x

    val viewModel: MainViewModel by viewModels()

//    lateinit var resultSound: AudioController
    lateinit var presentationSound: AudioController

//    @OptIn(InternalCoroutinesApi::class)
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        println("XXX VERSION: 13")

        super.onCreate(savedInstanceState)

//        lifecycleScope.launch {
            println("XXX FIRST")
            viewModel.controller.observeNote { value ->
                println("XXX [MAIN ACTIVITY] NEW NOTE: $value")
                presentationResId = R.drawable.unknown_presentation
                presentationSound = AudioController(
                    this@MainActivity,
                    ResourceIdMapper.audioFile(value),
                    object : AudioManager.AudioEventListener {
                        override fun onStart() {
                        }

                        override fun onPause() {
                        }

                        override fun onCancel() {
                        }

                        override fun onComplete() {
                            println("XXX [MAIN ACTIVITY] FINISHED NOTE SOUND")
                            viewModel.controller.onNotePresentationComplete()
                        }

                        override fun onEnd() {
                        }
                    })
            }
//        }

//        lifecycleScope.launch {
            println("XXX SECOND")
            viewModel.controller.observeSucceeded { value ->
                println("XXX [MAIN ACTIVITY] RESULT: $value")
                var audioId = R.raw.failed
                if (value) {
                    audioId = R.raw.succeeded
                    presentationResId = R.drawable.check
                } else {
                    presentationResId = R.drawable.x
                }
                presentationSound = AudioController(
                    this@MainActivity,
                    audioId,
                    object : AudioManager.AudioEventListener {
                        override fun onStart() {
                        }

                        override fun onPause() {
                        }

                        override fun onCancel() {
                        }

                        override fun onComplete() {
                            println("XXX [MAIN ACTIVITY] FINISHED RESULT SOUND")
                            viewModel.controller.onResultComplete()
                        }

                        override fun onEnd() {
                        }

                    })
//            }

//            viewModel.controller.note.collect(object: FlowCollector<NoteUtils.NoteLetter> {
//                override suspend fun emit(value: NoteUtils.NoteLetter) {
//                    println("XXX [MAIN ACTIVITY] NEW NOTE: $value")
//                    presentationResId = R.drawable.unknown_presentation
//                    presentationSound = AudioController(
//                        this@MainActivity,
//                        ResourceIdMapper.audioFile(value),
//                        object : AudioManager.AudioEventListener {
//                            override fun onStart() {
//                            }
//                            override fun onPause() {
//                            }
//                            override fun onCancel() {
//                            }
//                            override fun onComplete() {
//                                println("XXX [MAIN ACTIVITY] FINISHED NOTE SOUND")
//                                viewModel.controller.onNotePresentationComplete()
//                            }
//                            override fun onEnd() {
//                            }
//                        })
//                }
//            })

//            viewModel.controller.succeeded.collect(object: FlowCollector<Boolean> {
//                override suspend fun emit(value: Boolean) {
//                    println("XXX [MAIN ACTIVITY] RESULT: $value")
//                    var audioId = R.raw.failed
//                    if (value) {
//                        audioId = R.raw.succeeded
//                        presentationResId = R.drawable.check
//                    } else {
//                        presentationResId = R.drawable.x
//                    }
//                    presentationSound = AudioController(
//                        this@MainActivity,
//                        audioId,
//                        object : AudioManager.AudioEventListener {
//                            override fun onStart() {
//                            }
//                            override fun onPause() {
//                            }
//                            override fun onCancel() {
//                            }
//                            override fun onComplete() {
//                                println("XXX [MAIN ACTIVITY] FINISHED RESULT SOUND")
//                                viewModel.controller.onResultComplete()
//                            }
//                            override fun onEnd() {
//                            }
//
//                        })
//                }
//            })
        }

        lifecycleScope.launch {
            viewModel.controller.presentationVisible.collect {
                println("XXX [MAIN ACTIVITY][COLLECT] PRESENTATION VISIBILITY: $it")
                if(it.visible) {
                    println("XXX [MAIN ACTIVITY][COLLECT] PLAYING PRESENTATION SOUND")
                    presentationSound.play()
                }
            }
        }

        setContent {
            Main(viewModel)
        }

        println("XXX [MAIN ACTIVITY] CALLING START")
        viewModel.controller.start()
    }

    private var guess = -1
    set(value) {
        if (!viewModel.controller.inputAllowed) return
        field = value
        //TODO
        viewModel.controller.onResponse(
            value,
            AudioController(this, ResourceIdMapper.audioFile(NoteUtils.fromInt(value)), null)
        )
    }

    @ExperimentalAnimationApi
    @Composable
    fun PresentationOverlay(viewModel: MainViewModel) {
        val presentationVisible by viewModel.controller.presentationVisible.collectAsState(
            initial = PresentationVisibility(true)
        )
        println("XXX [MAIN ACTIVITY][PRESENTATION OVERLAY] PRESENTATION VISIBILITY: $presentationVisible")
//        val density = LocalDensity.current
        AnimatedVisibility(
            visible = presentationVisible.visible,
            modifier = Modifier.fillMaxSize(),
            enter = expandHorizontally(
                expandFrom = Alignment.CenterHorizontally
            ) + expandVertically(
                expandFrom = Alignment.CenterVertically
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = shrinkHorizontally() + shrinkVertically() + fadeOut()
        ) {
            Image(painter = painterResource(id = R.drawable.background), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillWidth)
            Image(painter = painterResource(id = presentationResId), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.FillHeight)
        }

//        if(presentationVisible) {
//            println("XXX [MAIN ACTIVITY][PRESENTATION OVERLAY] PLAYING PRESENTATION SOUND")
//            presentationSound.play()
//        }
    }

    @ExperimentalAnimationApi
    @Composable
    fun Main(viewModel: MainViewModel) {
        Box(
//            Modifier.fillMaxSize(),
//            Alignment.Center
        ) {
            Column(
                Modifier.fillMaxSize(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                BoxWithConstraints(
                    Modifier.fillMaxSize(),
                ) {
                    //background
                    Image(painterResource(R.drawable.sky), null, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    //buttons
                    maxHeight.times(1/9f).also {
                        Row(
                            Modifier.fillMaxSize(),
                            Arrangement.Center,
                            Alignment.Bottom
                        ) {
                            Modifier
                                .weight(1f).also { m ->
                                    for (i in 0 until 7) {
                                        ImageButton(
                                            modifier = m.offset(y = it.times(-i)),
                                            drawable = ResourceIdMapper.buttonFromInt(i),
//                                            audio = ResourceIdMapper.audioFile(NoteUtils.NoteLetter.values()[i])
                                        ) { guess = i }
                                    }
                                }
                        }
                    }
                }
            }

            PresentationOverlay(viewModel)
        }
    }

    @Composable
    fun ImageButton(modifier: Modifier, drawable: Int, onClick: () -> Unit) {
        Image(painterResource(drawable), null, modifier.clickable(true, onClick = onClick))
    }

    @ExperimentalAnimationApi
    @Preview(widthDp = 846, heightDp = 412)
    @Composable
    fun PreviewMain() {
        initPreviews()
        Main(MainViewModel())
    }

    @ExperimentalAnimationApi
    @Preview(widthDp = 846, heightDp = 412)
    @Composable
    fun PreviewPresentation() {
        initPreviews()
        PresentationOverlay(MainViewModel())
    }

    private fun initPreviews() {
        presentationResId = R.drawable.check
//        presentationSound = AudioController(this, R.raw.succeeded, null)
    }

    @Preview
    @Composable
    fun ImageButtonPreview() {
        ImageButton(Modifier, R.drawable.button_cat) {  }
    }
}
