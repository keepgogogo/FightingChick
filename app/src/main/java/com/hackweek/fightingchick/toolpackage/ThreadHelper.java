package com.hackweek.fightingchick.toolpackage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hackweek.fightingchick.database.FocusList;
import com.hackweek.fightingchick.database.FocusListDao;
import com.hackweek.fightingchick.database.FocusListDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionDao;
import com.hackweek.fightingchick.database.GloryAndConfessionDataBase;
import com.hackweek.fightingchick.database.GloryAndConfessionRecord;

import java.util.concurrent.TimeUnit;

public class ThreadHelper implements ThreadHelperInterface{

    /**
     * have a thread pool built
     */
    private ThreadFactory threadFactory=new ThreadFactoryBuilder().setNameFormat("poolForTime_%d").build();

    /**
     * initialization of parameters of ThreadPoolExecutor
     */
    private final static int CORE_POOL_SIZE =2;
    private final static int MAXIMUM_POOL_SIZE =2;
    private final static long KEEP_ALIVE_TIME =0L;
    private final static int CAPACITY_OF_BLOCKING_QUEUE =128;

    /**
     * have a single thread built based on a thread pool
     */
    private ExecutorService thread=new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>(CAPACITY_OF_BLOCKING_QUEUE),
            threadFactory,new ThreadPoolExecutor.AbortPolicy());

    public void insertFocusList(FocusListDataBase dataBase, FocusList ... focusLists)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                FocusListDao focusListDao=dataBase.FocusListDao();
                focusListDao.insert(focusLists);
            }
        });
    }

    public void deleteFocusList(FocusListDataBase dataBase, FocusList ... focusLists)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                FocusListDao focusListDao=dataBase.FocusListDao();
                focusListDao.delete(focusLists);
            }
        });
    }

    public void insertGloryAndConfession(GloryAndConfessionDataBase dataBase, GloryAndConfessionRecord ... records)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                GloryAndConfessionDao gloryAndConfessionDao=dataBase.GloryAndConfessionDao();
                gloryAndConfessionDao.insert(records);
            }
        });
    }

    public void deleteGloryAndConfession(GloryAndConfessionDataBase dataBase, GloryAndConfessionRecord ... records)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                GloryAndConfessionDao gloryAndConfessionDao=dataBase.GloryAndConfessionDao();
                gloryAndConfessionDao.delete(records);
            }
        });
    }


}
