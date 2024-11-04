package com.misw.appvynills.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.misw.appvynills.R
import com.misw.appvynills.databinding.FragmentHomeBinding
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.ui.adapter.AlbumAdapter
import com.misw.appvynills.viewmodel.HomeViewModel
import com.misw.appvynills.viewmodel.ViewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var albumAdapter: AlbumAdapter

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(AlbumRepository(requireContext()))
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        albumAdapter = AlbumAdapter(emptyList()) { albumId ->
            // Configura la navegación al fragmento de detalle con el albumId
            val action = HomeFragmentDirections.actionHomeFragmentToAlbumDetailFragment(albumId)
            findNavController().navigate(action)
        }
        binding.recyclerViewAlbums.apply {
            adapter = albumAdapter
            layoutManager = LinearLayoutManager(context)
        }
        //binding.recyclerViewAlbums.adapter = albumAdapter
        //binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(context)
        val root: View = binding.root


        // Observa los datos del ViewModel y actualiza el adapter cuando estén disponibles
        homeViewModel.albumsLiveData.observe(viewLifecycleOwner) { albums ->
            if (albums.isNotEmpty()) {
                albumAdapter.updateAlbums(albums)
                Log.d("HomeFragment", "Número de álbumes: ${albums.size}")
            } else {
                Log.d("HomeFragment", "La lista de álbumes está vacía")
            }
        }

        // Llama a la función para obtener los datos
        homeViewModel.fetchAlbums()


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}