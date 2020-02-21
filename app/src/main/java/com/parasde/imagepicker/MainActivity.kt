package com.parasde.imagepicker

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.parasde.imagepicker.adapter.ImagePickerAdapter
import com.parasde.imagepicker.utils.CheckPermission
import androidx.recyclerview.widget.GridLayoutManager
import android.util.DisplayMetrics
import android.widget.Toast
import com.parasde.imagepicker.adapter.ImagePickerItem
import kotlin.Comparator
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val REQUEST_OK = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!CheckPermission(this).check()) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_OK
            )
        } else {
            init()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permission: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_OK -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init()
            } else {
                Toast.makeText(this, R.string.permission, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun init() {
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val cursor = contentResolver.query(uri, projection, null, null, null)

        val albumPath = ArrayList<ImagePickerItem>()
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                val columnDate = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
                val columnAddDate = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED)
                val columnDisplayName =
                    cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val columnTitle = cursor.getColumnIndex(MediaStore.Images.Media.TITLE)

                albumPath.add(
                    ImagePickerItem(
                        cursor.getString(columnDisplayName),
                        cursor.getString(columnTitle),
                        cursor.getString(columnIndex),
                        cursor.getString(columnDate),
                        cursor.getString(columnAddDate)
                    )
                )
            }
            cursor.close()
        }

        albumPath.sortWith(Comparator { o1, o2 ->
            o2.addTime.compareTo(o1.addTime)
        })

        val selectAlbumPath = ArrayList<String>()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        val adapter = ImagePickerAdapter(this, albumPath, selectAlbumPath, width / 3)

        val gridLayoutManager =
            GridLayoutManager(this, 3)

        pickerGallery.layoutManager = gridLayoutManager
        pickerGallery.adapter = adapter

        closeBtn.setOnClickListener {
            finish()
        }

        completeBtn.setOnClickListener {
            Toast.makeText(this, "${selectAlbumPath.size} pictures", Toast.LENGTH_LONG).show()
        }
    }
}
