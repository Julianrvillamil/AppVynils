package com.misw.appvynills.ui.artist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.misw.appvynills.brokers.NetworkModule
import com.misw.appvynills.databinding.FragmentDetailArtistBinding
import com.misw.appvynills.model.Album
import com.misw.appvynills.model.Artist
import com.misw.appvynills.repository.AlbumRepository
import com.misw.appvynills.repository.ArtistRepositoryImpl
import com.misw.appvynills.utils.DataState
import com.squareup.picasso.Picasso

/**
 * Fragment que muestra los detalles de un artista específico
 */
class ArtistDetailFragment : Fragment() {

    // Usando view binding para acceder a las vistas de manera segura
    private var _binding: FragmentDetailArtistBinding? = null
    // Esta propiedad solo es válida entre onCreateView y onDestroyView
    private val binding get() = _binding!!

    // Repositorio para acceder a los datos del artista
    private lateinit var artistRepository: ArtistRepositoryImpl
    private val artistService = NetworkModule.artistServiceAdapter

    // Obtiene el ID del artista de los argumentos del fragmento
    private val artistId: Int by lazy {
        arguments?.getInt("artistId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflar el layout y configurar el binding
        _binding = FragmentDetailArtistBinding.inflate(inflater, container, false)
        artistRepository = ArtistRepositoryImpl(artistService)

        // Configurar la barra superior de la aplicación
        (activity as? AppCompatActivity)?.apply {
            // Habilitar el botón de retroceso
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "Detalle Artista"
        }

        // Verificar que tengamos un ID válido antes de cargar los detalles
        if (artistId != -1) {
            fetchArtistDetails(artistId)
        } else {
            Toast.makeText(context, "Error: ID del artista no encontrado", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    /**
     * Obtiene los detalles del artista usando corrutinas
     * @param artistId El ID del artista a buscar
     */
    private fun fetchArtistDetails(artistId: Int) {
        // Usar viewLifecycleOwner.lifecycleScope para respetar el ciclo de vida del Fragment
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                // Mostrar estado de carga inmediatamente
                setupLoadingState()

                // Manejar los diferentes estados de la respuesta
                when (val result = artistRepository.getArtistById(artistId)) {
                    is DataState.Success -> {
                        displayArtistDetails(result.data)
                    }
                    is DataState.Error -> {
                        showError(result.error.message ?: "Error desconocido")
                    }
                    is DataState.Loading -> {
                        // El estado de carga ya se maneja arriba
                    }
                }
            } catch (e: Exception) {
                showError("Error al cargar el artista: ${e.message}")
            }
        }
    }

    /**
     * Muestra los detalles del artista en la interfaz
     * @param artist El objeto Artist con los datos a mostrar
     */
    private fun displayArtistDetails(artist: Artist) {
        binding.apply {
            // Actualizar el nombre del artista
            artistTitle.text = artist.name
            // Cargar la imagen del artista usando Picasso
            Picasso.get().load(artist.image).into(artistCover)
            artistDescription.text = artist.description
            albumsList.text =
                artist.albums?.joinToString(separator = "\n") { it.name } ?: ""
            birthDate.text = artist.birthDate
        }
    }

    /**
     * Configura la interfaz para mostrar el estado de carga
     */
    private fun setupLoadingState() {
        binding.apply {
            // TODO: Implementar la lógica del estado de carga
            // Ejemplo:
            // progressBar.visibility = View.VISIBLE
            // contentLayout.visibility = View.GONE
        }
    }

    /**
     * Muestra un mensaje de error al usuario
     * @param message El mensaje de error a mostrar
     */
    private fun showError(message: String) {
        // Ocultar el indicador de carga si está visible
        // binding.progressBar.visibility = View.GONE

        // Mostrar el mensaje de error
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /**
     * Limpia los recursos cuando se destruye la vista
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // Evitar fugas de memoria
        _binding = null
    }
}