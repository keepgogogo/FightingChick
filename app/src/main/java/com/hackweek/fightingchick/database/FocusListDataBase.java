package com.hackweek.fightingchick.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {FocusList.class},version = 1,exportSchema = false)
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
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE FocusList ADD COLUMN identifier NOT NULL");
        }
    };

   static final Migration MIGRATION_2_3 = new Migration(2,3) {
       @Override
       public void migrate(@NonNull SupportSQLiteDatabase database) {
           database.execSQL("ALTER TABLE FocusList ADD COLUMN energyValue NOT NULL");
       }
   }
}
