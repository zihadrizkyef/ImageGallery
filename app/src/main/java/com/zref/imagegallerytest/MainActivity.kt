package com.zref.imagegallerytest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.FileProvider
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.options
import com.zref.imagegallerytest.ui.theme.ImageGalleryTestTheme
import java.io.File


class MainActivity : ComponentActivity() {
    private var cameraUri : Uri? = null
    private lateinit var cropImage: ActivityResultLauncher<CropImageContractOptions>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                val uriContent = result.uriContent
                findViewById<ImageView>(R.id.imageView).setImageURI(uriContent)
            } else {
                // an error occurred
                val exception = result.error
            }
        }
        openCameraIntent()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 5) {
            cropImage.launch(options(uri=cameraUri))
        }
    }

    private fun openCameraIntent() {
        cameraUri = makeOutput()
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
        startActivityForResult(cameraIntent, 5)
    }

    private fun makeOutput(): Uri {
        val imageDir = File(filesDir, "images/")
        imageDir.mkdirs()
        val imageFile = File(imageDir, "${System.currentTimeMillis()}.jpg")
        val photoUri = FileProvider.getUriForFile(
            this,
            "com.example.android.fileprovider",
            imageFile
        )
        return photoUri
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageGalleryTestTheme {
        Greeting("Android")
    }
}