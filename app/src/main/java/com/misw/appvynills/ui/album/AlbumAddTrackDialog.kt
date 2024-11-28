package com.misw.appvynills.ui.track

import android.R
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.misw.appvynills.databinding.DialogAddTrackAlbumBinding
import com.misw.appvynills.repository.AlbumRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class AddTrackDialog(private val albumId: Int) : DialogFragment() {

    private var _binding: DialogAddTrackAlbumBinding? = null
    private val binding get() = _binding!!
    private val albumRepository by lazy { AlbumRepository(requireContext()) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAddTrackAlbumBinding.inflate(inflater, container, false)




        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

        binding.buttonSubmit.setOnClickListener {
            val trackName = binding.inputTrackName.text.toString()
            val trackDuration = binding.inputTrackDuration.text.toString()

            if (trackName.isEmpty() || trackDuration.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            addTrackToAlbum(trackName, trackDuration)
        }
    }

    private fun addTrackToAlbum(name: String, duration: String) {
        val trackData = JSONObject().apply {
            put("name", name)
            put("duration", duration)
        }

        lifecycleScope.launch {
            try {
                val result = albumRepository.addTrackToAlbum(albumId, trackData)
                if (result.isSuccess) {
                    Toast.makeText(requireContext(), "Track agregado exitosamente", Toast.LENGTH_SHORT).show()
                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Error al agregar track", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AddTrackDialog", "Error al agregar track", e)
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
