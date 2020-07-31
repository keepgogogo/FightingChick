package com.hackweek.fightingchick.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "GloryAndConfessionRecord")
public class GloryAndConfessionRecord {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo( name = "date")
    public int date;

    @ColumnInfo( name = "content")
    public String content;

    //when this boolean is true , this record is a glory record
    //if not,this record is a confession
    @ColumnInfo( name = "GloryOrConfession")
    public boolean gloryOrConfession;
}
