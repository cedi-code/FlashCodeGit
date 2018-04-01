package ch.appquest.boredboizz.flashcode;

import android.util.Size;

import java.util.Comparator;

/**
 * Created by Girardin on 01.04.2018.
 */

class CompareSizeByArea implements Comparator<Size> {
    @Override
    public int compare(Size lhs, Size rhs) {
        return Long.signum((long) lhs.getWidth() * lhs.getHeight() /
                (long) rhs.getWidth() * rhs.getHeight());
    }
}
