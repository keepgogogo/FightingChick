package com.hackweek.fightingchick.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity( tableName = "GloryAndConfessionRecord")
public class GloryAndConfessionRecord {

    public GloryAndConfessionRecord(int date, String content, boolean gloryOrConfession){
        this.date=date;
        this.content = content;
        this.gloryOrConfession = gloryOrConfession;
    }
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
