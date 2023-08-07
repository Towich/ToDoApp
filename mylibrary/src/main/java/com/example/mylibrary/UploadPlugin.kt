package com.example.mylibrary

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class UploadPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Create an extension
        project.extensions.create("uploadExtension", UploadPluginExtension::class.java)

        val androidComponents =
            project.extensions.findByType(AndroidComponentsExtension::class.java)
                ?: throw NullPointerException("~ AndroidComponentsExtension not found!")

        // On each variant
        androidComponents.onVariants { variant ->

            val variantName = variant.name.capitalize()
            val apkFileFromVariant = variant.artifacts.get(SingleArtifact.APK)

            // Task validateApkSizeFor*
            project.tasks.register(
                "validateApkSizeFor$variantName",
                ValidateApkSizeTask::class.java
            ) {
                apkDir.set(apkFileFromVariant)
                apkSizeFile.set(project.layout.buildDirectory.file("apk-size"))
            }

            // Task uploadApkFor*
            project.tasks.register("uploadApkFor$variantName", UploadTask::class.java) {
                this.apkDir.set(apkFileFromVariant)
                this.apkSizeFile.set(project.layout.buildDirectory.file("apk-size"))

                // Dependency on validateApkSizeFor*
                dependsOn(":app:validateApkSizeFor$variantName")
            }
        }
    }
}
