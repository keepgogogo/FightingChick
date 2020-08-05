package com.hackweek.fightingchick.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {FocusList.class},version = 5,exportSchema = false)
public abstract class FocusListDataBase extends RoomDatabase {
    public abstract FocusListDao FocusListDao();
    private static volatile FocusListDataBase INSTANCE;//volatile保证从内存读写，各线程中获取的database实例相同

   public static FocusListDataBase getDatabase(final Context context)
    {
        if(INSTANCE == null )
        {
            synchronized (FocusListDataBase.class)
            {
                if(INSTANCE == null)
                {
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            FocusListDataBase.class,
                            "FocusDataBase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }



}
