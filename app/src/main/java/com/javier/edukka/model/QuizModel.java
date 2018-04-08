package com.javier.edukka.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QuizModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("question")
    @Expose
    private String question;
    @SerializedName("answer")
    @Expose
    private String answer;
    @SerializedName("options")
    @Expose
    private String options;
    @SerializedName("hint")
    @Expose
    private String hint;
    @SerializedName("game_id")
    @Expose
    private String gameId;

    public QuizModel() {}

    public QuizModel(String id, String type, String question, String answer, String options, String hint, String gameId) {
        super();
        this.id = id;
        this.type = type;
        this.question = question;
        this.answer = answer;
        this.options = options;
        this.hint = hint;
        this.gameId = gameId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

}