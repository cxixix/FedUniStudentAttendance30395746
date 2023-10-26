package au.edu.federation.itech3107.studentattendance30395746.xuesheng;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.databinding.ActivityStudentBinding;
import au.edu.federation.itech3107.studentattendance30395746.data.StudentBean;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDataBase;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class StuXSActivity extends AppCompatActivity {
    private ActivityStudentBinding studentBinding;
    private StudentAdapter ma;
    private int mId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentBinding = ActivityStudentBinding.inflate(getLayoutInflater());
        mId = getIntent().getIntExtra("id", 0);
        setContentView(studentBinding.getRoot());
        initView();
    }

    private void initView() {
        studentBinding.add.setOnClickListener(v -> {
            Intent intent = new Intent(this, TjStudentActivity.class);
            intent.putExtra("id", mId);
            intent.putExtra("add", true);
            startActivity(intent);
        });

        studentBinding.rv.setLayoutManager(new LinearLayoutManager(this));
        ma = new StudentAdapter(R.layout.item_course, new ArrayList<>());
        ma.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                StudentBean studentBean = ma.getData().get(position);
                Intent intent = new Intent(StuXSActivity.this, TjStudentActivity.class);
                intent.putExtra("id", mId);
                intent.putExtra("add", false);
                intent.putExtra("bean", studentBean);
                startActivity(intent);
            }
        });
        studentBinding.rv.setAdapter(ma);
    }

    @Override
    public void onResume() {
        super.onResume();
        searchData();
    }

    private void searchData() {
        UserDataBase.getInstance(this).getStudentDao().getAllUsers(mId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MaybeObserver<List<StudentBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<StudentBean> list) {
                        //Query Results
                        if (list.size() != 0) {
                            ma.setNewData(list);
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
