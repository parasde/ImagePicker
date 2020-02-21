package com.parasde.imagepicker.adapter

import android.app.AlertDialog
import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.parasde.imagepicker.R
import com.parasde.imagepicker.utils.DateFormat
import kotlinx.android.synthetic.main.gallery_item.view.*


class ImagePickerAdapter(
    private val mContext: Context,
    private val albumPath: ArrayList<ImagePickerItem>,
    private val selectAlbumPath: ArrayList<String>,
    private val width: Int
) : RecyclerView.Adapter<ImagePickerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): ImagePickerAdapter.ViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.gallery_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albumPath.size
    }

    override fun onBindViewHolder(viewHolder: ImagePickerAdapter.ViewHolder, position: Int) {
        val w = (width * 1.2).toInt()
        Glide.with(mContext)
            .load(albumPath[position].path)
            .apply(RequestOptions().override(w, w).centerCrop())
            .into(viewHolder.img)

        viewHolder.img.setOnClickListener(ItemClick(position))
        viewHolder.img.setOnLongClickListener(ItemInfo(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.galleryItem

        init {
            val param = img.layoutParams
            param.width = width
            param.height = width
            img.layoutParams = param
        }
    }

    inner class ItemClick(private val position: Int) : View.OnClickListener {
        override fun onClick(v: View?) {
            var b = false
            for (i in 0 until selectAlbumPath.size) {
                if (albumPath[position].path == selectAlbumPath[i]) {
                    selectAlbumPath.remove(albumPath[position].path)
                    v!!.alpha = 1f
                    b = true
                    break
                }
            }

            if (!b) {
                if (selectAlbumPath.size < 5) {
                    selectAlbumPath.add(albumPath[position].path)
                    v!!.alpha = 0.5f
                } else {
                    Toast.makeText(mContext, R.string.max_choose, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    inner class ItemInfo(private val position: Int) : View.OnLongClickListener {
        override fun onLongClick(v: View?): Boolean {
            val build = AlertDialog.Builder(mContext, R.style.CustomDialog)
                .setTitle(R.string.infoTitle)
                .setMessage(
                    "위치 : ${albumPath[position].pos}\n\n" +
                            "파일명 : ${albumPath[position].name}\n\n" +
                            "생성일 : ${DateFormat.format(albumPath[position].time.toLong())}"
                )
                .setPositiveButton("", null)
                .setNegativeButton("닫기") { dialog, _ ->
                    dialog.dismiss()
                }

            val dialog = build.create()
            dialog.show()

            return false
        }
    }
}