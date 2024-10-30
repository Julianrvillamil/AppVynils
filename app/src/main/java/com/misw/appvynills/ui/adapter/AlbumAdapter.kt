package com.misw.appvynills.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.misw.appvynills.databinding.ItemAlbumBinding
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import android.view.View
import com.misw.appvynills.R
import com.misw.appvynills.model.Album


class AlbumAdapter(private var albumList: List<Album>):
    RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>()  {

    fun updateAlbums(newAlbums: List<Album>) {
        albumList = newAlbums
        notifyDataSetChanged()
    }
    inner class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.albumTitle.text = album.name
            binding.albumGenre.text = album.genre
            Picasso.get().load(album.cover).into(binding.albumCover)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albumList[position]
        holder.bind(album)
    }

    override fun getItemCount(): Int = albumList.size

    /*class AlbumViewHolder(private val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.albumTitle.text = album.name
            binding.albumGenre.text = album.genre
            Picasso.get().load(album.cover).into(binding.albumCover)
        }
    }*/

}