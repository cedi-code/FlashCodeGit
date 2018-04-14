package ch.appquest.boredboizz.flashcode;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by Girardin on 09.04.2018.
 */
// TODO : framerate Berechnung + Besserer Algoritmus für die Lichtpunkt erkennung
public class encodeFootage {
    private int width;
    // fürs Testing
    public void saveToInternalStorage(Bitmap bitmapImage,File savePath,String id){

        // path to /data/data/yourapp/app_data/imageDir
        // Create imageDir
        String timestamp = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(new Date());
        File mypath=new File(savePath,"frameTest" + id +"_" +timestamp+ ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    // TODO besserer Algoritmus! brightness Berechnung funktioniert immer noch mit kombination von Hellsterpunkt!
    public int[] getLightPoints(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        this.width = width;
        int[] data = new int[width * height];
        int[][] data2D = new int[width][height];

        darkenBitMap(bitmap).getPixels(data, 0, width, 0, 0, width, height);

        int hellsterWert = data[0];
        int hellsterPunktX = 0;
        int hellsterPunktY = 0;
        int brightestValue = 0; int R = 0; int G = 0; int B = 0; int n = 0;
        // anstatt +1 +10 und bei true genauer mit +1
        for(int h = 0; h < height; h++) {
            for(int w = 0; w < width; w++) {
                data2D[w][h] = data[get1DPos(w,h)];
                if (data2D[w][h] > Color.argb(255,252,252,252)){
                    int color = data2D[w][h];
                    R += Color.red(color);
                    G += Color.green(color);
                    B += Color.blue(color);
                    n++;
                    if(data2D[w][h] >= hellsterWert && ((R + B + G) / (n * 3)) > brightestValue) {
                        hellsterPunktX = w;
                        hellsterPunktY = h;
                        hellsterWert = data2D[w][h];
                        brightestValue = ((R + B + G) / (n * 3));
                    }
                }else {
                     R = 0;  G = 0;  B = 0;  n = 0;
                }
            }
        }

        return new int[] {hellsterPunktX,hellsterPunktY};
    }

    private int get1DPos(int x,int y) {
        return x + y*width;
    }

    public Bitmap drawCircle(int posX,int posY,int width, int height,int color) {
        Bitmap bmp = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap
        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(color);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(10f);
        canvas.drawCircle(posX, posY, 30, p);
        return bmp;
    }


    private Bitmap darkenBitMap(Bitmap bm) {

        Bitmap bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);


        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }

    public int calculateBrightnessEstimate(android.graphics.Bitmap bitmap, int pixelSpacing) {
        int R = 0; int G = 0; int B = 0;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        int n = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i += pixelSpacing) {
            int color = pixels[i];
            R += Color.red(color);
            G += Color.green(color);
            B += Color.blue(color);
            n++;
        }
        return (R + B + G) / (n * 3);
    }


    private Bitmap find2LightSpots(Bitmap bitmap) {
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        this.width = width;
        int[] data = new int[width * height];
        int[][] data2D = new int[width][height];

        bitmap.getPixels(data, 0, width, 0, 0, width, height);

        int hellsterWert = data[0];
        int hellsterPunktX = 0;
        int hellsterPunktY = 0;

        int hellsterWert2 = data[0];
        int hellsterPunktX2 = 0;
        int hellsterPunktY2 = 0;



        for(int w = 0; w < width; w++) {
            for(int h = 0; h < height; h++) {
                data2D[w][h] = data[get1DPos(w,h)];
                //if (data2D[w][h] > Color.argb(255,150,150,150)){
                if(data2D[w][h] > hellsterWert) {
                    hellsterPunktX = w;
                    hellsterPunktY = h;
                    hellsterWert = data2D[w][h];
                }
                if(data2D[w][h] > hellsterWert2 &&
                        (hellsterPunktX + 50 < w || hellsterPunktX -50 > w) &&
                        (hellsterPunktY + 50 < h || hellsterPunktY -50 > h)){
                    hellsterPunktX2 = w;
                    hellsterPunktY2= h;
                    hellsterWert2 = data2D[w][h];
                }

                //data[i] = Color.argb(255, 245, 0, 0);
                //}
            }
        }
        data = drawOnHelsterPunkt(hellsterPunktX,hellsterPunktY,data,true);
        // check if second point had a match!
        if(hellsterPunktX2 < width-30 || hellsterPunktY2 < height-30) {
            data = drawOnHelsterPunkt(hellsterPunktX2,hellsterPunktY2,data,false);
        }


        return Bitmap.createBitmap(data, width, height, Bitmap.Config.ARGB_8888);
    }
    private int[] drawOnHelsterPunkt(int x,int y, int[] data,Boolean best) {
        int mColor;
        if(best) {
            mColor = Color.argb(255, 0, 245, 0);
        }else {
            mColor = Color.argb(255, 245, 0, 0);
        }

        data[get1DPos(x, y)] = mColor;

        data[get1DPos(x+30, y+30)] = mColor;
        data[get1DPos(x-30, y-30)] = mColor;
        data[get1DPos(x+30, y-30)] = mColor;
        data[get1DPos(x-30, y+30)] = mColor;

        for(int b = 1; b < 30; b++) {

            // Kreuz
            data[get1DPos(x-b, y)] = mColor;
            data[get1DPos(x+b, y)] = mColor;
            data[get1DPos(x, y-b)] = mColor;
            data[get1DPos(x, y+b)] = mColor;

            data[get1DPos(x-b, y+1)] = mColor;
            data[get1DPos(x+b, y+1)] = mColor;
            data[get1DPos(x+1, y-b)] = mColor;
            data[get1DPos(x+1, y+b)] = mColor;

            data[get1DPos(x-b, y-1)] = mColor;
            data[get1DPos(x+b, y-1)] = mColor;
            data[get1DPos(x-1, y-b)] = mColor;
            data[get1DPos(x-1, y+b)] = mColor;

            // Viereck
            data[get1DPos(x-30, y+b)] = mColor;
            data[get1DPos(x-30, y-b)] = mColor;
            data[get1DPos(x+30, y+b)] = mColor;
            data[get1DPos(x+30, y-b)] = mColor;

            data[get1DPos(x+b, y-30)] = mColor;
            data[get1DPos(x-b, y-30)] = mColor;
            data[get1DPos(x+b, y+30)] = mColor;
            data[get1DPos(x-b, y+30)] = mColor;
        }
        return data;
    }



}
