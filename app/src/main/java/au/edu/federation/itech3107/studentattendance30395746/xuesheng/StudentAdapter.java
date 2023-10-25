package au.edu.federation.itech3107.studentattendance30395746.xuesheng;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.data.StudentBean;



public class StudentAdapter extends BaseQuickAdapter<StudentBean, BaseViewHolder> {

    public StudentAdapter(int layoutResId, @Nullable List<StudentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudentBean item) {
        helper.setText(R.id.tv_item,"Name: "+item.getName());
    }
}
