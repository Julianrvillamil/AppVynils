package com.misw.appvynills.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misw.appvynills.R
import com.misw.appvynills.model.CollectorAlbum

class CollectorAlbumsAdapter(
    private val albums: List<CollectorAlbum>
) : RecyclerView.Adapter<CollectorAlbumsAdapter.AlbumViewHolder>() {

    class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val albumId: TextView = view.findViewById(R.id.collector_album_id)
        val albumPrice: TextView = view.findViewById(R.id.collector_album_price)
        val albumStatus: TextView = view.findViewById(R.id.collector_album_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_collector_album, parent, false) // Cambiamos al nuevo archivo
        return AlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = albums[position]
        holder.albumId.text = "Album ID: ${album.id}"
        holder.albumPrice.text = "Price: $${album.price}"
        holder.albumStatus.text = "Status: ${album.status}"
    }

    override fun getItemCount(): Int = albums.size
}
