package com.design.practice

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.lifecycle.LifecycleCamera
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.design.practice.jetpackCompose.TextExample
import com.design.practice.presentation.post.PostScreen
import com.design.practice.ui.theme.PracticeTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelProvider
import com.design.practice.data.model.TfLiteLandmarkClassifier
import com.design.practice.domain.model.Classification
import com.design.practice.nav.AppNavigation
import com.design.practice.presentation.chat.ChatPage
import com.design.practice.presentation.chat.ChatViewModel
import com.design.practice.presentation.home.NotificationPermissionScreen
import com.design.practice.presentation.home.RestaurantScreen
import com.design.practice.presentation.location.CurrentLocationMapScreen
import com.design.practice.presentation.location.LocationPickerScreen
import com.design.practice.presentation.post.PostViewModel
import com.design.practice.presentation.sharedPreference.DataStoreScreen
import com.design.practice.presentation.tensorflow.CameraPreview
import com.design.practice.presentation.tensorflow.LandmarkImageAnalyzer
import kotlin.collections.emptyList


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!hasCameraPermission()){

            ActivityCompat.requestPermissions(
                this,arrayOf(Manifest.permission.CAMERA),0
            )
        }
        val chatViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            PracticeTheme {

                var classifications by remember {

                    mutableStateOf(emptyList<Classification>())
                }

                val analyzer = remember {

                    LandmarkImageAnalyzer(
                        classifier = TfLiteLandmarkClassifier(
                            context = applicationContext
                        ),
                        onResults = {
                            classifications = it
                        }
                    )
                }

                val controller = remember {

                    LifecycleCameraController(applicationContext).apply {

                        setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
                        setImageAnalysisAnalyzer(
                            ContextCompat.getMainExecutor(applicationContext),
                            analyzer
                        )
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->

                    // first commit in git

                    DataStoreScreen(modifier = Modifier.padding(innerPadding))

//                    CameraPreview(controller, Modifier.fillMaxSize().padding(innerPadding))
//
//                    Column(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(innerPadding),
//
//                        horizontalAlignment = Alignment.CenterHorizontally
//
//
//                    ) {
//
//                        classifications.forEach {
//
//                            Text(
//                                text = it.name,
//                                modifier = Modifier.fillMaxWidth()
//                                    .background(MaterialTheme.colorScheme.primaryContainer)
//                                    .padding(8.dp),
//                                textAlign = TextAlign.Center,
//                                fontSize = 20.sp,
//                                color = MaterialTheme.colorScheme.primary
//                            )
//                        }
//
//                    }

                    // ChatPage(modifier = Modifier.padding(innerPadding))
                }

                //LocationPickerScreen()

                //  CurrentLocationMapScreen()

                //NotificationPermissionScreen()

                // AppNavigation()


//                    PostScreen(
//
//                        onClick = {
//
//
//                        }
//                    )


                // TextExample(modifier = Modifier.padding(innerPadding))
            }
        }
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(
        this, Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PracticeTheme {
        Greeting("Android")
    }
}