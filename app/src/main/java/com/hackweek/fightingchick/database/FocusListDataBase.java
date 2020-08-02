package com.hackweek.fightingchick.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FocusList.class},version = 1,exportSchema = false)
public abstract class FocusListDataBase extends RoomDatabase {
    public abstract FocusListDao FocusListDao();
}
