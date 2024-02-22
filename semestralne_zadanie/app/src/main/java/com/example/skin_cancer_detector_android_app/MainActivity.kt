package com.example.skin_cancer_detector_android_app


import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var imageView: ImageView
    private lateinit var predictionTextView: TextView
    private lateinit var confidenceTextView: TextView

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        predictionTextView = findViewById(R.id.predictionTextView)
        confidenceTextView = findViewById(R.id.confidenceTextView)

        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        selectImageButton.setOnClickListener {
            openGallery()
        }

        val analyseButton: Button = findViewById(R.id.analyseButton)
        analyseButton.setOnClickListener {
            val bitmap = (imageView.drawable as BitmapDrawable).bitmap
            ImageUploadTask(imageView, predictionTextView, confidenceTextView).execute(bitmap)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            val uri = data.data
            imageView.setImageURI(uri)
        }
    }
}
/*
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
    Skincancerdetectorandroid_appTheme {
        Greeting("Android")
    }
}
 */