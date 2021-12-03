package com.example.coroutinesudemy

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.coroutinesudemy.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val IMAGEURL = "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png"
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        coroutineScope.launch {
            val originalDeferred = coroutineScope.async(Dispatchers.IO) {
                getOriginalBitmap()
            }
            val originalBitmap =  originalDeferred.await()
            val filterDeferred = coroutineScope.async(Dispatchers.Default){
                applyFilter(originalBitmap)
            }
           val filteredBitmap  = filterDeferred.await()
            loadImage(filteredBitmap)
        }
    }


    private fun getOriginalBitmap() =
        URL(IMAGEURL).openStream().use {
            BitmapFactory.decodeStream(it)
        }

    private fun loadImage(bmp:Bitmap){
        binding.progressBar.visibility = View.GONE
        binding.imageView.setImageBitmap(bmp)
        binding.imageView.visibility = View.VISIBLE
    }

    private fun applyFilter(originalBitmap: Bitmap) = Filter.apply(originalBitmap)

}