package com.misw.appvynills.ui.collector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.misw.appvynills.databinding.FragmentCollectorDetailBinding
import com.misw.appvynills.repository.CollectorRepository
import kotlinx.coroutines.launch
import android.util.Log

class CollectorDetailFragment : Fragment() {

    private var _binding: FragmentCollectorDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectorDetailBinding.inflate(inflater, container, false)

        // Obtén el ID del coleccionista de los argumentos del fragmento
        val collectorId = arguments?.getInt("collectorId") ?: 0

        // Llama al repositorio para obtener los detalles del coleccionista usando corrutinas
        val repository = CollectorRepository(requireContext())
        lifecycleScope.launch {
            try {
                val result = repository.getCollectorDetails(collectorId)
                result.onSuccess { collector ->
                    binding.collectorName.text = collector.name
                    binding.collectorEmail.text = collector.email
                    binding.collectorTelephone.text = collector.telephone
                }.onFailure { error ->
                    Log.e("CollectorDetailFragment", "Error al obtener detalles: ${error.message}")
                }
            } catch (e: Exception) {
                Log.e("CollectorDetailFragment", "Excepción al obtener detalles: ${e.message}", e)
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
