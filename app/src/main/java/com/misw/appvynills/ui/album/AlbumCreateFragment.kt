package com.misw.appvynills.ui.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.misw.appvynills.databinding.FragmentCreateAlbumBinding
import com.misw.appvynills.repository.AlbumRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class AlbumCreateFragment : Fragment() {

    private var _binding: FragmentCreateAlbumBinding? = null
    private val binding get() = _binding!!
    private val albumRepository by lazy { AlbumRepository(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateAlbumBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSubmit.setOnClickListener {
            val name = binding.inputName.text.toString()
            val cover = binding.inputCover.text.toString()
            val releaseDate = binding.inputReleaseDate.text.toString()
            val description = binding.inputDescription.text.toString()
            val genre = binding.inputGenre.text.toString()
            val recordLabel = binding.inputRecordLabel.text.toString()

            if (name.isNotEmpty() && cover.isNotEmpty()) {
                val albumData = JSONObject().apply {
                    put("name", name)
                    put("cover", cover)
                    put("releaseDate", releaseDate)
                    put("description", description)
                    put("genre", genre)
                    put("recordLabel", recordLabel)
                }

                CoroutineScope(Dispatchers.Main).launch {
                    val result = albumRepository.createAlbum(albumData)
                    if (result.isSuccess) {
                        Toast.makeText(requireContext(), "Álbum creado exitosamente", Toast.LENGTH_SHORT).show()
                        requireActivity().onBackPressed() // Regresa al fragmento anterior
                    } else {
                        Toast.makeText(requireContext(), "Error al crear álbum", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Por favor llena los campos obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}