package au.edu.federation.itech3107.studentattendance30395746.kecheng;

import static au.edu.federation.itech3107.studentattendance30395746.util.ScreenUtils.dp2px;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.util.AppUtils;
import au.edu.federation.itech3107.studentattendance30395746.util.CjData;
import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.data.CourseV2;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395746.util.DialogHelper;
import au.edu.federation.itech3107.studentattendance30395746.util.DialogListener;
import au.edu.federation.itech3107.studentattendance30395746.util.Preferences;
import au.edu.federation.itech3107.studentattendance30395746.util.ScreenUtils;
import au.edu.federation.itech3107.studentattendance30395746.util.TimeUtils;
import au.edu.federation.itech3107.studentattendance30395746.util.Utils;
import au.edu.federation.itech3107.studentattendance30395746.view.CourseView;
import au.edu.federation.itech3107.studentattendance30395746.view.ShowDetailDialog;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

//Timetable
public class CouAArActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView weekAll;
    private int weekCount;
    private String monthXZ;
    private ShowDetailDialog mTkk;
    private CourseView cour;
    private LinearLayout weGp;
    private LinearLayout nGpu;
    private int weAll = 12;
    private int noAll = 11;
    private int noWid = 28;
    private TextView monContent;
    private RecyclerView rv;
    private int weekH;
    private boolean seleShow = false;
    private LinearLayout mLayoutCourse;
    private int mIntentId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        weGp = findViewById(R.id.layout_week_group);
        nGpu = findViewById(R.id.layout_node_group);
        mLayoutCourse = findViewById(R.id.layout_course);
        mIntentId = getIntent().getIntExtra("id", 0);
        monthXZ = getIntent().getStringExtra("date");
        ScreenUtils.init(this);
        Preferences.init(this);
        initToolbar();
        initWeek();
        initCourseView();
        initWeekNodeGroup();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void initWeek() {
        initWeekTitle();
        initSelectWeek();
    }

    private void initSelectWeek() {
        rv = findViewById(R.id.recycler_view_select_week);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rv.getLayoutParams();
        params.topMargin = -dp2px(45);
        rv.setLayoutParams(params);

        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL, false));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 1; i <= 11; i++) {
            strings.add( i + "week");
        }
        SelectWeekAdapter selectWeekAdapter = new SelectWeekAdapter(R.layout.adapter_select_week, strings);
        rv.setAdapter(selectWeekAdapter);
        rv.scrollToPosition(AppUtils.getCurrentWeek(this)-1);

        rv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                weekH = bottom - top;
            }
        });

        selectWeekAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                weekCount = position + 1;

                AppUtils.PreferencesCurrentWeek(CouAArActivity.this, weekCount);
                cour.setCurrentIndex(weekCount);
                cour.resetView();
                weekAll.setText(weekCount + "week");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animSelectWeek(false);
                        AppUtils.updateWidget(getApplicationContext());
                    }
                }, 150);
            }
        });
    }


    private void animSelectWeek(boolean show) {
        seleShow = show;

        int start = 0, end = 0;
        if (show) {
            start = -weekH;
        } else {
            end = -weekH;
        }

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rv.getLayoutParams();
                params.topMargin = (int) animation.getAnimatedValue();
                rv.setLayoutParams(params);
            }
        });
        animator.start();
    }


    private void initWeekTitle() {
        weekAll = findViewById(R.id.tv_toolbar_subtitle);
        weekAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animSelectWeek(!seleShow);
            }
        });
        TextView tvTitle = findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getString(R.string.class_schedule));
    }

    private void initWeekNodeGroup() {
        nGpu.removeAllViews();
        weGp.removeAllViews();

        for (int i = -1; i < 7; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setGravity(Gravity.CENTER);

            textView.setWidth(0);
            textView.setTextColor(getResources().getColor(R.color.primary_text));
            LinearLayout.LayoutParams params;

            if (i == -1) {
                params = new LinearLayout.LayoutParams(
                        Utils.dip2px(getApplicationContext(), noWid),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setTextSize(noAll);
                textView.setText(monthXZ + "\n月");

                monContent = textView;
            } else {
                params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                textView.setTextSize(weAll);
                textView.setText(CjData.WEEK_SINGLE[i]);
            }

            weGp.addView(textView, params);
        }

        int nodeItemHeight = Utils.dip2px(getApplicationContext(), 55);
        for (int i = 1; i <= 16; i++) {
            TextView textView = new TextView(getApplicationContext());
            textView.setTextSize(noAll);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.GRAY);
            textView.setText(String.valueOf(i));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, nodeItemHeight);
            nGpu.addView(textView, params);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCourseView() {
        cour = findViewById(R.id.course_view_v2);
        cour.setCourseItemRadius(3)
                .setTextTBMargin(dp2px(1), dp2px(1));

        cour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("touch");
                return false;
            }
        });
        initCourseViewEvent();
    }

    /**
     * courseVIew Event
     */
    private void initCourseViewEvent() {
        cour.setOnItemClickListener(new CourseView.OnItemClickListener() {
            @Override
            public void onClick(List<CourseAncestor> course, View itemView) {
                mTkk = new ShowDetailDialog();
                mTkk.show(CouAArActivity.this, (CourseV2) course.get(0), new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mTkk = null;
                    }
                });
            }

            @Override
            public void onLongClick(List<CourseAncestor> courses, View itemView) {
                final CourseV2 course = (CourseV2) courses.get(0);
                DialogHelper dialogHelper = new DialogHelper();
                dialogHelper.showNormalDialog(CouAArActivity.this, getString(R.string.confirm_to_delete),
                        "Class 【" + course.getCouName() + "】" + CjData.WEEK[course.getCouWeek()]
                                + "" + course.getCouStartNode() + " ", new DialogListener() {
                            @Override
                            public void onPositive(DialogInterface dialog, int which) {
                                super.onPositive(dialog, which);
                                deleteCancelSnackBar(course);
                            }
                        });
            }

            public void onAdd(CourseAncestor course, View addView) {
                Intent intent = new Intent(CouAArActivity.this, TjCourseActivity.class);
                intent.putExtra(CjData.INTENT_ADD_COURSE_ANCESTOR, course);
                intent.putExtra(CjData.INTENT_ADD, true);
                startActivity(intent);
            }

        });
    }

    /**
     * Undo Delete Tip
     */
    private void deleteCancelSnackBar(final CourseV2 course) {
        course.setDisplayable(false);
        cour.resetView();
        Snackbar.make(monContent, "Delete Success！☆\\(￣▽￣)/", Snackbar.LENGTH_LONG).setAction("undo",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                switch (event) {
                    case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                    case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                    case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                        //removing
                        UserDataBase.getInstance(CouAArActivity.this).getCourseDao().delete(course);
                        cour.resetView();
                        break;
                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                        //cancel
                        course.setDisplayable(true);
                        cour.resetView();
                        break;
                }
            }
        }).show();
    }

    private void updateView() {
        updateCoursePreference();
    }

    @SuppressLint("SetTextI18n")
    public void updateCoursePreference() {
        updateCurrentWeek();
        monContent.setText(monthXZ + "\nmonth");

        //get id
//        long currentCsNameId = Preferences.getLong(
//                getString(R.string.app_preference_current_cs_name_id), 0L);

//        mPresenter.updateCourseViewData(currentCsNameId);
        //Re-query data
        UserDataBase.getInstance(CouAArActivity.this).getCourseDao().getAllUsers(mIntentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<CourseV2>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<CourseV2> list) {
                        //Enquiry into the results
                        if (list.size() != 0) {
                            cour.clear();
                            for (CourseV2 course : list) {
                                if (course.getCouColor() == null || course.getCouColor() == -1) {
                                    course.setCouColor(Utils.getRandomColor());
                                }
                                course.init();
                                cour.addCourse(course);
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void updateCurrentWeek() {
        weekCount = AppUtils.getCurrentWeek(this);
        weekAll.setText( weekCount + "week");
        cour.setCurrentIndex(weekCount);
    }

//    @Override
//    public void initFirstStart() {
//        boolean isFirst = Preferences.getBoolean(getString(R.string.app_preference_app_is_first_start), true);
//        if (!isFirst) {
//            return;
//        }
//        Preferences.putBoolean(getString(R.string.app_preference_app_is_first_start), false);
//
//        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();
//        CourseGroup defaultGroup = groupDao
//                .queryBuilder()
//                .where(CourseGroupDao.Properties.CgName.eq("Default timetable"))
//                .unique();
//
//        long insert;
//        if (defaultGroup == null) {
//            insert = groupDao.insert(new CourseGroup(0L, "default (setting)", ""));
//        } else {
//            insert = defaultGroup.getCgId();
//        }
//
//        //migrate old data
//        AppUtils.copyOldData(this);
//        Preferences.putLong(getString(R.string.app_preference_current_cs_name_id), insert);
//        showOnceSplash();
//    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mTkk != null) {
                    mTkk.dismiss();
                    Log.e("hao", "CouAArActivity onKeyDown()");
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
         super.onBackPressed();
//        moveTaskToBack(false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTkk != null) mTkk.dismiss();
        return super.onTouchEvent(event);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void courseChangeEvent(CourseDataChangeEvent event) {
//        //Update the main interface
//        updateView();
//    }

}
