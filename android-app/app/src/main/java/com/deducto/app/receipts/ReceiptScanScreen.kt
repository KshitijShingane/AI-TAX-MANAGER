package com.deducto.app.receipts

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.livedata.observeAsState
import com.deducto.app.util.PermissionUtils
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.camera.view.PreviewView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ReceiptScanScreen(onParsed: (ParsedReceipt) -> Unit, onOpenSettings: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasPermission by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    var lastError by remember { mutableStateOf<String?>(null) }

    // Use Compose-friendly permission request flow
    val permissionState = remember { mutableStateOf(PermissionState.Unknown) }
    val launcher = rememberLauncherForActivityResult(contract = androidx.activity.result.contract.ActivityResultContracts.RequestPermission()) { granted ->
        permissionState.value = if (granted) PermissionState.Granted else PermissionState.Denied
    }

    LaunchedEffect(Unit) {
        val has = PermissionUtils.hasPermission(context as Context, Manifest.permission.CAMERA)
        permissionState.value = if (has) PermissionState.Granted else PermissionState.Unknown
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        when (permissionState.value) {
            PermissionState.Unknown -> {
                Text("Camera permission is required to scan receipts")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) { Text("Request camera permission") }
                return@Column
            }
            PermissionState.Denied -> {
                Text("Camera permission denied. Please enable camera permission in app settings.")
                return@Column
            }
            PermissionState.Granted -> {
                // proceed to camera UI
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            CameraPreviewAndroidView(modifier = Modifier.fillMaxSize(), lifecycleOwner = lifecycleOwner) { imageCapture ->
                // Capture callback
                isProcessing = true
                lastError = null
                imageCapture.takePicture(ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        processImageProxy(image, onParsed) {
                            // finished
                            isProcessing = false
                        }
                        image.close()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        lastError = exception.localizedMessage
                        isProcessing = false
                    }
                })
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { /* placeholder for manual entry */ }) { Text("Manual entry") }
            Button(onClick = { /* Nothing: capture handled via camera view tap */ }) { Text("Capture") }
        }

        if (isProcessing) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Processing receipt...")
            }
        }

        lastError?.let { Text("Error: $it") }

        // Small hint about dev dataset opt-in status
        val optIn = com.deducto.app.devdata.OptInPreferences(LocalContext.current).isOptedIn()
        Row(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(if (optIn) "Dev dataset: Opted in" else "Dev dataset: Opted out", modifier = Modifier.weight(1f))
            Button(onClick = { onOpenSettings() }) {
                Text("Settings")
            }
        }
    }
}

@Composable
private fun CameraPreviewAndroidView(modifier: Modifier = Modifier, lifecycleOwner: LifecycleOwner, onImageCaptureReady: (ImageCapture) -> Unit) {
    val context = LocalContext.current

    AndroidView(factory = { ctx ->
        val previewView = PreviewView(ctx)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
                onImageCaptureReady(imageCapture)
            } catch (e: Exception) {
                Log.e("CameraPreview", "Error binding camera use cases", e)
            }
        }, ContextCompat.getMainExecutor(ctx))

        previewView
    }, modifier = modifier)
}

private fun processImageProxy(image: ImageProxy, onParsed: (ParsedReceipt) -> Unit, onDone: () -> Unit) {
    val mediaImage = image.image
    if (mediaImage != null) {
        val rotation = image.imageInfo.rotationDegrees
        val inputImage = InputImage.fromMediaImage(mediaImage, rotation)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(inputImage)
            .addOnSuccessListener { visionText ->
                val text = visionText.text
                val parsed = ReceiptParser.parseReceiptText(text)
                onParsed(parsed)
                onDone()
            }
            .addOnFailureListener { e ->
                Log.e("ReceiptScan", "Text recognition failed", e)
                onDone()
            }
    } else {
        onDone()
    }
}
