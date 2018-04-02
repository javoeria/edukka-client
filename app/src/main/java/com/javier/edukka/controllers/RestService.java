package com.javier.edukka.controllers;

import com.javier.edukka.models.ClassModel;
import com.javier.edukka.models.GameModel;
import com.javier.edukka.models.QuizModel;
import com.javier.edukka.models.UserModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestService {

// User Services

    @GET("users")
    Call<List<UserModel>> getUsers();

    @GET("user/{id}")
    Call<UserModel> getUser(@Path("id") int userId);

// Class Services

    @GET("classes")
    Call<List<ClassModel>> getClasses();

    @GET("class/{id}")
    Call<ClassModel> getClass(@Path("id") int classId);

// Game Services

    @GET("games")
    Call<List<GameModel>> getGames();

    @GET("game/{id}")
    Call<GameModel> getGame(@Path("id") int gameId);

// Quiz Services

    @GET("quizzes")
    Call<List<QuizModel>> getQuizzes();

    @GET("quiz/{id}")
    Call<QuizModel> getQuiz(@Path("id") int quizId);

}
