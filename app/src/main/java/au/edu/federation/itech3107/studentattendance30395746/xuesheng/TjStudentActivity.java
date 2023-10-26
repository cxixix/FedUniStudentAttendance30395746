package au.edu.federation.itech3107.studentattendance30395746.xuesheng;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import au.edu.federation.itech3107.studentattendance30395746.databinding.ActivityAddStudentBinding;
import au.edu.federation.itech3107.studentattendance30395746.data.StudentBean;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDataBase;
import au.edu.federation.itech3107.studentattendance30395746.util.StringUtil;

public class TjStudentActivity extends AppCompatActivity {

    private ActivityAddStudentBinding studentBinding;
    private int mGetId;
    private StudentBean getBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentBinding = ActivityAddStudentBinding.inflate(getLayoutInflater());
        mGetId = getIntent().getIntExtra("id", 0);
        setContentView(studentBinding.getRoot());
        initView();
    }

    private void initView() {
        boolean add = getIntent().getBooleanExtra("add", false);
        if (!add) {
            getBean = (StudentBean) getIntent().getSerializableExtra("bean");
            if (getBean != null) {
                studentBinding.etName.setText(getBean.getName());
                studentBinding.etNumber.setText(getBean.getNumber()+"");
                studentBinding.btnDelete.setVisibility(View.VISIBLE);
            }else
                studentBinding.btnDelete.setVisibility(View.GONE);
        }else
            studentBinding.btnDelete.setVisibility(View.GONE);
        studentBinding.btnSubmit.setOnClickListener(v -> {
            if (StringUtil.isEmpty(studentBinding.etName.getText().toString()) || StringUtil.isEmpty(studentBinding.etNumber.getText().toString())) {
                Toast.makeText(TjStudentActivity.this, "Not Null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (add) {
                //new and additional
                StudentBean bean1 = new StudentBean();
                bean1.setName(studentBinding.etName.getText().toString());
                bean1.setClassId(mGetId);
                bean1.setNumber(Long.parseLong(studentBinding.etNumber.getText().toString()));
                UserDataBase.getInstance(TjStudentActivity.this).getStudentDao().insert(bean1);
                Toast.makeText(TjStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                //modifications
                getBean.setName(studentBinding.etName.getText().toString());
                getBean.setNumber(Long.parseLong(studentBinding.etNumber.getText().toString()));
                UserDataBase.getInstance(TjStudentActivity.this).getStudentDao().update(getBean);
                Toast.makeText(TjStudentActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        studentBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDataBase.getInstance(TjStudentActivity.this).getStudentDao().delete(getBean);
                finish();
            }
        });
    }
}
