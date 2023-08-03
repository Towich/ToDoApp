package com.example.mylibrary

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

abstract class UploadTask: DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @TaskAction
    fun upload(){
        val api = TelegramApi(HttpClient(OkHttp))
        val token = "6437014798:AAGAl18O165XABb4gH8ZjEHehF2FOzdYV9U"
        val chatId = "5259577325"

        runBlocking {
            apkDir.get().asFile.listFiles()
                .filter { it.name.endsWith(".apk")}
                .forEach { apkFile ->
                    api.uploadFile(apkFile, token, chatId)
                }
        }
    }
}
