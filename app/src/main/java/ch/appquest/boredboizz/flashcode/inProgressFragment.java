package ch.appquest.boredboizz.flashcode;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Girardin on 24.03.2018.
 */

public class inProgressFragment extends android.support.v4.app.Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.in_progress, container, false);
        // initzialisiert die Objekte im Fragment mit onklicks

        return myFragmentView;
    }
}
