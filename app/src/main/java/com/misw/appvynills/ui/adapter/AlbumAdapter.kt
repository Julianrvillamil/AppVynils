package com.misw.appvynills.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.misw.appvynills.R
import com.misw.appvynills.models.Album

class AlbumAdapter(
    private var albumList: List<Album>,
    private val onAlbumClick: (Int) -> Unit
) : RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {

    fun updateAlbums(newAlbums: List<Album>) {
        albumList = newAlbums
        notifyDataSetChanged()
    }

    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)
        private val albumTitle: TextView = itemView.findViewById(R.id.albumTitle)
        private val albumGenre: TextView = itemView.findViewById(R.id.albumGenre)

        fun bind(album: Album) {
            try {
                if (!album.cover.isNullOrEmpty()) {
                    Glide.with(itemView.context)
                        .load(album.cover)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .dontAnimate()
                        .into(albumCover)
                    albumCover.contentDescription = "Imagen del álbum: ${album.name}"
                } else {
                    albumCover.visibility = View.GONE
                    albumCover.contentDescription = "No hay imagen disponible para el álbum: ${album.name}"
                }
            } catch (e: Exception) {
                albumCover.visibility = View.GONE
                albumCover.contentDescription = "Error al cargar la imagen del álbum: ${album.name}"
            }

            albumTitle.text = album.name
            albumTitle.contentDescription = "Título del álbum: ${album.name}"

            albumGenre.text = album.genre
            albumGenre.contentDescription = "Género: ${album.genre}"

            // Configura el clic en la imagen
            itemView.setOnClickListener {
                onAlbumClick(album.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albumList[position])
    }

    override fun getItemCount(): Int = albumList.size

}