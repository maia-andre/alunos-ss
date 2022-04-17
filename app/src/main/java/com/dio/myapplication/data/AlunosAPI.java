package com.dio.myapplication.data;

import com.dio.myapplication.domain.Aluno;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AlunosAPI {

    @GET("alunos.json")
    Call<List<Aluno>> getAlunos();
}
