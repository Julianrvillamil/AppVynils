package com.misw.appvynills.ui.artist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.misw.appvynills.brokers.NetworkModule
import com.misw.appvynills.database.VinylRoomDatabase
import com.misw.appvynills.databinding.FragmentArtistsBinding
import com.misw.appvynills.models.Artist
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.repository.ArtistRepository
import com.misw.appvynills.ui.adapter.ArtistAdapter
import com.misw.appvynills.utils.DataState
import com.misw.appvynills.viewmodel.ListArtistViewModel
import com.misw.appvynills.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch


class ArtistFragment : Fragment() {

    private var _binding: FragmentArtistsBinding? = null
    private val binding get() = _binding!!
    private lateinit var artistAdapter: ArtistAdapter
    val artistService = NetworkModule.artistServiceAdapter

    private val viewModel: ListArtistViewModel by viewModels {
        val database = VinylRoomDatabase.getDatabase(requireContext())
        val artistRepository = ArtistRepository(artistService, database)
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
            layoutManager = GridLayoutManager(context, 2)
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
                    is DataState.Loading -> {
                        binding.loadingIndicator.visibility = View.VISIBLE
                        binding.loadingIndicator.announceForAccessibility("Cargando artistas")
                    }
                    is DataState.Success -> {
                        binding.loadingIndicator.visibility = View.GONE
                        result.data?.let { artistAdapter.updateArtist(it) }
                        //artistAdapter.updateArtist(result.data)
                    }
                    is DataState.Error -> {
                        binding.loadingIndicator.visibility = View.GONE
                        showError(result.error.message)
                    }
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


    private fun showError(message: String?) {
        Toast.makeText(context, message ?: "Error desconocido", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
