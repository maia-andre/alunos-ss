package com.dio.myapplication.ui

import android.animation.Animator
import androidx.appcompat.app.AppCompatActivity
import com.dio.myapplication.data.AlunosAPI
import com.dio.myapplication.ui.adapter.MatchesAdapter
import android.os.Bundle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.dio.myapplication.domain.Aluno
import com.google.android.material.snackbar.Snackbar
import com.dio.myapplication.R
import com.dio.myapplication.databinding.ActivityMainBinding
import com.google.android.material.snackbar.BaseTransientBottomBar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var alunosAPI: AlunosAPI? = null
    private var matchesAdapter: MatchesAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        setupHttpClient()
        setupMatchsList()
        setupMatchsRefresh()
        setupFloatingActionButton()
    }

    private fun setupHttpClient() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maia-andre.github.io/alunos-ss-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        alunosAPI = retrofit.create(AlunosAPI::class.java)
    }

    private fun setupMatchsList() {
        binding!!.rvMatches.setHasFixedSize(true)
        binding!!.rvMatches.layoutManager = LinearLayoutManager(this)
        findMatchesFromApi()
    }

    private fun setupMatchsRefresh() {
        binding!!.swipeRefresh.setOnRefreshListener { findMatchesFromApi() }
    }

    private fun setupFloatingActionButton() {
        binding!!.floatButton.setOnClickListener { view: View ->
            view.animate().rotationBy(360f).setDuration(1000)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        val random = Random()
                        var i = 0
                        while (1 < matchesAdapter!!.itemCount) {
                            val (_, _, homeTeam, awayTeam) = matchesAdapter!!.alunos[i]
                            homeTeam.score = random.nextInt(homeTeam.stars + 1)
                            awayTeam.score = random.nextInt(awayTeam.stars + 1)
                            matchesAdapter!!.notifyItemChanged(i)
                            i++
                        }
                    }
                })
        }
    }

    private fun showErrorMessage() {
        Snackbar.make(
            binding!!.swipeRefresh,
            R.string.error_api,
            BaseTransientBottomBar.LENGTH_LONG
        )
            .show()
    }

    private fun findMatchesFromApi() {
        binding!!.swipeRefresh.isRefreshing = true
        alunosAPI!!.alunos.enqueue(object : Callback<List<Aluno?>?> {
            override fun onResponse(call: Call<List<Aluno?>?>, response: Response<List<Aluno?>?>) {
                if (response.isSuccessful) {
                    val matches = response.body()
                    matchesAdapter = MatchesAdapter(matches)
                    binding!!.rvMatches.adapter = matchesAdapter
                } else {
                    showErrorMessage()
                }
                binding!!.swipeRefresh.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Aluno?>?>, t: Throwable) {
                showErrorMessage()
                binding!!.swipeRefresh.isRefreshing = false
            }
        })
    }
}