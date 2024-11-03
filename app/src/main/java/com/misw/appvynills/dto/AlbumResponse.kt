package com.misw.appvynills.dto

import com.misw.appvynills.model.Comment
import com.misw.appvynills.model.Performer
import com.misw.appvynills.model.Track

data class AlbumResponse(
    val id: Int,
    val name: String,
    val cover: String,
    val description: String,
    val releaseDate: String,
    val genre: String,
    val recordLabel: String,
    val comments: List<Comment>,
    val performers: List<Performer>,
    val tracks: List<Track>
)