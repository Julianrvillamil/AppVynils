package com.misw.appvynills.ui.album

import android.R
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.misw.appvynills.databinding.FragmentCreateAlbumBinding
import com.misw.appvynills.repository.AlbumRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AlbumCreateFragment : Fragment() {

    private var _binding: FragmentCreateAlbumBinding? = null
    private val binding get() = _binding!!
    private val albumRepository by lazy { AlbumRepository(requireContext()) }
    private var isCreatingAlbum = false // Bandera para evitar solicitudes duplicadas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCreateAlbumBinding.inflate(inflater, container, false)
        return binding.root

        //onViewCreated(View, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("AlbumCreateFragment", "...Vista de creación de álbum inicializada...")


        // Configuración de validación de texto
        setupTextWatchers()
        setupSpinners()
        setupDatePicker()

        // Configuración del botón de envío
        binding.buttonSubmit.setOnClickListener {
            Log.i("AlbumCreateFragment", "Botón de 'Crear Álbum' presionado...")
            if (!isCreatingAlbum) { // Asegúrate de que no haya otra solicitud en curso
                createAlbum()
            } else {
                Log.w("AlbumCreateFragment", "Ya hay una solicitud de creación en progreso, ignorando clic adicional.")
            }
        }

        // Configuración del botón de cancelar
        binding.buttonCancel.setOnClickListener {
            Log.i("AlbumCreateFragment", "Botón de 'Cancelar' presionado...")
            findNavController().popBackStack() // Navegar de regreso al fragmento anterior
        }
    }

    private fun setupSpinners() {
        val genres = listOf("Classical", "Salsa", "Rock", "Folk")
        val recordLabels = listOf("Sony Music", "EMI", "Discos Fuentes", "Elektra", "Fania Records")

        binding.spinnerGenre.adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_dropdown_item,
            genres
        )

        binding.spinnerRecordLabel.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            recordLabels
        )
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setupDatePicker() {
        val calendar = Calendar.getInstance()

        binding.inputReleaseDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'00:00:00", Locale.US)
                    val formattedDate = dateFormat.format(calendar.time)
                    binding.inputReleaseDate.setText(formattedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupTextWatchers() {
        Log.i("AlbumCreateFragment", "Configurando validaciones de formulario...")

        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateForm()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.inputName.addTextChangedListener(textWatcher)
        binding.inputCover.addTextChangedListener(textWatcher)
        binding.inputReleaseDate.addTextChangedListener(textWatcher)
        binding.inputDescription.addTextChangedListener(textWatcher)
    }

    private fun validateForm() {
        val isFormValid = binding.inputName.text.isNotEmpty() &&
                binding.inputCover.text.isNotEmpty() &&
                binding.inputReleaseDate.text.isNotEmpty() &&
                binding.inputDescription.text.isNotEmpty() &&
                binding.spinnerGenre.selectedItem != null &&
                binding.spinnerRecordLabel.selectedItem != null
        Log.i("AlbumCreateFragment", "Validación del formulario: $isFormValid")

        binding.buttonSubmit.isEnabled = isFormValid
    }

    private fun createAlbum() {
        isCreatingAlbum = true // Marca que el álbum está siendo creado
        binding.buttonSubmit.isEnabled = false //deshabilitar boton para evitar doble clic

        val name = binding.inputName.text.toString()
        val cover = binding.inputCover.text.toString()
        val releaseDate = binding.inputReleaseDate.text.toString()
        val description = binding.inputDescription.text.toString()
        val genre = binding.spinnerGenre.selectedItem.toString()
        val recordLabel = binding.spinnerRecordLabel.selectedItem.toString()

        Log.i("AlbumCreateFragment", "Datos del formulario: Name=$name, Cover=$cover, ReleaseDate=$releaseDate, Description=$description, Genre=$genre, RecordLabel=$recordLabel")

        val albumData = JSONObject().apply {
            put("name", name)
            put("cover", cover)
            put("releaseDate", releaseDate)
            put("description", description)
            put("genre", genre)
            put("recordLabel", recordLabel)
        }

        Log.i("AlbumCreateFragment", "Datos enviados: $albumData")


        CoroutineScope(Dispatchers.Main).launch {
            Log.i("AlbumCreateFragment", "Enviando solicitud para crear álbum...")

            try {
                val result = albumRepository.createAlbum(albumData)
                if (result.isSuccess) {
                    Toast.makeText(requireContext(), "Álbum creado exitosamente", Toast.LENGTH_SHORT).show()
                    delay(2000)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(requireContext(), "Error al crear álbum: ${result.exceptionOrNull()?.message}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AlbumCreateFragment", "Error al enviar solicitud: ${e.message}", e)
                Toast.makeText(requireContext(), "Error al enviar solicitud: ${e.message}", Toast.LENGTH_SHORT).show()
            }finally {
                binding.buttonSubmit.isEnabled = true //habilitar nuevamente el boton
            }
        }
    }
}