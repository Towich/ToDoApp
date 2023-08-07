package com.example.mylibrary

import org.gradle.api.provider.Property

interface UploadPluginExtension {
    val validateTaskActive: Property<Boolean>
    val maxApkSize: Property<Double>
}