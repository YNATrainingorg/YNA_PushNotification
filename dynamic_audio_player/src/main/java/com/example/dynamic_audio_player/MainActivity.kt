package com.example.dynamic_audio_player

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dynamic_audio_player.viewmodel.PlayerViewModel
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var btnPlay: Button
    private lateinit var btnPause: Button
    private lateinit var btnNext: Button
    private lateinit var btnPrevious: Button
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[PlayerViewModel::class.java]

        initializeViews()
        viewModel.bindService()
        fetchAudioUrls()

        btnPlay.setOnClickListener {
            if (viewModel.isPaused) viewModel.resume() else viewModel.play()
        }

        btnPause.setOnClickListener { viewModel.pause() }
        btnNext.setOnClickListener { viewModel.next() }
        btnPrevious.setOnClickListener { viewModel.previous() }
    }

    private fun fetchAudioUrls() {
        val databaseRef = FirebaseDatabase.getInstance().getReference("url")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val urls = mutableListOf<String>()
                for (child in snapshot.children) {
                    child.getValue(String::class.java)?.let { urls.add(it) }
                }
                if (urls.isNotEmpty()) {
                    viewModel.audioUrls = urls
                    showToast("Loaded ${urls.size} audio files")
                    btnPlay.isEnabled = true
                    btnNext.isEnabled = true
                    btnPrevious.isEnabled = true
                }
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Failed: ${error.message}")
            }
        })
    }

    private fun initializeViews() {
        btnPlay = findViewById(R.id.idBtnPlay)
        btnPause = findViewById(R.id.idBtnPause)
        btnNext = findViewById(R.id.idBtnNext)
        btnPrevious = findViewById(R.id.idBtnPrevious)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}