package com.hackweek.fightingchick.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FocusList.class},version = 1,exportSchema = false)
public abstract class FocusListDataBase extends RoomDatabase {
    public abstract FocusListDao FocusListDao();
}
