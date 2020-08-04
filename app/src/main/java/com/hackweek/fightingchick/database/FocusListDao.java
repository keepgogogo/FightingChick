package com.hackweek.fightingchick.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dao
public interface FocusListDao {
    @Insert
    void insert(FocusList ... focusLists);

    @Query("SELECT * FROM focuslist")
    List<FocusList> loadAll();

    @Update
    void update(FocusList ... focusLists);

    @Delete
    void delete(FocusList ... focusLists);

    @Query("SELECT * FROM FocusList WHERE date = :date")
    List<FocusList> getByDate(int date);

    //返回上周的
    @Query("SELECT * FROM FocusList WHERE :weekOfYear = 1 AND year= :year-1 AND month = 52 OR year = :year AND weekOfYear = :weekOfYear-1")
    List<FocusList> getLastWeekEnergyWeekday(int year, int weekOfYear);

    //返回上个月的
    @Query("SELECT * FROM FocusList WHERE :month = 1 AND year = :year-1 AND month = 12 OR year = :year AND month = :month-1")
    List<FocusList> getLastMonthEnergyDayOfMonth(int year, int month);

}
