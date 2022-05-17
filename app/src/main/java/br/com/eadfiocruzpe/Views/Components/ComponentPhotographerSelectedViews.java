package br.com.eadfiocruzpe.Views.Components;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import br.com.eadfiocruzpe.Contracts.ComparePhotographerSelectedViewsContract;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.PermissionUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;

public class ComponentPhotographerSelectedViews {

    private final String TAG = "CPhotoSelectViews";

    private ArrayList<ComponentShareableContentSelector> mContentSelectors;
    private HashMap<String, String> mMapImgsSelectedContent = new HashMap<>();

    private Context mContext;
    private ComparePhotographerSelectedViewsContract mCallback;
    private LogUtils mLogUtils;

    /**
     * Initialization
     */
    public ComponentPhotographerSelectedViews(Context context,
                                              ComparePhotographerSelectedViewsContract callback,
                                              ArrayList<ComponentShareableContentSelector> contentSelectors) {
        mContext = context;
        mCallback = callback;
        mContentSelectors = contentSelectors;
        mLogUtils = new LogUtils();

        initSelectors();
    }

    private void initSelectors() {
        try {

            for (ComponentShareableContentSelector shareableSelector : mContentSelectors) {
                shareableSelector.lockSelectorVisibility(false);
                mMapImgsSelectedContent.put(shareableSelector.getContentId(), "");
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initSelectors " + e.getMessage());
        }
    }

    /**
     * Events
     */
    public void enableSharingSelectors(boolean enable) {
        try {

            for (ComponentShareableContentSelector shareableSelector : mContentSelectors) {

                if (!shareableSelector.isSelectorVisibilityLocked()) {
                    shareableSelector.show(enable);
                }
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to enableSharingSelectors " + e.getMessage());
        }
    }

    public void lockShareableSelector(String selectorId, boolean lock) {
        try {

            for (ComponentShareableContentSelector shareableSelector : mContentSelectors) {

                if (shareableSelector.getContentId().equals(selectorId)) {
                    shareableSelector.show(false);
                    shareableSelector.lockSelectorVisibility(lock);
                }
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to enableSharingSelectors " + e.getMessage());
        }
    }

    public void takePictureSelectedItems() {
        try {

            for (ComponentShareableContentSelector shareableSelector : mContentSelectors) {

                if (shareableSelector.isSelectorChecked()) {
                    mMapImgsSelectedContent.put(
                            shareableSelector.getContentId(),
                            confirmPermissionGetPathImgSelectedView(shareableSelector.getSelectedView()));
                }
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to takePictureSelectedItems " + e.getMessage());
        }
    }

    /**
     * Image handling
     */
    private String confirmPermissionGetPathImgSelectedView(View selectedView) {
        try {
            PermissionUtils permissionUtils = new PermissionUtils();

            if (permissionUtils.isExternalStorageWritable()) {

                if (permissionUtils.isStoragePermissionGranted(mContext.getApplicationContext())) {
                    File shareableContentImageFile = saveBitMap(mContext.getApplicationContext(), selectedView);

                    if (shareableContentImageFile != null) {
                        return shareableContentImageFile.getAbsolutePath();
                    }

                } else if (mCallback != null) {
                    mCallback.askPermissionWriteExternalStorage();
                }
            } else if (mCallback != null) {
                mCallback.externalStorageNotWritable();
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to confirmPermissionGetPathImgSelectedView " + e.getMessage());
        }

        return "";
    }

    /**
     * Created an image from the container which contains the shareable content
     */
    private File saveBitMap(Context context, View drawView){
        File pictureFile = null;

        try {
            File pictureFileDir = getPublicAlbumStorageDir(ConstantUtils.APP_IMAGE_FOLDER_NAME);

            if (!pictureFileDir.exists()) {

                try {
                    boolean isDirectoryCreated = pictureFileDir.mkdirs();

                    if (!isDirectoryCreated) {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to saveBitMap");
                    }
                } catch (Exception e) {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                            TAG + " Failed to saveBitMap: " + e.getMessage());
                }

                return null;
            }

            String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis()+".jpg";
            pictureFile = new File(filename);
            Bitmap bitmap = getBitmapFromView(drawView);

            try {

                if (pictureFile.createNewFile()) {
                    FileOutputStream oStream = new FileOutputStream(pictureFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
                    oStream.flush();
                    oStream.close();
                } else {
                    mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                            TAG + " Failed to saveBitMap.createNewFile");
                }

            } catch (IOException e) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + " Failed to saveBitMap.createNewFile: " + e.getMessage());
            }

            scanGallery(context, pictureFile.getAbsolutePath());

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to saveBitMap: " + npe.getMessage());
        }

        return pictureFile;
    }

    /**
     * Get the directory for the user's public pictures directory.
     */
    private File getPublicAlbumStorageDir(String albumName) {
        File file = null;

        try {
            file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    albumName);

            if (!file.mkdirs()) {
                mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                        TAG + " Failed to getPublicAlbumStorageDir: Directory not created");
            }
        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getPublicAlbumStorageDir: " + e.getMessage());
        }

        return file;
    }

    /**
     * Create bitmap from view and returns it
     */
    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = null;

        try {
            returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            Drawable bgDrawable = view.getBackground();

            // Drawable has background?
            if (bgDrawable != null) {
                bgDrawable.draw(canvas);
            }   else{
                canvas.drawColor(Color.WHITE);
            }

            // Draw the view on the canvas
            view.draw(canvas);

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getBitmapFromView: " + e.getMessage());
        }

        return returnedBitmap;
    }

    /**
     * Verify if the image that was created is in the gallery
     */
    private void scanGallery(Context context, String path) {
        try {
            MediaScannerConnection.scanFile(
                context,
                new String[] {
                    path
                },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {

                        if (uri != null) {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                                        TAG + " Done scanning gallery, the image is there");
                        } else {
                            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                        TAG + " Failed to scanGallery: the image isn't there");
                        }
                    }
                }
            );
        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to scanGallery: " + e.getMessage());
        }
    }

    /**
     * Getters and Setters
     */
    public ArrayList<String> getAbsolutePathSharedImgs() {
        try {
            return new ArrayList<>(mMapImgsSelectedContent.values());

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getAbsolutePathSharedImgs " + npe.getMessage());
        }

        return new ArrayList<>();
    }

}