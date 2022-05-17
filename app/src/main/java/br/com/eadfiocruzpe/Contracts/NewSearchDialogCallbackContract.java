package br.com.eadfiocruzpe.Contracts;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public interface NewSearchDialogCallbackContract {

    void onActionConfirmed(LDBSearch search);

    void onShowDialogMessage(String message);

}