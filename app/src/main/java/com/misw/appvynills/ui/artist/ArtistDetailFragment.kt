package com.misw.appvynills.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.misw.appvynills.brokers.NetworkModule
import com.misw.appvynills.database.VinylRoomDatabase
import com.misw.appvynills.databinding.FragmentDetailArtistBinding
import com.misw.appvynills.models.Artist
import com.misw.appvynills.repository.ArtistRepository
import com.misw.appvynills.utils.DataState
import com.misw.appvynills.viewmodel.ListArtistViewModel
import kotlinx.coroutines.launch
import com.misw.appvynills.viewmodel.ViewModelFactory

class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentDetailArtistBinding? = null
    private val binding get() = _binding!!

    //private lateinit var artistRepository: ArtistRepository
    private val artistService = NetworkModule.artistServiceAdapter
    private val viewModel: ListArtistViewModel by viewModels {
        val database = VinylRoomDatabase.getDatabase(requireContext())
        val artistRepository = ArtistRepository(artistService, database)
        ViewModelFactory(artistRepository = artistRepository)
    }

    // Usar navArgs() en lugar de arguments
    private val args: ArtistDetailFragmentArgs by navArgs()
    private val artistId: Int get() = args.artistId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailArtistBinding.inflate(inflater, container, false)

        // Cargar los datos del artista
        fetchArtistDetails()
        viewModel.getArtistById(artistId)
        return binding.root
    }

    private fun fetchArtistDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            //viewModel.getArtistById(artistId) // Llama al método del ViewModel
            viewModel.singleArtistResult.collect { result ->
                when (result) {
                    is DataState.Loading -> showLoading()
                    is DataState.Success -> {
                        hideLoading()
                        //displayArtistDetails(state.data)
                        result.data?.let { displayArtistDetails(it) }
                    }
                    is DataState.Error -> {
                        hideLoading()
                        showError(result.error.message)
                    }
                }
            }
            /*try {
                viewModel.getArtistById(artistId)
                viewModel.singleArtistResult.collect { state ->
                    when (state) {
                        is DataState.Loading -> showLoading()
                        is DataState.Success -> displayArtistDetails(state.data)
                        is DataState.Error -> showError(state.error.message)
                    }
                }


            } catch (e: Exception) {

                Toast.makeText(
                    context,
                    "Error inesperado: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }*/
        }
    }

    private fun displayArtistDetails(artist: Artist) {
        binding.apply {
            artistTitle.text = artist.name

            context?.let { ctx ->
                if (artist.image.isNotEmpty()) {
                    Glide.with(ctx)
                        .load(artist.image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(artistCover)
                    artistCover.visibility = View.VISIBLE
                } else {
                    artistCover.visibility = View.GONE
                }
            }

            artistDescription.text = artist.description

            val albumsText = artist.albums?.let { albums ->
                if (albums.isNotEmpty()) {
                    albums.joinToString(separator = "\n") { it.name }
                } else {
                    "No hay artistas disponibles"
                }
            } ?: "No hay artistas disponibles"
            albumsList.text = albumsText

            birthDate.text = artist.birthDate
        }
    }

    private fun showLoading() {
        binding.loadingIndicator.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingIndicator.visibility = View.GONE
    }

    private fun showError(message: String?) {
        hideLoading()
        Toast.makeText(context, message ?: "Error desconocido", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Ya no necesitamos el companion object con newInstance
}