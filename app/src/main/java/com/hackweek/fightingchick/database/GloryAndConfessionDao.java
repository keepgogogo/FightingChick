package com.hackweek.fightingchick.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GloryAndConfessionDao {
    @Query("SELECT * FROM gloryandconfessionrecord")
    List<GloryAndConfessionRecord> loadAll();

    @Query("SELECT * FROM GloryAndConfessionRecord WHERE date = :date")
    List<GloryAndConfessionRecord> loadByDate(int date);

    @Insert
    void insert(GloryAndConfessionRecord ... gloryAndConfessionRecords);

    @Delete
    void delete(GloryAndConfessionRecord ... gloryAndConfessionRecords);

    @Query("UPDATE gloryandconfessionrecord SET content =:newContent WHERE date = :date and content =:originContent")
    void update(String newContent,int date,String originContent);
}
