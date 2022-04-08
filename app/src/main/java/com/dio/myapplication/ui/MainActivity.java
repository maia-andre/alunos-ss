package com.dio.myapplication.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dio.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity{

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupMatchsList();
        setupMatchsRefresh();
        setupFloatingActionButton();
    }

    private void setupMatchsList() {
        //TODO Listar as partidas, consumindo a API.
    }

    private void setupMatchsRefresh() {
        //TODO Atualizar as partidas na ação  de swipe.
    }

    private void setupFloatingActionButton() {
        //TODO Criar evento de click e simulação de partidas.
    }
}
