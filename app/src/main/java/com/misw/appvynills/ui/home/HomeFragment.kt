package com.misw.appvynills.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.databinding.FragmentHomeBinding
import com.misw.appvynills.ui.adapter.AlbumAdapter
import com.misw.appvynills.viewmodel.HomeViewModel
import com.misw.appvynills.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // viewModels para instanciar el ViewModel de este fragmento
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(AlbumRepository(requireContext()))
        //ViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del RecyclerView - fragment_home.xml
        binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(requireContext())

        // Observar los álbumes desde el ViewModel
        homeViewModel.albumsLiveData.observe(viewLifecycleOwner) { albums ->
            binding.recyclerViewAlbums.adapter = AlbumAdapter(albums)
        }

        // Llamada inicial para cargar los álbumes
        homeViewModel.fetchAlbums()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}