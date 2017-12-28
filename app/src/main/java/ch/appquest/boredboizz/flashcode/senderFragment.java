package ch.appquest.boredboizz.flashcode;

import android.os.Bundle;
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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.send_layout, container, false);
        // initzialisiert das ganze Layout
        final MainActivity main = (MainActivity) getActivity();

        // die Toolbar ist die Bar oben wo man von Text zu Manuel wechseln kann
        toolbar = (Toolbar) myFragmentView.findViewById(R.id.toolbar);
        main.setSupportActionBar(toolbar);

        // der Titel der Applikation (dort wo Flash code steht)
        main.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // erstellt die Tabs
        viewPager = (ViewPager) myFragmentView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // macht ein Layout für die Tabs
        tabLayout = (TabLayout) myFragmentView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return myFragmentView;
    }

    // erstellt die Beiden Tabs Text und Manual
    private void setupViewPager(ViewPager viewPager) {
        final MainActivity main = (MainActivity) getActivity();
        ViewPagerAdapter adapter = new ViewPagerAdapter(main.getSupportFragmentManager());
        // hier werden die Tabs hinzugefügt
        adapter.addFragment(new TextTransmitFragment(), "Text");
        adapter.addFragment(new ButtonTransmitFragment(), "Manual");
        viewPager.setAdapter(adapter);
    }
}
