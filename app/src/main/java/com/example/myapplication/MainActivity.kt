package com.example.myapplication

import ClientSocket
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val STATUS_PHRASE = 200
    lateinit var clientSocket: ClientSocket
    lateinit var handler: Handler
    lateinit var responseHeaders: String
    lateinit var bitmap: Bitmap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnSend.setOnClickListener { makeRequest() }


        handler = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    STATUS_PHRASE -> {
                        txtViewHeader.setText(responseHeaders)
                        image.setImageBitmap(bitmap)
                    }
                }
            }
        }

    }

    fun makeRequest() {
        val host = "allelectronics.am"
        val resourceLoc =
            "/media/image/2a/e6/85/2499473437-televizor-samsung-ue43n5510auxru_600x600.jpg"

        val thread = Thread(Runnable {
            try {
                clientSocket = ClientSocket(host, resourceLoc)
                clientSocket.makeRequest()
                responseHeaders = clientSocket.getHeaders()
                val byteArray = clientSocket.getBody()
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                handler.sendEmptyMessage(200)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })

        thread.start()
    }
}








