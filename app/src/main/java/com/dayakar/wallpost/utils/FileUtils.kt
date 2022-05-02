package com.dayakar.wallpost.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import com.dayakar.wallpost.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @Created By DAYAKAR GOUD BANDARI on 02-05-2022.
 */


object FileUtils {


     fun openSettingDetailsScreen(context: Context){
        try {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${context.packageName}")).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
            }
        }catch (e:Exception){
            e.printStackTrace()
        }

    }
    suspend fun saveImageToStorage(context: Context, bitmap: Bitmap):Boolean = withContext(Dispatchers.IO) {
            val format= Bitmap.CompressFormat.PNG
            val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val dirDest = File(Environment.DIRECTORY_PICTURES, context.getString(R.string.app_name))
            val date = System.currentTimeMillis()
            val extension = format.name

            val newImage = ContentValues().apply {
                //  put(MediaStore.Images.Media.DISPLAY_NAME, "$date.$extension")
                put(MediaStore.Images.Media.DISPLAY_NAME, "$date")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/$extension")
                put(MediaStore.MediaColumns.DATE_ADDED, date)
                put(MediaStore.MediaColumns.DATE_MODIFIED, date)
                put(MediaStore.MediaColumns.SIZE, bitmap.byteCount)
                put(MediaStore.MediaColumns.WIDTH, bitmap.width)
                put(MediaStore.MediaColumns.HEIGHT, bitmap.height)
                put(MediaStore.MediaColumns.RELATIVE_PATH, "$dirDest${File.separator}")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }

            val newImageUri = context.contentResolver.insert(collection, newImage)

            context.contentResolver.openOutputStream(newImageUri!!, "w").use {
                bitmap.compress(format, 100, it)
            }
            newImage.clear()
            newImage.put(MediaStore.Images.Media.IS_PENDING, 0)
            val result= context.contentResolver.update(newImageUri, newImage, null, null)
            return@withContext result>=1
        }



}