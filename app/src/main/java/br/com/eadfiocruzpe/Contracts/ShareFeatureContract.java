package br.com.eadfiocruzpe.Contracts;

public interface ShareFeatureContract {

    void initSocialNetPicker();

    void initShareableContentSelectors();

    void initNeedExternalStoragePermission(boolean showPermissionMsg);

    void startShare();

    void confirmShare();

    void undoShare();

    void showSocialNetPicker();

    void enableShareFlow();

    void confirmShareFlow();

    void finishShareFlow(int selectedSocialNetwork);

    void undoShareFlow();

    void redirectUserPermissionPageWriteExternalStorageDenied();

    void requestPermissionUseExternalStorage();

}
