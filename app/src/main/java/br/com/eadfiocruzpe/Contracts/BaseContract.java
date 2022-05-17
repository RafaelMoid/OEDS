package br.com.eadfiocruzpe.Contracts;

public interface BaseContract {

    interface View {

        void onShowMessage(String message);

        void onShowProgress(boolean show);

    }

}