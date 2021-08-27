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
    lateinit var mAppWidgetManager: AppWidgetManager
    lateinit var mAppWidgetHost: AppWidgetHost

    var REQUEST_PICK_APPWIDGET = 1
    var REQUEST_CREATE_APPWIDGET = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAppWidgetManager = AppWidgetManager.getInstance(this)
        mAppWidgetHost = AppWidgetHost(this, 1)

        selectWidget()
    }

    fun selectWidget() {
        val appWidgetId: Int = this.mAppWidgetHost.allocateAppWidgetId()
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        addEmptyData(pickIntent)
        startActivityForResult(pickIntent, 1)
    }

    fun addEmptyData(pickIntent: Intent) {
        val customInfo = java.util.ArrayList<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
        val customExtras = java.util.ArrayList<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_APPWIDGET) {
                configureWidget(data)
            } else if (requestCode == REQUEST_CREATE_APPWIDGET) {
                if (data != null) {
                    createWidget(data)
                }
            }
        } else if (resultCode == RESULT_CANCELED && data != null) {
            val appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
            if (appWidgetId != -1) {
                mAppWidgetHost.deleteAppWidgetId(appWidgetId)
            }
        }
    }

    private fun configureWidget(data: Intent?) {
        val extras = data!!.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId)
        if (appWidgetInfo.configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.component = appWidgetInfo.configure
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET)
        } else {
            createWidget(data)
        }
    }

    fun createWidget(data: Intent) {
        val extras = data.extras
        val appWidgetId = extras!!.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId)
        val hostView = mAppWidgetHost.createView(this, appWidgetId, appWidgetInfo)
        hostView.setAppWidget(appWidgetId, appWidgetInfo)
        findViewById<LinearLayout>(R.id.ll).addView(hostView)
    }

    fun loadBitmapFromView(v: View): Bitmap? {
        val b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.draw(c)
        return b
    }

    override fun onStart() {
        super.onStart()
        mAppWidgetHost.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAppWidgetHost.stopListening()
    }

    fun removeWidget(hostView: AppWidgetHostView) {
        mAppWidgetHost.deleteAppWidgetId(hostView.appWidgetId)
        findViewById<LinearLayout>(R.id.ll).removeView(hostView)
    }
}