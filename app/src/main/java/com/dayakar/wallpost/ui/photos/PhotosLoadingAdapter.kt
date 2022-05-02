
package com.dayakar.wallpost.ui.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dayakar.wallpost.databinding.PhotoLoadingStateBinding

class PhotosLoadingAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PhotosLoadingAdapter.LoadingStateViewHolder>() {

    class LoadingStateViewHolder(binding: PhotoLoadingStateBinding, retry: () -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        private val tvErrorMessage: TextView = binding.tvErrorMessage
        private val progressBar: ProgressBar = binding.progressBar
        private val btnRetry: Button = binding.btnRetry

        init {
            btnRetry.setOnClickListener {
                retry()
            }
        }

        fun bindState(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                tvErrorMessage.text = loadState.error.localizedMessage
            }
            progressBar.isVisible = loadState is LoadState.Loading
            tvErrorMessage.isVisible = loadState !is LoadState.Loading
            btnRetry.isVisible = loadState !is LoadState.Loading
        }

    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bindState(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
       val view= PhotoLoadingStateBinding.inflate(LayoutInflater.from(parent.context),parent, false)

        return LoadingStateViewHolder(view, retry)
    }
}