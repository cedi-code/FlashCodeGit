package ch.appquest.boredboizz.flashcode;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Girardin on 27.12.2017.
 */

// receiverFragment ist noch lehr wenn du willst kannst du objekte oder sogar Fragments hinzufügen
public class receiverFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.receiver_layout, container, false);

        return myFragmentView;
    }
}
