package com.example.aliu.tilepuzzle

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_puzzle.*
import java.util.*


class PuzzleActivity : AppCompatActivity() {

    var emptyTilePosition = 8
    lateinit var missingTile: Bitmap
    var hashCheck: HashMap<Bitmap, Int> = HashMap()
    lateinit var imageAdapter: ImageAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val tilesURI = intent.extras.get("tilesURI") as Uri
        val bitmap = createSquaredBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, tilesURI))
        val tiles = splitBitmap(bitmap)

        Collections.shuffle(tiles)
        tiles[emptyTilePosition].eraseColor(Color.GRAY)
        missingTile = tiles[emptyTilePosition]
        imageAdapter = ImageAdapter(this, tiles)
        imageGridView.adapter = imageAdapter

        imageGridView.setOnItemClickListener( { parent, view, position, id ->
            if (position-3 == emptyTilePosition|| position+3 == emptyTilePosition || position-1 == emptyTilePosition || position+1 == emptyTilePosition) {
                swap(position)

            }
        })
    }

    private fun correct(): Boolean {

        imageAdapter.images.forEach {
            if (imageAdapter.images.indexOf(it) != hashCheck[it]) {
                return false
            }
        }
        return true
    }

    private fun swap(position: Int) {
        var emptyTile = imageAdapter.images[emptyTilePosition]
        imageAdapter.images[emptyTilePosition] = imageAdapter.images[position]
        imageAdapter.images[position] = emptyTile

        imageAdapter.notifyDataSetChanged()
        imageGridView.invalidateViews()

        emptyTilePosition = position
        if(correct()) {
            Toast.makeText(this, "YOU WON!", Toast.LENGTH_LONG).show()
        }

    }



    private fun createSquaredBitmap(srcBmp: Bitmap): Bitmap {
        var dstBmp: Bitmap
        if (srcBmp.width >= srcBmp.height){

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    srcBmp.getWidth()/2 - srcBmp.height /2,
                    0,
                    srcBmp.height,
                    srcBmp.height
            );

        }else{

            dstBmp = Bitmap.createBitmap(
                    srcBmp,
                    0,
                    srcBmp.height /2 - srcBmp.width /2,
                    srcBmp.width,
                    srcBmp.width
            );
        }

        return dstBmp
    }

    private fun splitBitmap(picture: Bitmap): ArrayList<Bitmap> {
        val numOfTiles = 3
        val tileImages: ArrayList<Bitmap> = ArrayList()
        val length = picture.width/numOfTiles
        for (row in 0..2) {
            for (column in 0..2) {
                val bitmap = Bitmap.createBitmap(picture, length * column , length * row, length, length )
                tileImages.add(bitmap)
                hashCheck.put(bitmap, (column+row*3))
            }
        }

        return tileImages

    }


    inner class ImageAdapter(private val mContext: Context, val images: ArrayList<Bitmap>) : BaseAdapter() {


        override fun getCount(): Int {
            return images.size
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
            val imageView: ImageView?
            if (convertView == null) {
                imageView = ImageView(mContext)
                val metrics = mContext.resources.displayMetrics
                val width = metrics.widthPixels / 3
                val height = metrics.widthPixels / 3
                imageView.layoutParams = ViewGroup.LayoutParams(width, height)
                imageView.scaleType = ImageView.ScaleType.FIT_XY
            } else {
                imageView = convertView as ImageView
            }

            imageView.setImageBitmap(images[position])
            return imageView
        }
    }
}



