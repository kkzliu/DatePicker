package cn.aigestudio.datepicker.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.aigestudio.datepicker.R;
import cn.aigestudio.datepicker.bizs.decors.DPDecor;
import cn.aigestudio.datepicker.bizs.languages.DPLManager;
import cn.aigestudio.datepicker.bizs.themes.DPTManager;
import cn.aigestudio.datepicker.cons.ActionMode;
import cn.aigestudio.datepicker.cons.DPMode;
import cn.aigestudio.datepicker.utils.MeasureUtil;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * DatePicker
 *
 * @author AigeStudio 2015-06-29
 */
public class DatePicker extends LinearLayout {
    private DPTManager mTManager;// 主题管理器
    private DPLManager mLManager;// 语言管理器

    private MonthView monthView;// 月视图
    private TextView tvYear, tvMonth;// 年份 月份显示
    private TextView tvRight,tvLeft;// 左右确定按钮显示


    private OnDateSelectedListener onDateSelectedListener;// 日期多选后监听
    private OnMonthChangeListener mOnMonthChangeListener;

    /**
     * 日期单选监听器
     */
    public interface OnDatePickedListener {
        void onDatePicked(String date);
    }

    /**
     * 日期多选监听器
     */
    public interface OnDateSelectedListener {
        void onDateSelected(List<String> date);
    }

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTManager = DPTManager.getInstance();
        mLManager = DPLManager.getInstance();

        // 设置排列方向为竖向
        setOrientation(VERTICAL);

        LayoutParams llParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        // 标题栏根布局
        RelativeLayout rlTitle = new RelativeLayout(context);
        rlTitle.setBackgroundColor(mTManager.colorTitleBG());
        int rlTitlePadding = MeasureUtil.dp2px(context, 10);
        rlTitle.setPadding(rlTitlePadding, rlTitlePadding, rlTitlePadding, rlTitlePadding);
        LinearLayout llTitle = new LinearLayout(context);
        llTitle.setOrientation(HORIZONTAL);

        // 周视图根布局
        LinearLayout llWeek = new LinearLayout(context);
        llWeek.setBackgroundColor(mTManager.colorTitleBG());
        llWeek.setOrientation(HORIZONTAL);
        int llWeekPadding = MeasureUtil.dp2px(context, 5);
        llWeek.setPadding(0, llWeekPadding, 0, llWeekPadding);
        LayoutParams lpWeek = new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpWeek.weight = 1;

        // 标题栏子元素布局参数
        RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(WRAP_CONTENT,WRAP_CONTENT);
        lpTitle.addRule(RelativeLayout.CENTER_IN_PARENT);
        LayoutParams lpYear =
                new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

        LayoutParams lpMonth =
                new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        RelativeLayout.LayoutParams lpRight =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpRight.addRule(RelativeLayout.CENTER_VERTICAL);
        lpRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayout.LayoutParams lpLeft =
                new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        lpLeft.addRule(RelativeLayout.CENTER_VERTICAL);
        lpLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        // -------------------------------------------------------------------------------标题栏
        // 年份显示
        tvYear = new TextView(context);
        tvYear.setText("2015年");
        tvYear.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvYear.setTextColor(mTManager.colorTitle());

        // 月份显示
        tvMonth = new TextView(context);
        tvMonth.setText("六月");
        tvMonth.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        tvMonth.setTextColor(mTManager.colorTitle());


        // 右侧
        tvRight = new TextView(context);
        tvRight.setBackgroundResource(R.drawable.icon_right);
//        tvRight.setText(mLManager.titleEnsure());
//        tvRight.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
//        tvRight.setTextColor(mTManager.colorTitle());
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monthView!=null){
                    monthView.next();
                }
            }
        });

        //左侧
        tvLeft = new TextView(context);
        tvLeft.setBackgroundResource(R.drawable.icon_left);
        tvLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(monthView!=null){
                    monthView.previous();
                }
            }
        });


        llTitle.addView(tvYear, lpYear);
        llTitle.addView(tvMonth, lpMonth);
        rlTitle.addView(llTitle,lpTitle);
        rlTitle.addView(tvRight, lpRight);
        rlTitle.addView(tvLeft,lpLeft);

        addView(rlTitle, llParams);

        //横线
        View line = new View(context);
        LayoutParams lpLine = new LayoutParams(MATCH_PARENT, MeasureUtil.dp2px(context,0.2f));
        line.setBackgroundColor(Color.parseColor("#9e9e9e"));
        line.setLayoutParams(lpLine);
        addView(line);

        // --------------------------------------------------------------------------------周视图
        for (int i = 0; i < mLManager.titleWeek().length; i++) {
            TextView tvWeek = new TextView(context);
            tvWeek.setText(mLManager.titleWeek()[i]);
            tvWeek.setGravity(Gravity.CENTER);
            tvWeek.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            if(mLManager.titleWeek()[i].equals("六")||mLManager.titleWeek()[i].equals("日")){
                tvWeek.setTextColor(mTManager.colorWeekendTitle());
            }else
                tvWeek.setTextColor(mTManager.colorTitle());

            llWeek.addView(tvWeek, lpWeek);
        }
        addView(llWeek, llParams);

        // ------------------------------------------------------------------------------------月视图
        monthView = new MonthView(context);
        monthView.setOnDateChangeListener(new MonthView.OnDateChangeListener() {
            @Override
            public void onMonthChange(int month) {
                tvMonth.setText(mLManager.titleMonth()[month - 1]);
                //年份切换的时候会自动调用月份切换，所以不需要在年份切换里加监听
                if(mOnMonthChangeListener!=null)
                    mOnMonthChangeListener.onMonthChange(tvYear.getText().toString()+"-"+(month));
            }

            @Override
            public void onYearChange(int year) {
                String tmp = String.valueOf(year);
                if (tmp.startsWith("-")) {
                    tmp = tmp.replace("-", mLManager.titleBC());
                }
                tvYear.setText(tmp+"年");
            }
        });
        addView(monthView, llParams);


    }

    /**
     * 设置初始化年月日期
     *
     * @param year  ...
     * @param month ...
     */
    public void setDate(int year, int month) {
        if (month < 1) {
            month = 1;
        }
        if (month > 12) {
            month = 12;
        }
        monthView.setDate(year, month);
    }

    /**
     * 获取选择的日期列表
     */
    public void onDateSelected(){
        if (null != onDateSelectedListener) {
            onDateSelectedListener.onDateSelected(monthView.getDateSelected());
        }
    }

    /**
     * 清空已经选择的日期
     */
    public void clearDateSelected(){
        if(monthView!=null){
            monthView.clearDateSelected();
        }
    }

    public void setDPDecor(DPDecor decor) {
        monthView.setDPDecor(decor);
    }

    /**
     * 设置日期选择模式
     *
     * @param mode ...
     */
    public void setMode(DPMode mode) {
        if (mode != DPMode.MULTIPLE) {
            tvRight.setVisibility(GONE);
        }
        monthView.setDPMode(mode);
    }

    /**
     * 设置日历的滑动模式
     * @param mode
     */
    public void setActionMode(ActionMode mode){
        monthView.setActionMode(mode);
    }
    public void setFestivalDisplay(boolean isFestivalDisplay) {
        monthView.setFestivalDisplay(isFestivalDisplay);
    }

    public void setTodayDisplay(boolean isTodayDisplay) {
        monthView.setTodayDisplay(isTodayDisplay);
    }

    public void setHolidayDisplay(boolean isHolidayDisplay) {
        monthView.setHolidayDisplay(isHolidayDisplay);
    }

    public void setDeferredDisplay(boolean isDeferredDisplay) {
        monthView.setDeferredDisplay(isDeferredDisplay);
    }

    /**
     * 设置单选监听器
     *
     * @param onDatePickedListener ...
     */
    public void setOnDatePickedListener(OnDatePickedListener onDatePickedListener) {
        if (monthView.getDPMode() != DPMode.SINGLE) {
            throw new RuntimeException(
                    "Current DPMode does not SINGLE! Please call setMode set DPMode to SINGLE!");
        }
        monthView.setOnDatePickedListener(onDatePickedListener);
    }


    /**
     * 设置多选监听器
     *
     * @param onDateSelectedListener ...
     */
    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        if (monthView.getDPMode() != DPMode.MULTIPLE) {
            throw new RuntimeException(
                    "Current DPMode does not MULTIPLE! Please call setMode set DPMode to MULTIPLE!");
        }
        this.onDateSelectedListener = onDateSelectedListener;
    }

    public void setOnMonthChangeListener(OnMonthChangeListener onMonthChangeListener){
        this.mOnMonthChangeListener = onMonthChangeListener;
    }

    /**
     * 在月份切换的时候调用（MonthView中年切换的时候月份切换会调用）
     * 返回格式2016-5
     */
    public interface OnMonthChangeListener {
        void onMonthChange(String date);
    }
}
