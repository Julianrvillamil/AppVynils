package com.misw.appvynills.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misw.appvynills.R
import com.misw.appvynills.model.CollectorComment

class CollectorCommentsAdapter(
    private val comments: List<CollectorComment>
) : RecyclerView.Adapter<CollectorCommentsAdapter.CommentViewHolder>() {

    class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val commentDescription: TextView = view.findViewById(R.id.comment_description)
        val commentRating: TextView = view.findViewById(R.id.comment_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.commentDescription.text = comment.description
        holder.commentRating.text = "Rating: ${comment.rating}"
    }

    override fun getItemCount(): Int = comments.size
}
