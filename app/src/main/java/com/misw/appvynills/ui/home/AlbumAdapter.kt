package com.misw.appvynills.ui.home

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.misw.appvynills.databinding.ItemAlbumBinding
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import com.misw.appvynills.model.Album


class AlbumAdapter(private val albums: List<Album>): RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.albumTitle.text = album.name
            binding.albumGenre.text = album.genre
            Picasso.get().load(album.cover).into(binding.albumCover)
        }
    }

}