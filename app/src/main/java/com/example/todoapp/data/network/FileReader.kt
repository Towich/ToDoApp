package com.example.todoapp.data.network

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.example.todoapp.App
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class FileReader @Inject constructor(private var context: Context) {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = (context as App).assets.open(fileName)
            val builder = StringBuilder()
            val reader = InputStreamReader(inputStream, "UTF-8")
            reader.readLines().forEach {
                builder.append(it)
            }
            return builder.toString()
        } catch (e: IOException) {
            throw e
        }
    }
}