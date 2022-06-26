package de.telekom.it.mad.myapplication3.data

import android.graphics.Bitmap

sealed interface AppData

class TextAppData(val id: Int) : AppData {
    var string: String = ""
}

class ImageAppData(val url: String): AppData {
    var bitmap: Bitmap? = null
    var error: String = ""
}