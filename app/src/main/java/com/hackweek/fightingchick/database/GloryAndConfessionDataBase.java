package com.hackweek.fightingchick.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {GloryAndConfessionRecord.class},version = 1)
public abstract class GloryAndConfessionDataBase extends RoomDatabase {
    public abstract GloryAndConfessionDao GloryAndConfessionDao();

}
