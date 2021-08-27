package com.carudibu.android.widgetloader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Intent

import android.appwidget.AppWidgetHostView
import android.os.Parcelable
import android.widget.LinearLayout
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}