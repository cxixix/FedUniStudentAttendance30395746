package au.edu.federation.itech3107.studentattendance30395746.kecheng;


import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.data.CourseGroupBean;



public class CourseAdapter extends BaseQuickAdapter<CourseGroupBean, BaseViewHolder> {

    public CourseAdapter(int layoutResId, @Nullable List<CourseGroupBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseGroupBean item) {
        helper.setText(R.id.tv_item,item.getName());
    }
}
