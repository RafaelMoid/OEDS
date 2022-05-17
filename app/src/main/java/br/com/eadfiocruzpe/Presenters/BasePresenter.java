package br.com.eadfiocruzpe.Presenters;

import android.content.Context;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V> {

    protected WeakReference<V> view;
    protected Context mContext;


    BasePresenter() {
        // ..
    }

    public void bindView(final V view) {
        this.view = new WeakReference<>(view);
    }

    public void unbindView() {
        view = null;
    }

    public V getView() {
        if (view != null) {
            return view.get();
        } else {
            return null;
        }
    }

}