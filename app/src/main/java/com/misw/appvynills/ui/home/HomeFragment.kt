package com.misw.appvynills.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.recyclerview.widget.LinearLayoutManager
import com.misw.appvynills.brokers.AlbumRepository
import com.misw.appvynills.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // viewModels para instanciar el ViewModel de este fragmento
    //private val homeViewModel: HomeViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(AlbumRepository(requireContext())) // Asegúrate de usar la fábrica correctamente
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val homeViewModel =
            //ViewModelProvider(this).get(HomeViewModel::class.java)

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

        // Configuración del RecyclerView
        binding.albumRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observar los álbumes desde el ViewModel
        homeViewModel.albumsLiveData.observe(viewLifecycleOwner) { albums ->
            binding.albumRecyclerView.adapter = AlbumAdapter(albums)
        }

        // Llamada inicial para cargar los álbumes
        homeViewModel.fetchAlbums()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}