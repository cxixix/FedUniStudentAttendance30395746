package au.edu.federation.itech3107.studentattendance30395746.xuesheng;


import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.util.CjData;
import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.data.StudentBean;
import au.edu.federation.itech3107.studentattendance30395746.util.StringUtil;


public class StudentCheckAdapter extends BaseQuickAdapter<StudentBean, BaseViewHolder> {

    public StudentCheckAdapter(int layoutResId, @Nullable List<StudentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentBean item) {
        helper.setText(R.id.tv_item,item.getName());
        CheckBox view = helper.getView(R.id.cb);
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int id = item.getId();
                if (b) {
                    if (!CjData.select_student.contains(id+",")) {
                        CjData.select_student = CjData.select_student + id + ",";
                        Log.e("hao", "choose: "+ CjData.select_student);
                    }
                }else {
                    if (CjData.select_student.contains(id+",")) {
                        String[] split = CjData.select_student.split(",");
                        String newDes = "";
                        for (String i : split){
                            if (!StringUtil.isEmpty(i)){
                                int i1 = Integer.parseInt(i);
                                if (id != i1){
                                    newDes = newDes + i1+",";
                                }
                            }
                        }
                        CjData.select_student = newDes;
                        Log.e("hao", "Uncheck: "+ CjData.select_student);
                    }
                }
            }
        });
        view.setChecked(false);
        String selectClass = CjData.select_student;
        if (!StringUtil.isEmpty(selectClass)) {
            String[] split = selectClass.split(",");
            for (String s : split) {
                if (!StringUtil.isEmpty(s)) {
                    if ((s+",").equals(item.getId()+",")) {
                        view.setChecked(true);
                    }
                }
            }
        }
    }
}
