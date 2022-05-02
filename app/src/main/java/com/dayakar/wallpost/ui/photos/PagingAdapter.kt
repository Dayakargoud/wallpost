package com.dayakar.wallpost.ui.photos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dayakar.wallpost.databinding.ImageItemLayoutBinding
import com.dayakar.wallpost.model.Photo

/**
 * @Created By DAYAKAR GOUD BANDARI on 30-04-2022.
 */

class PagingAdapter(val onClick:(item:Photo,view:View)->Unit): PagingDataAdapter<Photo, PagingAdapter.PhotoViewHolder>(PhotoDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(ImageItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class PhotoViewHolder(private val binding:ImageItemLayoutBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item:Photo)= with(binding){
            Glide.with(image).load(item.src.medium).into(image)

            binding.image.setOnClickListener {
                onClick(item,it)
            }
            binding.image.transitionName=item.id.toString()
        }
    }


    companion object PhotoDiffUtil:DiffUtil.ItemCallback<Photo>(){
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo)=oldItem.id==newItem.id
        override fun areContentsTheSame(oldItem: Photo, newItem: Photo)=oldItem==newItem

    }
}