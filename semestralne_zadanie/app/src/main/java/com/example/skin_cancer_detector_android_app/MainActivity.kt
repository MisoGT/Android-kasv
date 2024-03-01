package com.example.skin_cancer_detector_android_app


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : Activity() { //AppCompatActivity()

    private lateinit var selectImageButton: Button
    private lateinit var analyseButton: Button
    private lateinit var selectedImageView: ImageView
    private lateinit var responseTextView: TextView

    private var selectedImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectImageButton = findViewById(R.id.selectImageButton)
        analyseButton = findViewById(R.id.analyseButton)
        selectedImageView = findViewById(R.id.selectedImageView)
        responseTextView = findViewById(R.id.responseTextView)

        selectImageButton.setOnClickListener {
            openGalleryForImage()
        }

        analyseButton.setOnClickListener {
            selectedImage?.let { file ->
                CoroutineScope(Dispatchers.IO).launch {
                    val response = sendImageForAnalysis(file)
                    withContext(Dispatchers.Main) {
                        responseTextView.text = response
                    }
                }
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                selectedImageView.setImageBitmap(bitmap)
                selectedImage = createFileFromBitmap(bitmap)
            }
        }
    }

    private fun createFileFromBitmap(bitmap: Bitmap): File {
        val file = File(cacheDir, "temp_image.jpg")
        file.createNewFile()
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
        outputStream.flush()
        outputStream.close()
        return file
    }

    private suspend fun sendImageForAnalysis(imageFile: File): String {
        val client = OkHttpClient()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "image",
                "skin_mole_image.jpg",
                imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            )
            .build()

        val request = Request.Builder()
            .url("http://192.168.0.100:12000/upload_predict_images")
            .post(requestBody)
            .build()

        return try {
            val response = client.newCall(request).execute()
            response.body?.string() ?: "Empty response"
        } catch (e: Exception) {
            Log.e(TAG, "Error sending request: ${e.message}")
            "Error: ${e.message}"
        }
    }

    companion object {
        private const val REQUEST_CODE_IMAGE_PICK = 100
        private const val TAG = "MainActivity"
    }
}
/*
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

    class ImageUploadTask(private val imageView: ImageView, private val predictionTextView: TextView, private val confidenceTextView: TextView) : AsyncTask<Bitmap, Void, String>() {

        override fun doInBackground(vararg bitmaps: Bitmap): String? {
            val bitmap = bitmaps[0]
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encodedImage = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)

            //URL of my IPv4 address+endpoint name of my locally running Flask server
            val url = URL("http://192.168.0.100:12000/upload_predict_images") //predtym som mal URL(http://127.0.0.1:12000/upload_predict_images")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true

            val outputStream = connection.outputStream
            outputStream.write(encodedImage.toByteArray(Charsets.UTF_8))
            outputStream.flush()

            val inputStream: InputStream = connection.inputStream
            return inputStream.bufferedReader().use { it.readText() }
        }

        override fun onPostExecute(response_data: String) { //(result: String)
            //super.onPostExecute(result)
            super.onPostExecute(response_data)

            //val json = JSONObject(result)
            val json = JSONObject(response_data)
            val prediction = json.getString("prediction")
            val confidence = json.getDouble("confidence") //json.getDouble("confidence")

            predictionTextView.text = "Prediction is: $prediction"
            confidenceTextView.text = "Confidence is: $confidence"
        }
    }
}
*/
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