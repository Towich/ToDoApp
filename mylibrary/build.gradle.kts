plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("upload-tg-plugin") {
        id = "upload-tg-plugin"
        implementationClass = "com.example.mylibrary.UploadPlugin"
    }
}

dependencies {
    implementation(libs.agp)
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.bundles.ktor)
}