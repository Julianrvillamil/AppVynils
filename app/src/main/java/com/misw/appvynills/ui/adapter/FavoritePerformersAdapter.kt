package com.misw.appvynills.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misw.appvynills.R
import com.misw.appvynills.model.CollectorPerformer

class FavoritePerformersAdapter(
    private val performers: List<CollectorPerformer>
) : RecyclerView.Adapter<FavoritePerformersAdapter.PerformerViewHolder>() {

    class PerformerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val performerImage: ImageView = view.findViewById(R.id.performer_image)
        val performerName: TextView = view.findViewById(R.id.performer_name)
        val performerDescription: TextView = view.findViewById(R.id.performer_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerformerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_performer, parent, false)
        return PerformerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PerformerViewHolder, position: Int) {
        val performer = performers[position]
        holder.performerName.text = performer.name
        holder.performerDescription.text = performer.description
        Glide.with(holder.performerImage.context)
            .load(performer.image)
            .into(holder.performerImage)
    }

    override fun getItemCount(): Int = performers.size
}
