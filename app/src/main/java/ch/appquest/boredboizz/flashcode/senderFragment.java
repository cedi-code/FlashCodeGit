package ch.appquest.boredboizz.flashcode;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Girardin on 27.12.2017.
 */

public class senderFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public ViewPagerAdapter adapter;
    private View myFragmentView;

    private TextTransmitFragment textTransmit;
    private ButtonTransmitFragment buttonTransmit;
    private inProgressFragment progress;
    private Camera2BasicFragment camera2Basic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        textTransmit = new TextTransmitFragment();
        buttonTransmit = new ButtonTransmitFragment();
        progress = new inProgressFragment();
        camera2Basic = Camera2BasicFragment.newInstance();
        // Inflate the layout for this fragment
        myFragmentView = inflater.inflate(R.layout.send_layout, container, false);
        // initzialisiert das ganze Layout
        final MainActivity main = (MainActivity) getActivity();

        // die Toolbar ist die Bar oben wo man von Text zu Manuel wechseln kann
        toolbar = (Toolbar) myFragmentView.findViewById(R.id.toolbar);
        main.setSupportActionBar(toolbar);

        // der Titel der Applikation (dort wo Flash code steht)
        main.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // erstellt die Tabs
        viewPager = (ViewPager) myFragmentView.findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
            @Override
            public void onPageSelected(int position) {
                checkFragment(position);
            }

        });
        adapter = new ViewPagerAdapter(main.getSupportFragmentManager());
        setupViewPager(viewPager);

        // macht ein Layout für die Tabs
        tabLayout = (TabLayout) myFragmentView.findViewById(R.id.tabs);

        tabLayout.setupWithViewPager(viewPager);

        return myFragmentView;
    }

    // erstellt die Beiden Tabs Text und Manual
    private void setupViewPager(ViewPager viewPager) {


        // hier werden die Tabs hinzugefügt
        adapter.addFragment(textTransmit, "Text");
        adapter.addFragment(buttonTransmit, "Button");
        viewPager.setAdapter(adapter);
    }
    public void changeViewPager(boolean isTransmitPage) {

            adapter.removeAllFragment();
            if(isTransmitPage) {
                adapter.addFragment(textTransmit, "Text");
                adapter.addFragment(buttonTransmit, "Button");
            }else {
                adapter.addFragment(camera2Basic, "Camera");
                adapter.addFragment(progress, "Touch");
            }
            checkFragment(viewPager.getCurrentItem());
            // adapter.notifyDataSetChanged();
            //https://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
            viewPager.getAdapter().notifyDataSetChanged();
            tabLayout.setupWithViewPager(viewPager);
            // viewPager.setAdapter(adapter);
    }
    private void checkFragment(int pos) {
        if(adapter.getItem(pos).getClass().getSimpleName().equals("Camera2BasicFragment")) {

            scrollDown(false);
        }else {
            /*if(cameraReceive.getIsPlaying()){
                cameraReceive.stopPlay();
            }*/

            scrollDown(true);
        }
    }
    private void scrollDown(boolean on) {
        AppBarLayout appBarLayout = (AppBarLayout) myFragmentView.findViewById(R.id.BarLayoutSend);
        appBarLayout.setExpanded(on, true);

    }
}
