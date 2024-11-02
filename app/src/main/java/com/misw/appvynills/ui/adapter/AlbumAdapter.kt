package com.misw.appvynills.ui.adapter

import android.content.Intent
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.misw.appvynills.databinding.ItemAlbumBinding
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.misw.appvynills.R
import com.misw.appvynills.model.Album
import com.misw.appvynills.ui.home.AlbumDetailFragment


class AlbumAdapter(private var albumList: List<Album>):
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>()  {

    fun updateAlbums(newAlbums: List<Album>) {
        albumList = newAlbums
        notifyDataSetChanged()
    }
    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val albumCover: ImageView = itemView.findViewById(R.id.albumCover)
        private val albumTitle: TextView = itemView.findViewById(R.id.albumTitle)
        private val albumGenre: TextView = itemView.findViewById(R.id.albumGenre)


        fun bind(album: Album) {
           /* Log.d("AlbumAdapter", "Título del álbum: ${album.name}")
            Log.d("AlbumAdapter", "Género del álbum: ${album.genre}")
            Log.d("AlbumAdapter", "URL de portada del álbum: ${album.cover}")
            */
            Picasso.get()
                .load(album.cover) // 'album.coverUrl' con la propiedad que contenga la URL de la imagen
                .placeholder(R.drawable.ic_launcher_background) // Imagen de marcador de posición mientras se carga
                .error(R.drawable.ic_launcher_foreground) // Imagen que se mostrará si ocurre un error al cargar
                .into(albumCover) // El ImageView donde se cargará la imagen
            //albumCover.setImageResource(R.drawable.placeholder) // Cambia esto por la lógica de carga
            albumTitle.text = album.name
            albumGenre.text = album.genre

            // Configura el clic en la imagen para abrir AlbumDetailActivity
            albumCover.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, AlbumDetailFragment::class.java).apply {
                    putExtra("album", album) // Pasa el objeto album
                }
                context.startActivity(intent)
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