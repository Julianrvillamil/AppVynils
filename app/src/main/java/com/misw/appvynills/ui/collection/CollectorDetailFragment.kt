package com.misw.appvynills.ui.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.misw.appvynills.databinding.FragmentCollectorDetailBinding
import com.misw.appvynills.repository.CollectorRepository
import com.misw.appvynills.ui.adapter.CollectorCommentsAdapter
import com.misw.appvynills.ui.adapter.FavoritePerformersAdapter
import com.misw.appvynills.ui.adapter.CollectorAlbumsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CollectorDetailFragment : Fragment() {

    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)

        // Obtener el ID del coleccionista desde los argumentos del fragmento
        val collectorId = arguments?.getInt("collectorId") ?: 0

        // Inicializar los RecyclerViews con LayoutManagers
        binding.recyclerViewComments.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPerformers.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewAlbums.layoutManager = LinearLayoutManager(requireContext())

        // Consumir datos desde el repositorio
        val repository = CollectorRepository(requireContext())
        CoroutineScope(Dispatchers.Main).launch {
            val result = withContext(Dispatchers.IO) {
                repository.getCollectorDetails(collectorId)
            }
            result.onSuccess { collector ->
                // Mostrar datos principales
                binding.collectorName.text = collector.name
                binding.collectorEmail.text = collector.email
                binding.collectorTelephone.text = collector.telephone

                // Configurar adaptadores con datos
                binding.recyclerViewComments.adapter = CollectorCommentsAdapter(collector.comments)
                binding.recyclerViewPerformers.adapter = FavoritePerformersAdapter(collector.favoritePerformers)
                binding.recyclerViewAlbums.adapter = CollectorAlbumsAdapter(collector.collectorAlbums)
            }.onFailure { exception ->
                // Manejar errores (puedes mostrar un mensaje al usuario)
                exception.printStackTrace()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
