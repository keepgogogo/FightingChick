package com.hackweek.fightingchick.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {GloryAndConfessionRecord.class},version = 1,exportSchema = false)
public abstract class GloryAndConfessionDataBase extends RoomDatabase {
    public abstract GloryAndConfessionDao GloryAndConfessionDao();
    private static volatile GloryAndConfessionDataBase INSTANCE;//volatile保证从内存中读取，各线程将获取到相同的数据库实例

    public static GloryAndConfessionDataBase getDataBase(final Context context)
    {
        if(INSTANCE==null)
        {
            synchronized (GloryAndConfessionDataBase.class)
            {
                if(INSTANCE==null)
                {
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            GloryAndConfessionDataBase.class,
                            "GloryAndConfessionDataBase").build();

                }
            }
        }
        return INSTANCE;
    }
}
