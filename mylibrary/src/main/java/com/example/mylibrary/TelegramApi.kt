package com.example.mylibrary

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import java.io.File

class TelegramApi(
    private val httpClient: HttpClient
) {

    suspend fun uploadFile(file: File, token: String, chatId: String){
        val response = httpClient.post("https://api.telegram.org/bot$token/sendDocument") {
            parameter("chat_id", chatId)
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("document", file.readBytes(), Headers.build {
                            append(HttpHeaders.ContentDisposition,
                                "${ContentDisposition.Parameters.FileName}=\"${file.name}\"")
                        })
                    }
                )
            )
        }
        println("CODE = ${response.status.value}")
    }
}