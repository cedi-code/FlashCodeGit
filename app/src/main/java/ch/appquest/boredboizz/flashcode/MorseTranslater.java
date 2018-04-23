package ch.appquest.boredboizz.flashcode;

/**
 * Created by florian.leuenberger on 23.04.2018.
 */

public class MorseTranslater {

    private static String[] morseTable = new String[26];

    private void initMorseTable() {
        morseTable[0] = ".-";
        morseTable[1] = "-...";
        morseTable[2] = "-.-.";
        morseTable[3] = "-..";
        morseTable[4] = ".";
        morseTable[5] = "..-.";
        morseTable[6] = "--.";
        morseTable[7] = "....";
        morseTable[8] = "..";
        morseTable[9] = ".---";
        morseTable[10] = "-.-";
        morseTable[11] = ".-..";
        morseTable[12] = "--";
        morseTable[13] = "-.";
        morseTable[14] = "---";
        morseTable[15] = ".--.";
        morseTable[16] = "--.-";
        morseTable[17] = ".-.";
        morseTable[18] = "...";
        morseTable[19] = "-";
        morseTable[20] = "..-";
        morseTable[21] = "...-";
        morseTable[22] = ".--";
        morseTable[23] = "-..-";
        morseTable[24] = "-.--";
        morseTable[25] = "--..";
    }

    public MorseTranslater() {
        initMorseTable();
    }

    public String morseToText(String[] morse) {
        String text = "";


        return text;
    }

    public String[] textToMorse(String text) {
        String[] morse = new String[40];

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            morse[i] = morseTable[c - 97];
        }
        return morse;
    }
}
