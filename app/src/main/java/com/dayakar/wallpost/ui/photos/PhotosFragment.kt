package com.dayakar.wallpost.ui.photos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dayakar.wallpost.R
import com.dayakar.wallpost.databinding.FragmentPhotosBinding
import com.dayakar.wallpost.model.Photo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private var _binding:FragmentPhotosBinding?=null
    private val binding get() = _binding!!
    private val viewModel by viewModels<PhotosViewModel>()

    private val photoAdapter=PagingAdapter{photo,view->
        openPhoto(photo,view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentPhotosBinding.inflate(inflater)

        binding.photosRecyclerview.apply {
            adapter=photoAdapter.withLoadStateHeaderAndFooter(header = PhotosLoadingAdapter{photoAdapter.retry()}, footer = PhotosLoadingAdapter{photoAdapter.retry()})
            setHasFixedSize(true)
            layoutManager=StaggeredGridLayoutManager(2,RecyclerView.VERTICAL)
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener{
                override fun onPreDraw(): Boolean {
                    viewTreeObserver.removeOnPreDrawListener(this)
                    startPostponedEnterTransition()
                    return true
                }

            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.photos.collectLatest {
                binding.loadingProgress.visibility=View.GONE
                photoAdapter.submitData(it)
            }
        }

    }

    private fun openPhoto(photo:Photo,view: View){
        val extras=FragmentNavigatorExtras(view to requireContext().getString(R.string.transition_image))
        findNavController().navigate(PhotosFragmentDirections.actionPhotosFragmentToPhotoDetailsFragment(photo),extras)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}