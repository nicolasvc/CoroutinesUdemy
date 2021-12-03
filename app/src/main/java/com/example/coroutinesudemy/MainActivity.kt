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
    //Se crea un Scope en el hilo main para que nos permita realizar actualizaciones dentro de la UI
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        coroutineScope.launch {
            //Cambiamos de Dispatches debido a que IO es necesario para hacer consumos de network y demas
            val originalDeferred = coroutineScope.async(Dispatchers.IO) {
                getOriginalBitmap()
            }
            val originalBitmap =  originalDeferred.await()
            //Cambios de dispatches nuevamente debido a que al modificar la imagen necesitamos poder de CPU y el default nos garantiza eso
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