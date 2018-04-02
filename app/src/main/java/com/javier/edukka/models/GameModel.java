package com.javier.edukka.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("difficulty")
    @Expose
    private String difficulty;
    @SerializedName("vote")
    @Expose
    private String vote;
    @SerializedName("teacher_id")
    @Expose
    private String teacherId;

    public GameModel() {}

    public GameModel(String id, String subject, String title, String description, String difficulty, String vote, String teacherId) {
        super();
        this.id = id;
        this.subject = subject;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.vote = vote;
        this.teacherId = teacherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

}