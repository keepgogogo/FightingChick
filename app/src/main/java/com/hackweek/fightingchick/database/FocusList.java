package com.hackweek.fightingchick.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.Nonnull;

@Entity(tableName = "FocusList")
public class FocusList implements Serializable {

    public FocusList(int date, int hour, int minute, int FocusTime,
                     String whatTodo, int notice, int noticeMusic,
                     int noticeInterval, int weekday ,int energyValue,
                     int year,int month,int weekOfYear,int dayOfMonth,
                     int timeRung) {
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.FocusTime = FocusTime;
        this.whatTodo = whatTodo;
        this.notice = notice;
        this.noticeMusic = noticeMusic;
        this.noticeInterval = noticeInterval;
        this.weekday = weekday;
        this.identifier = UUID.randomUUID().toString();
        this.energyValue = energyValue;
        this.year=year;
        this.month=month;
        this.weekOfYear = weekOfYear;
        this.dayOfMonth = dayOfMonth;
        this.timeRung = timeRung;
    }


    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo
    public int date;
    @ColumnInfo
    public int hour;
    @ColumnInfo
    public int minute;
    //length of focus time should be saved by minutes
    @ColumnInfo(name = "FocusTime")
    public int FocusTime;

    @ColumnInfo(name = "whatToDo")
    public String whatTodo;

    //notice = 0 提示方式为响铃
    //notice = 1 提示方式为振动
    //notice = 2 提示方式为响铃和振动
    @ColumnInfo
    public int notice;

    //noticeMusic = 1 音乐为 温和鸡
    //noticeMusic = 2 音乐为 暴躁鸡
    //noticeMusic = 3 音乐为 快乐鸡
    //noticeMusic = 4 音乐为 尖叫鸡
    @ColumnInfo
    public int noticeMusic;

    @ColumnInfo
    public int noticeInterval;

    //weekday=1 to 7 means Sunday to Monday
    @ColumnInfo
    public int weekday;

    //save the UUID for alarm intent use
    @ColumnInfo
    @Nonnull
    public String identifier;

    @ColumnInfo
    public int energyValue;

    @ColumnInfo
    public int year;

    @ColumnInfo
    public int month;


    @ColumnInfo
    public int weekOfYear;


    @ColumnInfo
    public int dayOfMonth;

    @ColumnInfo
    public int timeRung;//响铃几次

}
