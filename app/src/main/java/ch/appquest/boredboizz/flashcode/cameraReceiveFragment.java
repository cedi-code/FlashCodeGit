package ch.appquest.boredboizz.flashcode;


import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.jar.Manifest;

/**
 * Created by Girardin on 01.04.2018.
 */

public class cameraReceiveFragment extends Fragment {


    private ImageButton playButton;
    private boolean isPlaying;
    private TextureView mTextureView;
    private TextureView.SurfaceTextureListener mSurfaceTextureListener;
    private CameraDevice mCameraDevice;
    private CameraDevice.StateCallback mCameraCallback;
    private HandlerThread mBackgroundHandlerThread;
    private Handler mBackgroundHandler;

    private Size mPreviewSize;
    private CaptureRequest.Builder mCaptureRecuestBuilder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myFragmentView = inflater.inflate(R.layout.live_camera, container, false);
        // initzialisiert die Objekte im Fragment mit onklicks
        // TODO main Activity braucht es nicht unbedingt!!!!
        final MainActivity main = (MainActivity) getActivity();
        initCamera(myFragmentView);
        initPlayButton(myFragmentView,main);
        initTextureView(myFragmentView,main);
        return myFragmentView;
    }
    @Override
    public void onResume() {
        super.onResume();
        final MainActivity main = (MainActivity) getActivity();
        startBackgroundThread();

        if(mTextureView.isAvailable()) {
            setupCamera(mTextureView.getHeight(), mTextureView.getHeight(), main);
            connectCamera(main);
        }else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }
    @Override
    public void onPause() {
        closeCamera();
        isPlaying = false;

        stopBackgroundThread();

        super.onPause();
    }

    private void initPlayButton(View v, MainActivity main) {
        playButton = (ImageButton) v.findViewById(R.id.playButton);

        isPlaying = false;

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying) {
                    playButton.setImageResource(R.drawable.ic_play_circle_outline_black_24dp);

                }else {
                    playButton.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
                }
                isPlaying = !isPlaying;
            }
        });


    }
    private void initCamera(View v) {
        mCameraCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                mCameraDevice = camera;
                startPreview();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                camera.close();
                mCameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                camera.close();
                mCameraDevice = null;
            }
        };

    }
    private void setupCamera(int width, int height, final MainActivity main) {
        try {
            CameraCharacteristics cameraCharacteristics = main.getmCameraManager().getCameraCharacteristics(main.getmCameraId());
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height );

        }catch(CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private void connectCamera(final  MainActivity main) {
        CameraManager cameraManager = main.getmCameraManager();
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ContextCompat.checkSelfPermission(main, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraManager.openCamera(main.getmCameraId(),mCameraCallback,mBackgroundHandler);
                } else {
                    if(shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
                        Toast.makeText(main.getApplicationContext(), "required acccess to camera", Toast.LENGTH_SHORT).show();

                    }
                    requestPermissions(new String[] {android.Manifest.permission.CAMERA}, main.getRequestCameraPermissionResult() );
                }
            }else {
                cameraManager.openCamera(main.getmCameraId(),mCameraCallback,mBackgroundHandler);
            }

        }catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }
    private void startPreview() {
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(),mPreviewSize.getHeight());
        Surface previewSurface = new Surface(surfaceTexture);
        try {
            mCaptureRecuestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRecuestBuilder.addTarget(previewSurface);

            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    try {
                        session.setRepeatingRequest(mCaptureRecuestBuilder.build(), null, mBackgroundHandler);

                    }catch (CameraAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                }
            }, null);
        }catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void initTextureView(View v, final MainActivity main) {

        mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                setupCamera(width, height,main);
                connectCamera(main);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        };
        mTextureView = (TextureView) v.findViewById(R.id.textureView);
    }
    private void closeCamera() {
        if(mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    }
    private void startBackgroundThread() {
        mBackgroundHandlerThread = new HandlerThread("liveCamera");
        mBackgroundHandlerThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandlerThread.getLooper());
    }
    private void stopBackgroundThread() {
        mBackgroundHandlerThread.quitSafely();
        try {
            mBackgroundHandlerThread.join();

        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            mBackgroundHandlerThread = null;
            mBackgroundHandler = null;
        }


    }
    private static Size chooseOptimalSize(Size[] choices, int width, int height) {
        List<Size> bigEnough = new ArrayList<Size>();
        for(Size option : choices) {
            if (option.getHeight() == option.getWidth() * height / width &&
                    option.getWidth() >= width && option.getHeight() >= height) {

                bigEnough.add(option);
            }
        }
        if(bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizeByArea());
        }else {
            return choices[0];
        }

    }
}

