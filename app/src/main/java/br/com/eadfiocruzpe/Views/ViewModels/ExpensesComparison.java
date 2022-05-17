package br.com.eadfiocruzpe.Views.ViewModels;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBExpense;

public class ExpensesComparison {

    private LDBExpense mExpenseA;
    private LDBExpense mExpenseB;

    public void setExpenseA(LDBExpense expense) {
        mExpenseA = expense;
    }

    public LDBExpense getExpenseA() {
        return mExpenseA;
    }

    public void setExpenseB(LDBExpense expense) {
        mExpenseB = expense;
    }

    public LDBExpense getExpenseB() {
        return mExpenseB;
    }

}