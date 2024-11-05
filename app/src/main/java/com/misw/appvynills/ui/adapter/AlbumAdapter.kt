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
import com.misw.appvynills.model.Album

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
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .into(albumCover)
                } else {
                    albumCover.visibility = View.GONE
                }
            } catch (e: Exception) {
                albumCover.visibility = View.GONE
            }

            albumTitle.text = album.name
            albumGenre.text = album.genre

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