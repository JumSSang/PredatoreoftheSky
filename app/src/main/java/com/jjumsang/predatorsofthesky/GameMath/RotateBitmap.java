package com.jjumsang.predatorsofthesky.GameMath;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by 경민 on 2015-08-01.
 */
public class RotateBitmap {
    public static Bitmap rotate(Bitmap bmp,float degree)
    {
        int width = bmp.getWidth();
        int height = bmp.getHeight();

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, true);
        bmp.recycle();
        return resizedBitmap;
    }
}
