package com.misw.appvynills.ui.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.misw.appvynills.databinding.FragmentDetailAlbumBinding
import com.squareup.picasso.Picasso
import com.misw.appvynills.models.Album
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.ui.track.AddTrackDialog
import com.misw.appvynills.viewmodel.AlbumDetailViewModel
import com.misw.appvynills.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class AlbumDetailFragment : Fragment(){

    private var _binding: FragmentDetailAlbumBinding? = null
    private val binding get() = _binding!!

    private val albumId: Int by lazy {
        arguments?.getInt("albumId") ?: -1
    }

    private val viewModel: AlbumDetailViewModel by viewModels {
        ViewModelFactory(AlbumRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAlbumBinding.inflate(inflater, container, false)

        //val albumId = arguments?.getInt("albumId") ?: 0
        // Configura la barra de retroceso
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Detalle Album"



        // Verifica que el ID del álbum sea válido
        if (albumId != -1) {
            viewLifecycleOwner.lifecycleScope.launch {
                fetchAlbumDetails(albumId)
            }
        } else {
            Toast.makeText(context, "Error: ID del álbum no encontrado mi Rey", Toast.LENGTH_SHORT).show()
        }

        binding.buttonAddTrack.setOnClickListener {
            val dialog = AddTrackDialog(albumId) {
                Log.d("AlbumDetailFragment", "Recargando detalles del álbum tras agregar track")
                viewModel.fetchAlbumDetails(albumId)
                Log.d("AlbumDetailFragment", "Se recargo correctamente.................")

            }
            dialog.show(childFragmentManager, "AddTrackDialog")
        }



        setupObservers()

        return binding.root
    }


    private suspend fun fetchAlbumDetails(albumId: Int) {
        // Llama al repositorio para obtener la información detallada del álbum
        try {
            val albumDetails = AlbumRepository(requireContext()).getAlbumDetails(albumId)
            Log.d("AlbumDetailFragment", "fetchAlbumDetails:::: llama a repo para obtener data actualizada albumDetails -> ${albumDetails}")
            if (albumDetails != null) {
                displayAlbumDetails(albumDetails)
            }
        }catch (e: Exception){
            Toast.makeText(context, "Error al cargar detalles del álbum", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setupObservers() {
        // Observa los detalles del álbum
        viewModel.albumDetail.observe(viewLifecycleOwner, Observer { album ->
            if (album != null) {
                Log.d("AlbumDetailFragment", "Detalles del álbum actualizados: ${album.tracks}")

                displayAlbumDetails(album)
            } else {
                Toast.makeText(context, "No se encontraron detalles del álbum", Toast.LENGTH_SHORT).show()
            }
        })

        // Observa el estado de carga
        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            //Log.d("AlbumDetailFragment", "Cargando: $isLoading")
            //binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
            if (isLoading) {
                binding.loadingIndicator.announceForAccessibility("Cargando detalles del álbum")
            }
        }

        // Observa los errores
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            /*error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }*/
            if (!error.isNullOrEmpty()) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun displayAlbumDetails(album: Album) {

        Log.d("AlbumDetailFragment", "displayAlbumDetails:::: ENTRAN EN DISPLAY PARA ACTUALIZAR LA DATA EN FRAGMENT")
        binding.albumName.text = album.name
        binding.albumGenre.text = album.genre
        binding.albumReleaseDate.text = album.releaseDate
        binding.albumDescription.text = album.description
        binding.albumRecordLabel.text = album.recordLabel
        Picasso.get().load(album.cover).into(binding.albumCover)

        // Carga y muestra los comentarios, tracks, o performers adicionales si es necesario
        val tracksText = album.tracks.joinToString(separator = "\n") { "${it.name} - Duración: ${it.duration}" }
        binding.tracksList.text = tracksText
        binding.tracksList.post {
            binding.tracksList.text = tracksText
            binding.tracksList.invalidate()
            binding.tracksList.requestLayout()
        }
        Log.d("AlbumDetailFragment", "Pistas actualizadas en la vista: $tracksText")

        binding.performersList.text = album.performers.joinToString(separator = "\n") { it.name }
        binding.commentsList.text = album.comments.joinToString(separator = "\n") { "${it.rating}⭐: ${it.description}" }

    }

    private fun setupAccessibility(album: Album) {
        binding.albumCover.contentDescription = "Portada del álbum: ${album.name}"
        binding.albumName.contentDescription = "Título del álbum: ${album.name}"
        binding.albumGenre.contentDescription = "Género del álbum: ${album.genre}"
        binding.albumDescription.contentDescription = "Descripción del álbum: ${album.description}"
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}