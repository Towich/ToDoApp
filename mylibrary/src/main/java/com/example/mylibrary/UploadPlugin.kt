package com.example.mylibrary

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import java.io.File

class UploadPlugin : Plugin<Project> {
    private val validateTaskActive = true
    private val apkMaxSize = 10.0

    override fun apply(project: Project) {
        val extension = project.extensions.create("uploadExtension", UploadPluginExtension::class.java)
        extension.validateTaskActive.set(this.validateTaskActive)
        extension.maxApkSize.set(this.apkMaxSize)

        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw NullPointerException("~ AndroidComponentsExtension not found!")

        androidComponents.onVariants { variant ->

            val variantName = variant.name.capitalize()
            val apkFileFromVariant = variant.artifacts.get(SingleArtifact.APK)

            project.tasks.register(
                "validateApkSizeFor$variantName",
                ValidateApkSizeTask::class.java
            ) {
                apkDir.set(apkFileFromVariant)
                apkSizeFile.set(project.layout.buildDirectory.file("apk-size"))
            }

            project.tasks.register("uploadApkFor$variantName", UploadTask::class.java) {
                apkDir.set(apkFileFromVariant)
                apkSizeFile.set(project.layout.buildDirectory.file("apk-size"))
                dependsOn(":app:validateApkSizeFor$variantName")
            }
        }
    }
}
