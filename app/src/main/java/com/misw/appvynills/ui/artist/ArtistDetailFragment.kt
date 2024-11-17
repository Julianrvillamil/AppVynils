package com.misw.appvynills.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.misw.appvynills.brokers.NetworkModule
import com.misw.appvynills.databinding.FragmentDetailArtistBinding
import com.misw.appvynills.models.Artist
import com.misw.appvynills.repository.ArtistRepository
import com.misw.appvynills.utils.DataState
import kotlinx.coroutines.launch

class ArtistDetailFragment : Fragment() {
    private var _binding: FragmentDetailArtistBinding? = null
    private val binding get() = _binding!!

    private lateinit var artistRepository: ArtistRepository
    private val artistService = NetworkModule.artistServiceAdapter

    // Usar navArgs() en lugar de arguments
    private val args: ArtistDetailFragmentArgs by navArgs()
    private val artistId: Int get() = args.artistId

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailArtistBinding.inflate(inflater, container, false)

        // Inicializar el repositorio
        artistRepository = ArtistRepository(artistService)

        // Cargar los datos del artista
        fetchArtistDetails(artistId)

        return binding.root
    }

    private fun fetchArtistDetails(artistId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                artistRepository.getArtistById(artistId).collect { state ->
                    when (state) {
                        is DataState.Loading -> {

                        }
                        is DataState.Success -> {

                            val artist = state.data
                            println("Test exitoso con artista: $artist")
                            displayArtistDetails(artist as Artist)

                        }
                        is DataState.Error -> {
                            Toast.makeText(
                                context,
                                "Error inesperado",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

            } catch (e: Exception) {

                Toast.makeText(
                    context,
                    "Error inesperado: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
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
                    "No hay álbumes disponibles"
                }
            } ?: "No hay álbumes disponibles"
            albumsList.text = albumsText

            birthDate.text = artist.birthDate
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Ya no necesitamos el companion object con newInstance
}