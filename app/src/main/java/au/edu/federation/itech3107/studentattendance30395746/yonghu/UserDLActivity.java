package au.edu.federation.itech3107.studentattendance30395746.yonghu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.databinding.ActivityLoginBinding;
import au.edu.federation.itech3107.studentattendance30395746.data.UserBean;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDataBase;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserDLActivity extends AppCompatActivity {
    private ActivityLoginBinding studentBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(studentBinding.getRoot());
        initView();
    }

    private void initView() {
        //log on
        studentBinding.btnLogin.setOnClickListener(v -> {
            UserDataBase.getInstance(this).getUserDao().getUserByName(studentBinding.etAccount.getText().toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new MaybeObserver<List<UserBean>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<UserBean> list) {
                            //Query Results
                            if (list.size() != 0) {
                                //Verify account
                                UserBean userBean = list.get(0);
                                if (userBean.getPwd().equals(studentBinding.etPassword.getText().toString())) {
                                    startActivity(new Intent(UserDLActivity.this, MainActivity.class));
                                    finish();
                                }else {
                                    //Password error
                                    Toast.makeText(UserDLActivity.this, getString(R.string.passwordNotice), Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                //No such account
                                Toast.makeText(UserDLActivity.this, getString(R.string.passwordNotice), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {
                            //No results found
                            Toast.makeText(UserDLActivity.this, getString(R.string.passwordNotice), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        //register
        studentBinding.btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, UserZcActivity.class));
        });
    }

}
