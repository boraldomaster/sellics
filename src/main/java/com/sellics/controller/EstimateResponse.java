package com.sellics.controller;

public class EstimateResponse {
    private String keyword;
    private int score;

    public EstimateResponse(String keyword, int score) {
        this.keyword = keyword;
        this.score = score;
    }

    public String getKeyword() {
        return keyword;
    }

    public int getScore() {
        return score;
    }
}
