package ch.appquest.boredboizz.flashcode;


import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
                        switchButton.setText("On");
                        switchButton.setTextColor(ContextCompat.getColor(getContext(),R.color.active));
                        drawable.setStroke(15, ContextCompat.getColor(getContext(),R.color.active));
                        main.turnOnFlashLight();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        switchButton.setText("On");
                        switchButton.setTextColor(ContextCompat.getColor(getContext(),R.color.active));
                        drawable.setStroke(15, ContextCompat.getColor(getContext(),R.color.active));
                        main.turnOnFlashLight();
                        break;
                    case MotionEvent.ACTION_UP:
                        switchButton.setText("Off");
                        switchButton.setTextColor(ContextCompat.getColor(getContext(),R.color.inactive));
                        drawable.setStroke(5, ContextCompat.getColor(getContext(),R.color.inactive));
                        main.turnOffFlashLight();
                        break;
                    default:
                        switchButton.setText("Off");
                        switchButton.setTextColor(ContextCompat.getColor(getContext(),R.color.inactive));
                        drawable.setStroke(5, ContextCompat.getColor(getContext(),R.color.inactive));
                        main.turnOffFlashLight();
                        return false;
                }
                return true;
            }
        });
    }
}
