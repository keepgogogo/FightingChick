package com.hackweek.fightingchick.toolpackage;

import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionRecord;

public interface ThreadHelperInterface {

    //通过这个方法插入 专注任务 ，参数为数据库实例以及不限个数的专注任务实例
    void insertFocusList(FocusListDataBase dataBase, FocusList... focusLists);
    //通过这个方法删除  专注任务， 参数为数据库实例以及不限个数的专注任务实例
    void deleteFocusList(FocusListDataBase dataBase, FocusList... focusLists);
    //通过这个方法插入  光荣录或忏悔录， 参数为数据库实例以及不限个数的光荣录或忏悔录实例
    void insertGloryAndConfession(GloryAndConfessionDataBase dataBase, GloryAndConfessionRecord ... records);
    //通过这个方法删除  光荣录或忏悔录， 参数为数据库实例以及不限个数的光荣录或忏悔录实例
    void deleteGloryAndConfession(GloryAndConfessionDataBase dataBase, GloryAndConfessionRecord ... records);
}
