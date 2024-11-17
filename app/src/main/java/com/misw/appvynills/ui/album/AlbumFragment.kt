package com.misw.appvynills.ui.album

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
import com.google.android.material.snackbar.Snackbar
import com.misw.appvynills.databinding.FragmentAlbumBinding
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.ui.adapter.AlbumAdapter
import com.misw.appvynills.viewmodel.AlbumViewModel
import com.misw.appvynills.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch


class AlbumFragment : Fragment() {

    private var _binding: FragmentAlbumBinding? = null
    private val binding get() = _binding!!
    private lateinit var albumAdapter: AlbumAdapter

    private val albumViewModel: AlbumViewModel by viewModels {
        ViewModelFactory(AlbumRepository(requireContext()))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAlbumBinding.inflate(inflater, container, false)
        //binding.recyclerViewAlbums.adapter = albumAdapter
        //binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(context)
        val root: View = binding.root


        // Observa los datos del ViewModel y actualiza el adapter cuando estén disponibles

        setupRecyclerView()
        setupObservers()
        // Llama a la función para obtener los datos
        //albumViewModel.fetchAlbums()
        loadAlbums()


        return root
    }

    private fun setupRecyclerView(){
        albumAdapter = AlbumAdapter(emptyList()) { albumId ->
            // Configura la navegación al fragmento de detalle con el albumId
            try {
                val action = AlbumFragmentDirections.actionHomeFragmentToAlbumDetailFragment(albumId)
                findNavController().navigate(action)
            } catch (e: Exception) {
                Log.e("AlbumFragment", "Error en navegación", e)
                showError("Error al navegar al detalle")
            }
        }
        binding.recyclerViewAlbums.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(context)
            //setHasFixedSize(true) // permite optimizar cuando los items tienen tamaño fijo
        }

    }

    private fun setupObservers() {

        albumViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            Log.d("AlbumFragment", "Cargando: $isLoading")
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
           // binding.recyclerViewAlbums.visibility = if (isLoading) View.GONE else View.VISIBLE

        }

        albumViewModel.albumsLiveData.observe(viewLifecycleOwner) { albums ->
            if (!albums.isNullOrEmpty()) {
                Log.d("AlbumFragment", "Álbumes cargados: ${albums.size}")
                albumAdapter.updateAlbums(albums)
            } else {
                Log.d("AlbumFragment", "La lista de álbumes está vacía")
            }
        }

        //observer para errores de red identificados
        albumViewModel.error.observe(viewLifecycleOwner) { hasError ->

            hasError?.let {
                Log.e("AlbumFragment", "Error: $it")
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
        }
    }
    private fun loadAlbums() {
        try {
            albumViewModel.fetchAlbums()
        } catch (e: Exception) {
            Log.e("AlbumFragment", "Error cargando albums", e)
            showError("Error al cargar los álbumes")
        }
    }

    private fun showError(message: String) {
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}