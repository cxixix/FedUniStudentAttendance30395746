package au.edu.federation.itech3107.studentattendance30395746.kecheng;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.util.AppUtils;
import au.edu.federation.itech3107.studentattendance30395746.util.CjData;
import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.yonghu.MainActivity;
import au.edu.federation.itech3107.studentattendance30395746.xuesheng.ClassCheckAdapter;
import au.edu.federation.itech3107.studentattendance30395746.data.ClassBean;
import au.edu.federation.itech3107.studentattendance30395746.data.CourseV2;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395746.util.Preferences;
import au.edu.federation.itech3107.studentattendance30395746.util.ScreenUtils;
import au.edu.federation.itech3107.studentattendance30395746.util.StringUtil;
import au.edu.federation.itech3107.studentattendance30395746.view.EditTextLayout;
import au.edu.federation.itech3107.studentattendance30395746.view.PopupWindowDialog;
import au.edu.federation.itech3107.studentattendance30395746.xuesheng.CheckDActivity;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


//Add a course
public class TjCourseActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean isAdd = true;

    private CourseAncestor courseData;
    private CourseV2 courseDataTwo;

    private ImageView mAdd;
    private LinearLayout mContainer;
    private ImageView ivSu;
    private EditTextLayout eName;
    private EditTextLayout eTea;
    private LinearLayout ll_class;
    private RecyclerView rv;
    private Button addClick;
    private ClassCheckAdapter mClassAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        handleIntent();
        initView();

        initListener();
    }

    private void initView() {
        eName = findViewById(R.id.etl_name);
        eTea = findViewById(R.id.etl_teacher);

        mAdd = findViewById(R.id.iv_add_location);
        mContainer = findViewById(R.id.layout_location_container);
        ivSu = findViewById(R.id.iv_submit);
        ll_class = findViewById(R.id.ll_class);
        addClick = findViewById(R.id.btn_add);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setNestedScrollingEnabled(false);
        mClassAdapter = new ClassCheckAdapter(R.layout.item_check_course, new ArrayList<>());
        rv.setAdapter(mClassAdapter);
        ivSu.setImageResource(R.drawable.ic_done_black_24dp);
        addLocation(false);

        addClick.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).putExtra("type", 1));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserDataBase.getInstance(this).getClassDao().getAllUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<ClassBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ClassBean> list) {
                        //Enquiry into the results
                        if (list.size() != 0) {
                            mClassAdapter.setNewData(list);
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

    private void initListener() {
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addLocation(true);
                startActivity(new Intent(TjCourseActivity.this, CheckDActivity.class)
                        .putExtra("bean", courseDataTwo));
            }
        });
        ivSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        courseData = (CourseAncestor) intent.getSerializableExtra(CjData.INTENT_ADD_COURSE_ANCESTOR);
        if (courseData != null) {
            isAdd = true;
        } else {
            courseDataTwo = (CourseV2) intent.getSerializableExtra(CjData.INTENT_EDIT_COURSE);
            if (courseDataTwo != null) {
                isAdd = false; //is edit mode
                courseDataTwo.init();// Clicking on it from the desktop is bound to have initialised it, but not necessarily from any other location.
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    private void submit() {
        //name
        String name = eName.getText().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(TjCourseActivity.this, "Not Null", Toast.LENGTH_SHORT).show();
            return;
        }

        //teacher
        String teacher = eTea.getText().trim();
        //group

        long couCgId = Preferences.getLong(getString(R.string.app_preference_current_cs_name_id), 0);
        int childCount = mContainer.getChildCount();
        boolean hasLocation = false;
        for (int i = 0; i < childCount; i++) {
            View locationItem = mContainer.getChildAt(i);
            Object obj = locationItem.getTag();

            if (obj != null) {
                hasLocation = true;
                CourseV2 courseV2 = (CourseV2) obj;
                courseV2.setCouName(name);
                courseV2.setCouTeacher(teacher);
                courseV2.setGroupId(CjData.selectId);

                if (isAdd || courseV2.getCouId() == 0) {
                    courseV2.setCouCgId(couCgId);
                    courseV2.setJoinClassId(CjData.select_class);
                    courseV2.init();
                    //Insert course
                    UserDataBase.getInstance(this).getCourseDao().insert(courseV2);
                } else {
                    courseV2.setJoinClassId(CjData.select_class);
                    courseV2.init();
                    //Updated Courses
                    UserDataBase.getInstance(this).getCourseDao().update(courseV2);
                }

            }
        }
        if (!hasLocation) {
            Toast.makeText(TjCourseActivity.this, "Time Is Null", Toast.LENGTH_SHORT).show();
        }

        if (isAdd) {
            Toast.makeText(TjCourseActivity.this, "Success", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(TjCourseActivity.this, "Success", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void addLocation(boolean closeable) {
        final LinearLayout locationItem = (LinearLayout) View.inflate(this,
                R.layout.layout_location_item, null);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = ScreenUtils.dp2px(8);

        if (closeable) {
            locationItem.findViewById(R.id.iv_clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContainer.removeView(locationItem);
                }
            });

            initEmptyLocation(locationItem);

        } else {// Create default class times and locations
            locationItem.findViewById(R.id.iv_clear).setVisibility(View.INVISIBLE);

            if (courseData != null) {
                // Tap the screen over

                CourseV2 defaultCourse = new CourseV2().setCouOnlyIdR(AppUtils.createUUID())
                        .setCouAllWeekR(CjData.DEFAULT_ALL_WEEK)
                        .setCouWeekR(courseData.getRow())
                        .setCouStartNodeR(courseData.getCol())
                        .setCouNodeCountR(courseData.getRowNum())
                        .init();

                initNodeInfo(locationItem, defaultCourse);
            } else if (courseDataTwo != null) {
                // Edit this
                initNodeInfo(locationItem, courseDataTwo);

                eName.setText(courseDataTwo.getCouName());
                eTea.setText(courseDataTwo.getCouTeacher());
                CjData.select_class = "";
                String joinClassId = courseDataTwo.getJoinClassId();
                if (!StringUtil.isEmpty(joinClassId)) {
                    CjData.select_class = joinClassId;
                }
            } else {
                //
                initEmptyLocation(locationItem);
                mAdd.setVisibility(View.GONE);
            }
        }

        locationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLocationItem(locationItem);
            }
        });

        mContainer.addView(locationItem, params);
    }

    private void initEmptyLocation(LinearLayout locationItem) {
        CourseV2 defaultCourse = new CourseV2().setCouOnlyIdR(AppUtils.createUUID())
                .setCouAllWeekR(CjData.DEFAULT_ALL_WEEK)
                .setCouAllWeekR(1 + "")
                .setCouStartNodeR(1)
                .setCouNodeCountR(1);
        initNodeInfo(locationItem, defaultCourse);
    }

    private void initNodeInfo(LinearLayout locationItem, CourseV2 courseV2) {
        TextView tvText = locationItem.findViewById(R.id.tv_text);
        String builder = CjData.WEEK_SINGLE[courseV2.getCouWeek() - 1] + "Week " +
                " Section" + courseV2.getCouStartNode() + "-" +
                (courseV2.getCouStartNode() + courseV2.getCouNodeCount() - 1);
        tvText.setText(builder);

        locationItem.setTag(courseV2);
    }

    private void clickLocationItem(final LinearLayout locationItem) {
        PopupWindowDialog dialog = new PopupWindowDialog();

        CourseV2 courseV2 = null;
        Object obj = locationItem.getTag();
        // has tag data
        if (obj != null && obj instanceof CourseV2) {
            courseV2 = (CourseV2) obj;
        } else {
            throw new RuntimeException("Course data tag not be found");
        }

        dialog.showSelectTimeDialog(this, courseV2, new PopupWindowDialog.SelectTimeCallback() {
            @Override
            public void onSelected(CourseV2 course) {
                StringBuilder builder = new StringBuilder();
                builder.append("weekly").append(CjData.WEEK_SINGLE[course.getCouWeek() - 1])
                        .append(" The").append(course.getCouStartNode()).append("-")
                        .append(course.getCouStartNode() + course.getCouNodeCount() - 1).append("section");
                if (!TextUtils.isEmpty(course.getCouLocation())) {
                    builder.append("【").append(course.getCouLocation()).append("】");
                }

                ((TextView) locationItem.findViewById(R.id.tv_text))
                        .setText(builder.toString());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
