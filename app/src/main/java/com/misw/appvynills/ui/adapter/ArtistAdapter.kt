package com.misw.appvynills.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misw.appvynills.R
import com.misw.appvynills.model.Artist
import com.squareup.picasso.Picasso
import android.util.Log

class ArtistAdapter(private var artists: List<Artist>,
                    private val onArtistClick: (Int) -> Unit) : RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    override fun getItemCount(): Int = artists.size

    fun updateArtist(newArtists: List<Artist>) {
        artists = newArtists
        notifyDataSetChanged()
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val artistCover: ImageView = itemView.findViewById(R.id.artistCover)
        private val artistTitle: TextView = itemView.findViewById(R.id.artistTitle)


        fun bind(artist: Artist) {
            Picasso.get()
                .load(artist.image) // 'album.coverUrl' con la propiedad que contenga la URL de la imagen
                .placeholder(R.drawable.ic_launcher_background) // Imagen de marcador de posición mientras se carga
                .error(R.drawable.ic_launcher_foreground) // Imagen que se mostrará si ocurre un error al cargar
                .into(artistCover) // El ImageView donde se cargará la imagen
            artistTitle.text = artist.name

            // Configura el clic en la imagen para abrir ArtistDetailFragment
            artistCover.setOnClickListener {
                Log.d("ArtistAdapter", "id: ${artist.id}")
                onArtistClick(artist.id) // Llama al lambda con el ID del álbum
            }
        }
    }
}

