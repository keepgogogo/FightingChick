package com.hackweek.fightingchick.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "FocusList")
public class FocusList {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo( name = "date")
    public int date;

    @ColumnInfo( name = "hour")
    public int hour;

    @ColumnInfo( name = "minute")
    public int minute;

    //length of focus time should be saved by minutes
    @ColumnInfo( name = "FocusTime")
    public int FocusTime;

    @ColumnInfo( name = "whatToDo")
    public String whatTodo;

    //notice = 0 提示方式为响铃
    //notice = 1 提示方式为振动
    //notice = 2 提示方式为响铃和振动
    @ColumnInfo( name = "notice")
    public int notice;

    //noticeMusic = 1 音乐为 温和鸡
    //noticeMusic = 2 音乐为 暴躁鸡
    //noticeMusic = 3 音乐为 快乐鸡
    //noticeMusic = 4 音乐为 尖叫鸡
    @ColumnInfo( name = "noticeMusic")
    public int noticeMusic;

    @ColumnInfo( name = "noticeInterval")
    public int noticeInterval;

    //weekday=1 to 7 means Monday to Sunday
    @ColumnInfo( name = "weekday")
    public int weekday;
}
