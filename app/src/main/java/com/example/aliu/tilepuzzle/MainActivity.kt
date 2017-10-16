package com.example.aliu.tilepuzzle

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.graphics.Bitmap
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.R.attr.data
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import com.example.aliu.tilepuzzle.R.layout.activity_main


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        findViewById<Button>(R.id.selectImageButton).setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "image/*"
            startActivityForResult(intent, 21)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode === 21 && resultCode === Activity.RESULT_OK) {
            if (data != null) {
                val uri = data.data
                val puzzleIntent = Intent(this, PuzzleActivity::class.java)
                puzzleIntent.putExtra("tilesURI", uri)
                startActivity(puzzleIntent)
            }
        }
    }


}