package com.example.skin_cancer_detector_android_app

import android.graphics.Bitmap
import android.os.AsyncTask
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ImageUploadTask(private val imageView: ImageView, private val predictionTextView: TextView, private val confidenceTextView: TextView) : AsyncTask<Bitmap, Void, String>() {

    override fun doInBackground(vararg bitmaps: Bitmap): String? {
        val bitmap = bitmaps[0]
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedImage = android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT)

        //URL of my IPv4 address+endpoint name of my locally running Flask server
        val url = URL("http://192.168.0.104:12000/upload_predict_images") //predtym som mal URL(http://127.0.0.1:12000/upload_predict_images")
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
        val confidence = json.getDouble("confidence")

        predictionTextView.text = "Prediction is: $prediction"
        confidenceTextView.text = "Confidence is: $confidence"
    }
}