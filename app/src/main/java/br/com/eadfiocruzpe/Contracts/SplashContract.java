package br.com.eadfiocruzpe.Contracts;

public interface SplashContract {

    interface View extends BaseContract.View {
        void navigateToHomePage();
    }

    interface Presenter {
        void init();
    }

}