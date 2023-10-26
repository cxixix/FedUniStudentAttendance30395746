package au.edu.federation.itech3107.studentattendance30395746.util;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles;
    private List<AppCompatDialogFragment> mFragments;

    /**
     * @param fm
     * @param fragments
     * @param titles Array of text for tab
     */
    public TabViewPagerAdapter(FragmentManager fm , List<AppCompatDialogFragment> fragments , String[] titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    /**
     * If the secondary method is not rewritten here, it can be set in the activity
     *     for (int i = 0; i < mTitle.length; i++) {
     *          mTablayout.getTabAt(i).setText(mTitle[i]).setIcon(pic[i]);
     *         }
     *     This can also set the text and icons on the tab, but if neither method is used, the text and icons on the tab cannot be displayed
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
