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
import au.edu.federation.itech3107.studentattendance30395746.data.ClassBean;
import au.edu.federation.itech3107.studentattendance30395746.util.StringUtil;


public class ClassCheckAdapter extends BaseQuickAdapter<ClassBean, BaseViewHolder> {

    public ClassCheckAdapter(int layoutResId, @Nullable List<ClassBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassBean item) {
        helper.setText(R.id.tv_item,item.getName());
        CheckBox view = helper.getView(R.id.cb);
        view.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (!CjData.select_class.contains(item.getId()+",")) {
                        CjData.select_class = CjData.select_class + item.getId() + ",";
                        Log.e("hao", "choose: "+ CjData.select_class);
                    }
                }else {
                    if (CjData.select_class.contains(item.getId()+",")) {
                        String[] split = CjData.select_class.split(",");
                        String newDes = "";
                        for (String i : split){
                            if (!StringUtil.isEmpty(i)){
                                int i1 = Integer.parseInt(i);
                                if (item.getId() != i1){
                                    newDes = newDes + i1+",";
                                }
                            }
                        }
                        CjData.select_class = newDes;
                        Log.e("hao", "unchecked: "+ CjData.select_class);
                    }
                }
            }
        });
        view.setChecked(false);
        String selectClass = CjData.select_class;
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