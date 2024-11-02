package com.misw.appvynills.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.misw.appvynills.R
import com.misw.appvynills.databinding.FragmentDetailAlbumBinding
import com.squareup.picasso.Picasso
import com.misw.appvynills.model.Album

class AlbumDetailFragment : AppCompatActivity(){

    private lateinit var binding: FragmentDetailAlbumBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDetailAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val album = intent.getSerializableExtra("album") as? Album
        album?.let {
            binding.albumTitle.text = it.name
            binding.albumGenre.text = it.genre
            Picasso.get().load(it.cover).into(binding.albumCover)

            // Aquí puedes añadir más información que quieras mostrar en los detalles
        }
    }
}