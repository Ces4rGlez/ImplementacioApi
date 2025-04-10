package com.utng_gds0651

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "AppApiGonzalez",
    ) {
        App()
    }
}