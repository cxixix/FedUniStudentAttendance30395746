package au.edu.federation.itech3107.studentattendance30395746.xuesheng;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.util.CjData;
import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.kecheng.CheckAdapter;
import au.edu.federation.itech3107.studentattendance30395746.databinding.ActivityCheckBinding;
import au.edu.federation.itech3107.studentattendance30395746.data.ClassBean;
import au.edu.federation.itech3107.studentattendance30395746.data.CourseV2;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395746.util.StringUtil;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class CheckDActivity extends AppCompatActivity {
    private ActivityCheckBinding studentBinding;
    private CheckAdapter ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentBinding = ActivityCheckBinding.inflate(getLayoutInflater());
        setContentView(studentBinding.getRoot());
        initView();
    }

    private void initView() {
        studentBinding.rv.setLayoutManager(new LinearLayoutManager(this));
        studentBinding.rv.setNestedScrollingEnabled(false);

        ad = new CheckAdapter(R.layout.item_check, new ArrayList<>(),  CheckDActivity.this);
        studentBinding.rv.setAdapter(ad);

        CourseV2 bean = (CourseV2) getIntent().getSerializableExtra("bean");
        if (bean != null) {
            UserDataBase.getInstance(this).getCourseDao().getCourseById(bean.getCouId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MaybeObserver<CourseV2>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(CourseV2 list) {
                            //查询到结果
                            if (!StringUtil.isEmpty(list.getJoinClassId())) {
                                CjData.select_class = list.getJoinClassId();
                            }
                            if (!StringUtil.isEmpty(list.getCheckInStudentIds())) {
                                CjData.select_student = list.getCheckInStudentIds();
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

        studentBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bean.setJoinClassId(CjData.select_class);
                bean.setCheckInStudentIds(CjData.select_student);
                Log.e("hao", "CheckDActivity onClick(): "+ CjData.select_student);
                UserDataBase.getInstance(CheckDActivity.this).getCourseDao().update(bean);
                Toast.makeText(CheckDActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

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
                            for (int i = 0; i < list.size(); i++) {
                                if (CjData.select_class.contains(list.get(i).getId() + ",")) {
                                    ad.addData(list.get(i));
                                }
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
}
