package com.javier.edukka.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActivityModel {

    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("game_id")
    @Expose
    private String gameId;
    @SerializedName("result")
    @Expose
    private String result;

    public ActivityModel() {}

    public ActivityModel(String studentId, String gameId, String result) {
        super();
        this.studentId = studentId;
        this.gameId = gameId;
        this.result = result;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}