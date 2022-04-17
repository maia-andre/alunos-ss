package com.dio.myapplication.data;

import com.dio.myapplication.domain.Match;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MatchesAPI {

    @GET("matches.json")
    Call<List<Match>> getMatches();
}
