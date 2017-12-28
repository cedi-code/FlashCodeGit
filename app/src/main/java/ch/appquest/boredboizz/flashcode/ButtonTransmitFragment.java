package ch.appquest.boredboizz.flashcode;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Girardin on 26.12.2017.
 */
public class ButtonTransmitFragment extends Fragment {
    private Button switchButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.button_transmit, container, false);
        // initzialisiert den switchButton
        initButton(myFragmentView);

        return myFragmentView;
    }
    // setzt den OnTouchListener und gibt dem switchButton den Button
    private void initButton(View v) {
        switchButton = (Button) v.findViewById(R.id.switchButton);

        final MainActivity main = (MainActivity) getActivity();
        final GradientDrawable drawable = (GradientDrawable)switchButton.getBackground();

        switchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // turn on flash
                        switchButton.setTextColor(Color.WHITE);
                        drawable.setStroke(15, Color.GREEN);
                        main.turnOnFlashLight();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        switchButton.setTextColor(Color.WHITE);
                        drawable.setStroke(15, Color.GREEN);
                        main.turnOnFlashLight();
                        break;
                    case MotionEvent.ACTION_UP:
                        switchButton.setTextColor(Color.BLACK);
                        drawable.setStroke(5, Color.DKGRAY);
                        main.turnOffFlashLight();
                        break;
                    default:
                        switchButton.setTextColor(Color.BLACK);
                        drawable.setStroke(5, Color.DKGRAY);
                        main.turnOffFlashLight();
                        return false;
                }
                return true;
            }
        });
    }
}
