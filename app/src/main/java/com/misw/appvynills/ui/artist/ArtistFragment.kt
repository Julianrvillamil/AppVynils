package com.misw.appvynills.ui.artist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.misw.appvynills.R
import com.misw.appvynills.brokers.NetworkModule
import com.misw.appvynills.databinding.FragmentArtistsBinding
import com.misw.appvynills.model.Artist
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.repository.ArtistRepository
import com.misw.appvynills.ui.adapter.AlbumAdapter
import com.misw.appvynills.ui.adapter.ArtistAdapter
import com.misw.appvynills.ui.home.HomeFragmentDirections
import com.misw.appvynills.utils.DataState
import com.misw.appvynills.viewmodel.ListArtistViewModel
import com.misw.appvynills.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class ArtistFragment : Fragment() {

    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!
    private lateinit var artistAdapter: ArtistAdapter
    val artistService = NetworkModule.artistServiceAdapter

    private val viewModel: ListArtistViewModel by viewModels {
        val artistRepository = ArtistRepository(artistService)
        ViewModelFactory(AlbumRepository(requireContext()), artistRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArtistsBinding.inflate(inflater, container, false)
        //artistAdapter = ArtistAdapter(emptyList())


        artistAdapter = ArtistAdapter(emptyList()) { artistId ->
            // Configura la navegación al fragmento de detalle con el artistId
            val action = ArtistFragmentDirections.actionArtistFragmentToArtistDetailFragment(artistId)
            findNavController().navigate(action)
        }
        binding.recyclerViewArtists.apply {
            adapter = artistAdapter
            layoutManager = LinearLayoutManager(context)
        }

        Log.d("ArtistFragment", "En Oncreate")
        // Llama al método `getArtists()` para obtener los datos
        viewModel.getArtists()

        // Observa los datos del ViewModel y actualiza el adapter cuando estén disponibles
        observeArtists()

        return binding.root
    }

    private fun observeArtists() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.artistResult.collect { result ->
                when (result) {
                    is DataState.Loading -> showLoading()
                    is DataState.Success -> updateArtistList(result.data)
                    is DataState.Error -> showError(result.error.message)
                    null -> TODO()
                }
            }
        }
    }

    private fun updateArtistList(artists: List<Artist>?) {
        if (artists != null) {
            artistAdapter.updateArtist(artists)
            Log.d("ArtistFragment", "Número de artistas: ${artists.size}")
        } else {
            Log.d("ArtistFragment", "La lista de artistas está vacía")
        }
    }

    private fun showLoading() {
        // Muestra un indicador de carga en tu UI
        Log.d("ArtistFragment", "Cargando artistas...")
    }

    private fun showError(message: String?) {
        // Muestra el mensaje de error en tu UI
        Log.e("ArtistFragment", "Error: $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
