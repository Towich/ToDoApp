package com.example.mylibrary

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import kotlinx.coroutines.runBlocking
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.util.*

abstract class ValidateApkSizeTask : DefaultTask() {

    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:OutputFile
    abstract val apkSizeFile: RegularFileProperty

    @TaskAction
    fun upload() {
        val api = TelegramApi(HttpClient(OkHttp))
        val token = Confidential.telegramToken
        val chatId = Confidential.telegramChatId

        val extension = project.extensions.getByType(UploadPluginExtension::class.java)
            ?: throw NullPointerException("UploadPluginExtension not found")
        val validateTaskActive = extension.validateTaskActive.get()
        val maxSize = extension.maxApkSize.get() // MB


        runBlocking {
            apkDir.get().asFile.listFiles()
                .filter { it.name.endsWith(".apk") }
                .forEach { apkFile ->
                    val apkSize = "%.2f".format(Locale.ENGLISH, apkFile.length() / 1048576.0) // 1024*1024
                        .toDouble()

                    // If apk size is more than max size
                    if (validateTaskActive && apkSize > maxSize) {
                        val message = "APK file is more than $maxSize MB! \n(apkSize = $apkSize)"

                        api.sendMessage(message, token, chatId)         // send message in Telegram
                        throw java.lang.IllegalStateException(message)  // fail task
                    }

                    // Put apk size in output file
                    apkSizeFile.get().asFile.writeText(apkSize.toString())
                }
        }
    }
}