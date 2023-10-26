package au.edu.federation.itech3107.studentattendance30395746.yonghu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.databinding.ActivityRegisterBinding;
import au.edu.federation.itech3107.studentattendance30395746.data.UserBean;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDao;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395746.util.StringUtil;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserZcActivity extends AppCompatActivity {

    private int biaozhi = 1;
    private ActivityRegisterBinding studentBinding;
    private UserDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(studentBinding.getRoot());
        dao = UserDataBase.getInstance(this).getUserDao();
        initView();
    }

    private void initView() {
        //Registration
        studentBinding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verify that the input box is empty
                if (StringUtil.isEmpty(studentBinding.etNick.getText().toString())) {
                    Toast.makeText(UserZcActivity.this, getString(R.string.nick) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isEmpty(studentBinding.etAccount.getText().toString())) {
                    Toast.makeText(UserZcActivity.this, getString(R.string.account) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isEmpty(studentBinding.etPassword.getText().toString())) {
                    Toast.makeText(UserZcActivity.this, getString(R.string.password) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isEmpty(studentBinding.etPassword2.getText().toString())) {
                    Toast.makeText(UserZcActivity.this, getString(R.string.password) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!studentBinding.etPassword.getText().toString().equals(studentBinding.etPassword2.getText().toString())) {
                    Toast.makeText(UserZcActivity.this, getString(R.string.password_not_again), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (StringUtil.isEmpty(studentBinding.etMobile.getText().toString())) {
                    Toast.makeText(UserZcActivity.this, getString(R.string.mobile) + getString(R.string.notNull), Toast.LENGTH_SHORT).show();
                    return;
                }
                //Check for duplicate accounts
                dao.getUserByName(studentBinding.etAccount.getText().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new MaybeObserver<List<UserBean>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<UserBean> list) {
                                //Enquiry into the results
                                if (list != null && list.size() != 0) {
                                    //Duplicate accounts for alerts
                                    Toast.makeText(UserZcActivity.this, getString(R.string.accountNotice), Toast.LENGTH_SHORT).show();
                                } else {
                                    //Save data
                                    insertData();
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("hao", "TaskDetailActivity onError(): " + e.toString());
                            }

                            @Override
                            public void onComplete() {
                                //No result for enquiry
                                insertData();
                            }
                        });
            }
        });


//        inflate.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int checkid) {
//                switch (checkid) {
//                    case R.id.rb_1:
//                        au = 1;
//                        break;
//                    case R.id.rb_2:
//                        au = 0;
//                        break;
//                }
//            }
//        });
    }

    private void insertData() {
        //Database insertion of data
        UserBean userBean = new UserBean();
        userBean.setName(studentBinding.etAccount.getText().toString());
        userBean.setPwd(studentBinding.etPassword.getText().toString());
        userBean.setNick(studentBinding.etNick.getText().toString());
        userBean.setMobile(studentBinding.etMobile.getText().toString());
        userBean.setAuthon(biaozhi);
        dao.insert(userBean);
        Toast.makeText(UserZcActivity.this, getString(R.string.registerNotice), Toast.LENGTH_SHORT).show();
        finish();
    }

}