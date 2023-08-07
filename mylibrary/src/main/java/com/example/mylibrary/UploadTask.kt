package com.example.mylibrary

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import kotlin.math.roundToInt

abstract class UploadTask: DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:InputFile
    abstract val apkSizeFile: RegularFileProperty

    @TaskAction
    fun upload(){
        val api = TelegramApi(HttpClient(OkHttp))
        val token = Confidential.token
        val chatId = Confidential.chatId

        // Execute a thread
        runBlocking {
            apkDir.get().asFile.listFiles()
                .filter { it.name.endsWith(".apk")}
                .forEach { apkFile ->
                    // Upload file to chat in Telegram
                    api.uploadFile(apkFile, token, chatId)

                    // Send message in Telegram
                    api.sendMessage("APK size: ${apkSizeFile.get().asFile.readText()} MB", token, chatId)
                }
        }
    }
}
