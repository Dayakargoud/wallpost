package com.dayakar.wallpost.ui.details

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dayakar.wallpost.R
import com.dayakar.wallpost.databinding.PhotoDetailsFragmentBinding
import com.dayakar.wallpost.model.Photo
import com.dayakar.wallpost.utils.FileUtils.openSettingDetailsScreen
import com.dayakar.wallpost.utils.FileUtils.saveImageToStorage
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class PhotoDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PhotoDetailsFragment()
    }

    private val args by navArgs<PhotoDetailsFragmentArgs>()
    private var _binding:PhotoDetailsFragmentBinding?=null
    private val binding get() = _binding!!

    private val requestPermissions=registerForActivityResult(ActivityResultContracts.RequestPermission()){ granted->
        if (granted){
                saveImage()
        }
        else { // Do something as the permission is not granted
            Snackbar.make(requireContext(),binding.root,"Please grant storage permissions to save Image",Snackbar.LENGTH_LONG)
                .setAction("Open Settings") {
                        openSettingDetailsScreen(requireContext())
                }
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= PhotoDetailsFragmentBinding.inflate(layoutInflater)
        postponeEnterTransition(100,TimeUnit.MILLISECONDS)
        val transition = TransitionInflater.from(requireContext()).inflateTransition(R.transition.custom_shared_transition).apply {
            duration=200L
            interpolator = FastOutSlowInInterpolator()
        }
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
        val photo=args.photo
        loadImage(photo)
        binding.saveFab.setOnClickListener {
            checkStorageWritePermissionsAndSave()
        }

        binding.photographerName.text="Photo by ${photo.photographer}"
      return binding.root
    }

    private fun loadImage(photo:Photo){

        val thumbnail=Glide.with(this).load(photo.src.medium).addListener(object :RequestListener<Drawable>{
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                startPostponedEnterTransition()
                binding.progressBar.visibility=View.GONE
                binding.saveFab.visibility=View.VISIBLE
                return false
            }

        })

        Glide.with(this).load(photo.src.original).thumbnail(thumbnail).dontAnimate()
            .addListener(object:RequestListener<Drawable>{
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {

                    return false
                }

            })
            .into(binding.photo)
    }

    private fun checkStorageWritePermissionsAndSave(){
        //Check Read SMS permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            //request permission
            requestPermissions.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }else{
            //Permissions already granted
            saveImage()

        }
    }

    private fun saveImage()=lifecycleScope.launch {
        val bitmap=(binding.photo.drawable as BitmapDrawable).bitmap
        val isSaved=saveImageToStorage(requireContext(),bitmap)
        if (isSaved){
            Snackbar.make(requireContext(),binding.root,"Image saved to Gallery",Snackbar.LENGTH_SHORT).setAnchorView(binding.saveFab).show()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}