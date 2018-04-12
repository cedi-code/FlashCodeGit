package ch.appquest.boredboizz.flashcode;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaMetadataRetriever;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by Girardin on 09.04.2018.
 */

public class encodeFootage {


    private ArrayList<Bitmap> frames = new ArrayList<>();
    private int fps = 1;
    private int width;
    private MainActivity SCHEISSLÖSCHMI;
    public encodeFootage(MainActivity main) {
        SCHEISSLÖSCHMI = main;
    }
    public void enCode(String fileName, File savePath) {
        try {
            frames = getFrames(fileName);
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SCHEISSLÖSCHMI.getApplicationContext(),"getFrames Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        String timestamp = new SimpleDateFormat("yyyy.MM.dd_HH:mm:ss").format(new Date());
        //width = frames.get(0).getWidth();
        //height = frames.get(0).getHeight();

        try {
            if(calculateBrightnessEstimate(frames.get(0),10) > 90) {

                    saveToInternalStorage(findLightSpot(darkenBitMap(frames.get(0))),savePath,timestamp + 0 + "dark_id" );


                    saveToInternalStorage(findLightSpot(darkenBitMap(frames.get(1))),savePath,timestamp + "dark_id" + 1);
            }else {
                saveToInternalStorage(findLightSpot(frames.get(0)),savePath, timestamp + 0 +"_id" );
                saveToInternalStorage(findLightSpot(frames.get(1)),savePath, timestamp + 0 +"_id" );
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(SCHEISSLÖSCHMI.getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        /*saveToInternalStorage(findLightSpot(frames.get(frames.size()-1)),savePath, timestamp + "end_id");
        /*for (int count = 1; count < frames.size(); count++) {
            // saveToInternalStorage(frames.get(count),savePath, count +"_id" + timestamp);
        }*/

    }


    private ArrayList<Bitmap> getFrames(String path) {
        try {
            ArrayList<Bitmap> bArray = new ArrayList<Bitmap>();
            bArray.clear();
            FFmpegMediaMetadataRetriever mRetriever = new FFmpegMediaMetadataRetriever();
            // von uri holen
            mRetriever.setDataSource(path);

            String time = mRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInmillisec = Long.parseLong( time );
            long seconds = timeInmillisec / 1000;
            if(seconds <= 1){
                return null;
            }else {
                for (int i = 0; i < seconds*fps; i++) {

                    bArray.add(mRetriever.getFrameAtTime(anzahlFramesProSec(fps)*i,
                            MediaMetadataRetriever.OPTION_CLOSEST));
                }
            }

            return bArray;
        } catch (Exception e) { return null; }
    }
    private int anzahlFramesProSec(int anzahl) {
        return (int) 1000000 / anzahl;
    }

    // fürs Testing
    private void saveToInternalStorage(Bitmap bitmapImage,File savePath,String id){

        // path to /data/data/yourapp/app_data/imageDir
        // Create imageDir
        File mypath=new File(savePath,"frameTest" + id + ".jpg");

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

    private Bitmap findLightSpot(Bitmap bitmap) {
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




        Toast.makeText(SCHEISSLÖSCHMI.getApplicationContext(),hellsterPunktX + "," + hellsterPunktY, Toast.LENGTH_SHORT).show();

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
    private int get1DPos(int x,int y) {
        return x + y*width;
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
    private Bitmap darkenBitMap(Bitmap bm) {

        Bitmap bitmap = bm.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas();
        canvas.setBitmap(bitmap);
        Paint p = new Paint(Color.RED);
        //ColorFilter filter = new LightingColorFilter(0xFFFFFFFF , 0x00222222); // lighten
        ColorFilter filter = new LightingColorFilter(0xFF7F7F7F, 0x00000000);    // darken
        p.setColorFilter(filter);
        canvas.drawBitmap(bm, new Matrix(), p);

        return bm;
    }
    private Paint paint = new Paint();
    private ColorMatrix cm = new ColorMatrix();
    public Bitmap toGrayscale(Bitmap bmp) {

        Bitmap bmpGrayscale = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmp, 0, 0, paint);
        return bmpGrayscale;
    }

}
