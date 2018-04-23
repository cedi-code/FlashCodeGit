package ch.appquest.boredboizz.flashcode;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TextTransmitFragment extends Fragment{
    private EditText mEdit;
    private Button send;

    // Text tab Fragment wo die Textbox und der Send Button ist
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.text_transmit, container, false);
        // initzialisiert die Objekte im Fragment mit onklicks
        init(myFragmentView);

        return myFragmentView;
    }

    // initzialisiert die Objekte im Fragment mit onklicks
    private void init(View v) {
        // holst sich die Objekte vom XML und speichert diese in die Variabeln
        send = (Button) v.findViewById(R.id.send);
        mEdit = (EditText) v.findViewById(R.id.editText);

        // holst sich die MainActivity klasse
        final MainActivity main = (MainActivity) getActivity();
        // Mit diesem Onklick auf dem sender Button wird der Eingegebene Text in der message Attribut der MainActivity gespeichert
        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mEdit.getText().toString().equals("")) {
                            String nachricht = mEdit.getText().toString().toLowerCase();
                            MorseMessage mm = new MorseMessage(main);
                            MorseTranslater mt = new MorseTranslater();
                            String[] morseCode = mt.textToMorse(nachricht);
                            mm.transmitMorse(morseCode); //Hier werden die MorseSignale gesendet
                        }else {
                            Toast.makeText(main.getApplicationContext(),"Bitte geben Sie eine Nachricht ein!",Toast.LENGTH_LONG).show();
                        }
                        // wenn man den Button drückt verlässt man den Focus und somit auch die Tastatur
                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
        );
    }
}
