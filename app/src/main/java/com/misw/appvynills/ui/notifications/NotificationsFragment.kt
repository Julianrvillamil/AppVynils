package com.misw.appvynills.ui.notifications

import CollectorAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.misw.appvynills.databinding.FragmentNotificationsBinding
import com.misw.appvynills.repository.CollectorRepository
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import java.util.stream.Collectors


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerViewCollectors
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Llama al repositorio para obtener los coleccionistas
        val repository = CollectorRepository(requireContext())
        //llama repo a travesde coroutine
        viewLifecycleOwner.lifecycleScope.launch {
            val result = repository.getCollectors()
            result.onSuccess { collectors ->
                recyclerView.adapter = CollectorAdapter(collectors)
            }.onFailure { exception ->
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

