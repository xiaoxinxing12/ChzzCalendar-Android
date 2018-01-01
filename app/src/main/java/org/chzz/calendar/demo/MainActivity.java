package org.chzz.calendar.demo;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.chzz.calendar.Utils;
import org.chzz.calendar.component.CalendarAttr;
import org.chzz.calendar.component.CalendarViewAdapter;
import org.chzz.calendar.interf.OnSelectDateListener;
import org.chzz.calendar.model.CalendarDate;
import org.chzz.calendar.view.Calendar;
import org.chzz.calendar.view.MonthPager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tvYear;
    TextView tvMonth;
    TextView backToday;
    TextView scrollSwitch;
    TextView themeSwitch;
    TextView nextMonthBtn;
    TextView lastMonthBtn;

    MonthPager monthPager;
    private CalendarDate currentDate;
    CalendarViewAdapter calendarAdapter;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        monthPager = (MonthPager) findViewById(R.id.calendar_view);
        //此处强行setViewHeight，毕竟你知道你的日历牌的高度
        tvYear = (TextView) findViewById(R.id.show_year_view);
        tvMonth = (TextView) findViewById(R.id.show_month_view);
        backToday = (TextView) findViewById(R.id.back_today_button);
        scrollSwitch = (TextView) findViewById(R.id.scroll_switch);
        themeSwitch = (TextView) findViewById(R.id.theme_switch);
        nextMonthBtn = (TextView) findViewById(R.id.next_month);
        lastMonthBtn = (TextView) findViewById(R.id.last_month);
        //monthPager.setViewHeight(Utils.dpi2px(this, 270));
        initCurrentDate();
        initCalendarView();
    }

    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        // tvYear.setText(currentDate.getYear() + "年");
        //tvMonth.setText(currentDate.getMonth() + "");
    }

    /**
     * 初始化CustomDayView，并作为CalendarViewAdapter的参数传入
     */
    private void initCalendarView() {
        CustomDayView customDayView = new CustomDayView(this, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                this,
                onSelectDateListener,
                CalendarAttr.CalendarType.MONTH,
                CalendarAttr.WeekArrayType.Sunday,
                customDayView);
        calendarAdapter.setOnCalendarTypeChangedListener(new CalendarViewAdapter.OnCalendarTypeChanged() {
            @Override
            public void onCalendarTypeChanged(CalendarAttr.CalendarType type) {
                //rvToDoList.scrollToPosition(0);
            }
        });

        initMonthPager();
    }

    private OnSelectDateListener onSelectDateListener = new OnSelectDateListener() {
        @Override
        public void onSelectDate(CalendarDate date) {

        }

        @Override
        public void onSelectOtherMonth(int offset) {
            //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
            monthPager.selectOtherMonth(offset);
        }
    };

    /**
     * 初始化monthPager，MonthPager继承自ViewPager
     *
     * @return void
     */
    private void initMonthPager() {
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    //tvYear.setText(date.getYear() + "年");
                    //tvMonth.setText(date.getMonth() + "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}
