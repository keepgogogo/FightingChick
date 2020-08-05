package com.hackweek.fightingchick.toolpackage;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.hackweek.fightingchick.StatisticsFragment;
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

    public void updateFocusList(FocusListDataBase dataBase, FocusList... focusLists)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                FocusListDao focusListDao = dataBase.FocusListDao();
                focusListDao.update(focusLists);
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

    public void loadTodayForStatisticsFragment(StatisticsFragment.StatisticsFragmentHandler handler,FocusListDao dao)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                Calendar currentCalendar = Calendar.getInstance();
                SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
                String newDateString = fmt.format(currentCalendar.getTime());
                Message message=new Message();
                message.obj=dao.getByDate(Integer.parseInt(newDateString));
                message.what=StatisticsFragment.RECEIVE_TODAY_FOCUS_LIST;
                handler.sendMessage(message);
            }
        });
    }

    public void loadWeekForStatisticsFragment(StatisticsFragment.StatisticsFragmentHandler handler,FocusListDao dao)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                Calendar currentCalendar = Calendar.getInstance();
                int year=currentCalendar.get(Calendar.YEAR);
                int week=currentCalendar.get(Calendar.WEEK_OF_YEAR);
                Message message=new Message();
                message.obj=dao.getLastWeekEnergyWeekday(year,week);
                message.what=StatisticsFragment.RECEIVE_WEEK_FOCUS_LIST;
                handler.sendMessage(message);
            }
        });
    }

    public void loadMonthForStatisticsFragment(StatisticsFragment.StatisticsFragmentHandler handler,FocusListDao dao)
    {
        thread.execute(new Runnable() {
            @Override
            public void run() {
                Calendar currentCalendar = Calendar.getInstance();
                int year=currentCalendar.get(Calendar.YEAR);
                int month=currentCalendar.get(Calendar.MONTH)+1;
                Message message=new Message();
                message.obj=dao.getLastMonthEnergyDayOfMonth(year,month);
                message.what=StatisticsFragment.RECEIVE_MONTH_FOCUS_LIST;
                handler.sendMessage(message);
            }
        });
    }


}
