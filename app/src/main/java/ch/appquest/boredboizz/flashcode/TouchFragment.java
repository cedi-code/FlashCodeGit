package ch.appquest.boredboizz.flashcode;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Girardin on 24.03.2018.
 */

public class TouchFragment extends android.support.v4.app.Fragment {
    private Button switchButton;
    private AutoCompleteTextView texview;
    private int test = 0;
    private boolean pause;
    private boolean isStarted = false;
    private ArrayList<Integer[]> msgList = new ArrayList<>();

    private final Handler mHandler = new Handler();

    private final Runnable mUpdateUI = new Runnable() {
        public void run() {
            test++;
            mHandler.postDelayed(mUpdateUI, 500
            ); // 0.5 second
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.touch_receive, container, false);
        // initzialisiert die Objekte im Fragment mit onklicks
        init(myFragmentView);
        return myFragmentView;
    }
    private void init(View v) {
        switchButton = (Button) v.findViewById(R.id.touchButton);
        texview = (AutoCompleteTextView) v.findViewById(R.id.textResult);
        texview.setEnabled(false);
        texview.setText("test1234");
        mHandler.post(mUpdateUI);
        final MainActivity main = (MainActivity) getActivity();
        final GradientDrawable drawable = (GradientDrawable)switchButton.getBackground();

        switchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // turn on flash
                        switchButton.setTextColor(ContextCompat.getColor(getContext(),R.color.active));
                        texview.setText(Integer.toString(test));
                        pause = false;
                        Integer [] intArray = {test,0};
                        msgList.add(intArray);
                        test = 0;

                        if(!isStarted) {
                            mHandler.post(mUpdateUI);
                            isStarted = true;
                        }

                        break;
                    case MotionEvent.ACTION_MOVE:
                        switchButton.setTextColor(ContextCompat.getColor(getContext(),R.color.active));
                        drawable.setStroke(15, ContextCompat.getColor(getContext(),R.color.active));
                        texview.setText(Integer.toString(test));



                        break;
                    case MotionEvent.ACTION_UP:
                        switchButton.setTextColor(ContextCompat.getColor(getContext(),R.color.inactive));
                        drawable.setStroke(5, ContextCompat.getColor(getContext(),R.color.inactive));
                        texview.setText(Integer.toString(test));
                        pause = true;
                        Integer [] intArray2 = {test,1};
                        msgList.add(intArray2);
                        test = 0;

                        break;
                    default:
                        switchButton.setTextColor(ContextCompat.getColor(getContext(),R.color.inactive));
                        drawable.setStroke(5, ContextCompat.getColor(getContext(),R.color.inactive));
                        texview.setText("off");

                        return false;
                }
                return true;
            }
        });



    }
    @Override
    public void onResume() {
        super.onResume();
    }

}
