package com.misw.appvynills.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.misw.appvynills.R
import com.misw.appvynills.databinding.FragmentDetailAlbumBinding
import com.squareup.picasso.Picasso
import com.misw.appvynills.model.Album
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.ui.adapter.AlbumAdapter
import kotlinx.coroutines.launch

class AlbumDetailFragment : Fragment(){

    private var _binding: FragmentDetailAlbumBinding? = null
    private val binding get() = _binding!!

    private val albumId: Int by lazy {
        arguments?.getInt("albumId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAlbumBinding.inflate(inflater, container, false)

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


        return binding.root
    }

    private suspend fun fetchAlbumDetails(albumId: Int) {
        // Llama al repositorio para obtener la información detallada del álbum
        try {
            val albumDetails = AlbumRepository(requireContext()).getAlbumDetails(albumId)
            if (albumDetails != null) {
                displayAlbumDetails(albumDetails)
            }
        }catch (e: Exception){
            Toast.makeText(context, "Error al cargar detalles del álbum", Toast.LENGTH_SHORT).show()
        }

    }

    private fun displayAlbumDetails(album: Album) {

        binding.albumName.text = album.name
        binding.albumGenre.text = album.genre
        binding.albumReleaseDate.text = album.releaseDate
        binding.albumDescription.text = album.description
        binding.albumRecordLabel.text = album.recordLabel
        Picasso.get().load(album.cover).into(binding.albumCover)

        // Carga y muestra los comentarios, tracks, o performers adicionales si es necesario
        binding.tracksList.text = album.tracks.joinToString(separator = "\n") { it.name }
        binding.performersList.text = album.performers.joinToString(separator = "\n") { it.name }
        binding.commentsList.text = album.comments.joinToString(separator = "\n") { "${it.rating}⭐: ${it.description}" }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}