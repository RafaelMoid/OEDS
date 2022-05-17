package br.com.eadfiocruzpe.Models.BusinessLogicModels;

import java.util.ArrayList;

import br.com.eadfiocruzpe.Models.BusinessLogicModels.LocalDatabase.LDBSearch;

public class SharedContent {

    private LDBSearch mSearchA;
    private LDBSearch mSearchB;
    private ArrayList<String> mSharedContent;
    private int mSelectedSocialNetworkId = -1;
    private boolean mIsSharingComparison = false;


    public void setListPathImgSharedContent(ArrayList<String> pathImgsSharedContent) {
        mSharedContent = pathImgsSharedContent;
    }

    public ArrayList<String> getListPathImgSharedContent() {
        return mSharedContent;
    }

    public void setSelectedSocialNetworkId(int socialNetId) {
        mSelectedSocialNetworkId = socialNetId;
    }

    public int getSelectedSocialNetworkId() {
        return mSelectedSocialNetworkId;
    }

    public LDBSearch getSearchA() {
        return mSearchA;
    }

    public void setSearchA(LDBSearch searchA) {
        mSearchA = searchA;
    }

    public LDBSearch getSearchB() {
        return mSearchB;
    }

    public void setSearchB(LDBSearch searchB) {
        mSearchB = searchB;
    }

    public boolean isSharingComparison() {
        return mIsSharingComparison;
    }

    public void setSharingComparison(boolean isSharingComparison) {
        mIsSharingComparison = isSharingComparison;
    }

}