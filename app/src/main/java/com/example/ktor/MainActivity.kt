package com.example.ktor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ktor.Adapter.PostAdapter
import com.example.ktor.data.util.ApiState
import com.example.ktor.databinding.ActivityMainBinding
import com.example.ktor.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var postAdapter: PostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerview()
        mainViewModel.getPost()
        handleResponse()
    }

    private fun handleResponse() {
        lifecycleScope.launchWhenStarted {
            mainViewModel.apiStateFlow.collect {
                when (it) {
                    is ApiState.Loading -> {
                        binding.apply {
                            progressBar.isVisible = true
                            recyclerview.isVisible = false
                            error.isVisible = false
                        }
                    }
                    is ApiState.Success -> {
                        binding.apply {
                            progressBar.isVisible = false
                            recyclerview.isVisible = true
                            error.isVisible = false
                        }
                        Log.d("main", "handleResponse: ${it.data}")
                        postAdapter.submitList(it.data)
                    }
                    is ApiState.Failure -> {
                        binding.apply {
                            progressBar.isVisible = false
                            recyclerview.isVisible = false
                            error.isVisible = true
                            error.text = it.msg.toString()
                        }

                    }
                    is ApiState.Empty -> {

                    }
                }
            }
        }
    }

    private fun initRecyclerview() {
        binding.apply {
            recyclerview.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = postAdapter
            }
        }
    }
}