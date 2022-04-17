package com.dio.myapplication.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dio.myapplication.databinding.MatchItemBinding;
import com.dio.myapplication.domain.Aluno;
import com.dio.myapplication.ui.DetailActivity;

import java.util.List;

import retrofit2.http.GET;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.ViewHolder> {

    private List<Aluno> alunos;

    public MatchesAdapter(List<Aluno> alunos) {
        this.alunos = alunos;
    }

    public List<Aluno> getAlunos() {
        return alunos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MatchItemBinding binding = MatchItemBinding.inflate(layoutInflater, parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Aluno aluno = alunos.get(position);

        // Adapta os dados da partida (recuperada da API) para o nosso layout.
        Glide.with(context).load(aluno.getHomeTeam().getImage()).circleCrop().into(holder.binding.fotoAluno);
        holder.binding.nameHomeTeam.setText(aluno.getHomeTeam().getName());
        if (aluno.getHomeTeam().getScore() != null){
            holder.binding.scoreHomeTeam.setText(String.valueOf(aluno.getHomeTeam().getScore()));
        }

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.Extras.ALUNO,aluno);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return alunos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MatchItemBinding binding;

        public ViewHolder(MatchItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
