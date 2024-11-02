package com.misw.appvynills.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misw.appvynills.R
import com.misw.appvynills.model.Album
import com.misw.appvynills.databinding.ItemAlbumBinding
import com.squareup.picasso.Picasso

class AlbumAdapter(
    private var albumList: List<Album>,
    private val onAlbumClick: (Album) -> Unit // Listener para manejar el clic en el álbum
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
            Picasso.get()
                .load(album.cover)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(albumCover)

            albumTitle.text = album.name
            albumGenre.text = album.genre

            // Configura el clic en el álbum para navegar al detalle
            itemView.setOnClickListener {
                onAlbumClick(album) // Llama al listener con el álbum seleccionado
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumList[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int = albumList.size
}
