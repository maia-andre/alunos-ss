package com.dio.myapplication.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dio.myapplication.R;
import com.dio.myapplication.data.AlunosAPI;
import com.dio.myapplication.databinding.ActivityMainBinding;
import com.dio.myapplication.domain.Aluno;
import com.dio.myapplication.ui.adapter.MatchesAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;
    private AlunosAPI alunosAPI;
    private MatchesAdapter matchesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupHttpClient();
        setupMatchsList();
        setupMatchsRefresh();
        setupFloatingActionButton();
    }

    private void setupHttpClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maia-andre.github.io/alunos-ss-api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        alunosAPI = retrofit.create(AlunosAPI.class);
    }

    private void setupMatchsList() {
        binding.rvMatches.setHasFixedSize(true);
        binding.rvMatches.setLayoutManager(new LinearLayoutManager(this));
        findMatchesFromApi();
    }

    private void setupMatchsRefresh() {
        binding.swipeRefresh.setOnRefreshListener(this::findMatchesFromApi);
    }

    private void setupFloatingActionButton() {
        binding.floatButton.setOnClickListener(view -> {
            view.animate().rotationBy(360).setDuration(1000).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Random random = new Random();
                    for (int i = 0; 1 < matchesAdapter.getItemCount(); i++){
                        Aluno match = matchesAdapter.getAlunos().get(i);
                        match.getHomeTeam().setScore(random.nextInt(match.getHomeTeam().getStars() + 1));
                        match.getAwayTeam().setScore(random.nextInt(match.getAwayTeam().getStars() + 1));
                        matchesAdapter.notifyItemChanged(i);
                    }
                }
            });
        });
    }

    private void showErrorMessage() {
        Snackbar.make(binding.swipeRefresh, R.string.error_api, BaseTransientBottomBar.LENGTH_LONG)
                .show();

    }

    private void findMatchesFromApi() {
        binding.swipeRefresh.setRefreshing(true);
        alunosAPI.getAlunos().enqueue(new Callback<List<Aluno>>() {
            @Override
            public void onResponse(Call<List<Aluno>> call, Response<List<Aluno>> response) {
                if(response.isSuccessful()){
                    List<Aluno> matches = response.body();
                    matchesAdapter = new MatchesAdapter(matches);
                    binding.rvMatches.setAdapter(matchesAdapter);
                } else {
                    showErrorMessage();
                }
                binding.swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Aluno>> call, Throwable t) {
                showErrorMessage();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

}
