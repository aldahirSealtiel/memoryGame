package com.example.memorygame.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "scores")
public class Score {
    public Score( int pointsScore) {
        this.pointsScore = pointsScore;

    }
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int id;
    private int pointsScore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPointsScore() {
        return pointsScore;
    }

    public void setPointsScore(int pointsScore) {
        this.pointsScore = pointsScore;
    }

}
