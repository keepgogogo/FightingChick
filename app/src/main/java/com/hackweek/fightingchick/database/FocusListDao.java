package com.hackweek.fightingchick.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FocusListDao {
    @Insert
    void insert(FocusList ... focusLists);

    @Query("SELECT * FROM focuslist")
    List<FocusList> loadAll();

    @Update
    void update(FocusList... focusLists);

    @Delete
    void delete(FocusList ... focusLists);

    @Query("SELECT * FROM focuslist WHERE date = :date")
    List<FocusList> getByDate(int date);

}
