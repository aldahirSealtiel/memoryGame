package com.example.memorygame.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertScore(Score score);
    @Query("SELECT * FROM scores")
    Score getScore();
    @Update
    void updateScore(Score score);
}
