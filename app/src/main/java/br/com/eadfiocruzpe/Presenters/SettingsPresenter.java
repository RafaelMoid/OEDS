package br.com.eadfiocruzpe.Presenters;

import android.content.Context;

import br.com.eadfiocruzpe.Contracts.SettingsContract;

public class SettingsPresenter extends BasePresenter<SettingsContract.View> implements
        SettingsContract.Presenter {

    public SettingsPresenter(Context context) {
        mContext = context;
    }

}