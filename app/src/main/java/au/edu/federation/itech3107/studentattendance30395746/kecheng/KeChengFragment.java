package au.edu.federation.itech3107.studentattendance30395746.kecheng;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.util.CjData;
import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.databinding.FragmentCourseBinding;
import au.edu.federation.itech3107.studentattendance30395746.data.CourseGroupBean;
import au.edu.federation.itech3107.studentattendance30395746.data.UserDataBase;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * programme management
 */
public class KeChengFragment extends AppCompatDialogFragment{

   private FragmentCourseBinding bind;
   private CourseAdapter mAdapter;
    private String headerText;

    @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      bind = FragmentCourseBinding.inflate(inflater,container,false);
      initView();
      return bind.getRoot();
   }

   private void initView() {
      bind.add.setOnClickListener(v -> {
          showDataPickDialog();
      });

      bind.rv.setLayoutManager(new LinearLayoutManager(getContext()));
      mAdapter = new CourseAdapter(R.layout.item_course, new ArrayList<>());
      mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
         @Override
         public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
             CourseGroupBean courseGroupBean = mAdapter.getData().get(position);
             CjData.selectId = courseGroupBean.getId();
             startActivity(new Intent(getContext(), CouAArActivity.class)
                     .putExtra("id",courseGroupBean.getId())
                     .putExtra("date",courseGroupBean.getDate()));
         }
      });
      bind.rv.setAdapter(mAdapter);
   }

   @Override
   public void onResume() {
      super.onResume();
      UserDataBase.getInstance(getContext()).getCourseGroupDao().getAllUsers()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new MaybeObserver<List<CourseGroupBean>>() {
                 @Override
                 public void onSubscribe(Disposable d) {

                 }

                 @Override
                 public void onSuccess(List<CourseGroupBean> list) {
                    //Enquiry into the results
                    if (list.size() != 0) {
                       mAdapter.setNewData(list);
                       for(int i = 0; i < list.size(); i++) {
                           Log.e("hao", "list: "+list.get(i).toString());
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

   public void showDataPickDialog(){

      long today = MaterialDatePicker.todayInUtcMilliseconds();
      Pair<Long, Long> todayPair = new Pair<>(today, today);
      MaterialDatePicker.Builder<Pair<Long, Long>> builder =
              MaterialDatePicker.Builder.dateRangePicker();
      builder.setSelection(todayPair);
      CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();;
      try {
         builder.setCalendarConstraints(constraintsBuilder.build());
         MaterialDatePicker<?> picker = builder.build();
         picker.addOnPositiveButtonClickListener(selection -> {
             headerText = picker.getHeaderText();
             Pair selection1 = (Pair) selection;
             Object first = selection1.first;
             SimpleDateFormat format = new SimpleDateFormat("MM");
             String format1 = format.format(new Date(Long.parseLong(first + "")));
             CourseGroupBean bean = new CourseGroupBean();
             bean.setName(headerText);
             bean.setDate(format1);
             UserDataBase.getInstance(getContext()).getCourseGroupDao().insert(bean);
             UserDataBase.getInstance(getContext()).getCourseGroupDao().getAllUsers()
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(new MaybeObserver<List<CourseGroupBean>>() {
                         @Override
                         public void onSubscribe(Disposable d) {

                         }

                         @Override
                         public void onSuccess(List<CourseGroupBean> list) {
                             //Enquiry into the results
                             if (list.size() != 0) {
                                 mAdapter.setNewData(list);
                                 for(int i = 0; i < list.size(); i++) {
                                     Log.e("hao", "list: "+list.get(i).toString());
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
         });
         picker.show(getChildFragmentManager(), picker.toString());

      } catch (IllegalArgumentException e) {
      }

   }
}
