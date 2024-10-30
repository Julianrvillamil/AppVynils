package com.misw.appvynills.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    //private val homeViewModel: HomeViewModel by viewModels()
    // viewModels para instanciar el ViewModel de este fragmento

    private val homeViewModel: HomeViewModel by viewModels {
        ViewModelFactory(AlbumRepository(requireContext()))
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = albumAdapter */


        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Inicializa el adapter y configura el RecyclerView
        albumAdapter = AlbumAdapter(emptyList())

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewAlbums)
        recyclerView.adapter = albumAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observa los datos del ViewModel y actualiza el adapter cuando estén disponibles
        homeViewModel.albumsLiveData.observe(viewLifecycleOwner) { albums ->
            albumAdapter.updateAlbums(albums)
        }

        // Llama a la función para obtener los datos
        homeViewModel.fetchAlbums()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}