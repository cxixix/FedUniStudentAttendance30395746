package au.edu.federation.itech3107.studentattendance30395746.yonghu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import au.edu.federation.itech3107.studentattendance30395746.R;
import au.edu.federation.itech3107.studentattendance30395746.kecheng.KeChengFragment;
import au.edu.federation.itech3107.studentattendance30395746.xuesheng.BanJiFragment;
import au.edu.federation.itech3107.studentattendance30395746.util.TabViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private List<AppCompatDialogFragment> mFragments;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(new KeChengFragment());
        mFragments.add(new BanJiFragment());
        ViewPager vp = findViewById(R.id.vp);
        TabLayout tab = findViewById(R.id.tab);
        String[] mTitle = new String[]{"", ""};
        TabViewPagerAdapter adapter = new TabViewPagerAdapter(getSupportFragmentManager(), mFragments, mTitle);
        vp.setAdapter(adapter);
        tab.setupWithViewPager(vp);
        tab.getTabAt(0).setText(mTitle[0]).setIcon(R.mipmap.cla);
        tab.getTabAt(1).setText(mTitle[1]).setIcon(R.mipmap.ban);

        int type = getIntent().getIntExtra("type", 0);
        vp.setCurrentItem(type);
    }
}