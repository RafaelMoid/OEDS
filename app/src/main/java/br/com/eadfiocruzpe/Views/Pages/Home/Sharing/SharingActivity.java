package br.com.eadfiocruzpe.Views.Pages.Home.Sharing;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import br.com.eadfiocruzpe.Utils.KeyboardUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Contracts.CToolbarCallbackContract;
import br.com.eadfiocruzpe.Contracts.ShareableItemAdapterContract;
import br.com.eadfiocruzpe.Utils.JsonUtils;
import br.com.eadfiocruzpe.Utils.ConstantUtils;
import br.com.eadfiocruzpe.Utils.LogUtils;
import br.com.eadfiocruzpe.Utils.TypeUtils;
import br.com.eadfiocruzpe.Persistence.PreferencesManager;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.ShareableItem;
import br.com.eadfiocruzpe.Models.BusinessLogicModels.SharedContent;
import br.com.eadfiocruzpe.Views.Adapters.ShareableItemsAdapter;
import br.com.eadfiocruzpe.Views.Pages.Global.BaseActivity;
import br.com.eadfiocruzpe.Views.Components.ComponentToolbar;

public class SharingActivity extends BaseActivity {

    private static final String TAG = "SharingActivity";

    @BindView(R.id.root)
    public CoordinatorLayout rootView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar_container)
    View progressBarContainer;

    @BindView(R.id.sharing_scroll_view)
    ScrollView shareScrollView;

    @BindView(R.id.component_msg_no_search_selected)
    FrameLayout containerNoSearchSelectedMsg;

    @BindView(R.id.sharing_search_being_shared_description)
    TextView valueSearchBeingSharedDescription;

    @BindView(R.id.sharing_content_rv)
    RecyclerView rvShareableItems;
    @BindView(R.id.sharing_msg_shared_items)
    TextView msgSharedItems;

    @BindView(R.id.sharing_shared_content_preview_comment)
    EditText userCommentEditText;

    private LogUtils mLogUtils;
    private ViewTreeObserver mVto;
    private ShareableItemsAdapter mRvAdapterShareItems;

    /**
     * Initialization
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sharing);
        ButterKnife.bind(this);

        init();
    }

    public void init() {
        try {
            mLogUtils = new LogUtils(getApplicationContext());
            mLogUtils.logMessage(TypeUtils.LogMsgType.SUCCESS, TAG + "Initializing SharingActivity ...");

            setSharedContent(JsonUtils.fromJson(
                    getIntent().getStringExtra(ConstantUtils.REQUEST_PARAM_SHARED_CONTENT),
                    SharedContent.class));

        } catch (Exception e) {
            mLogUtils.logSystemMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + "Failed to init the input parameters: " + e.getMessage());
        }
    }

    /**
     * Reinitialization
     */
    @Override
    public void onResume() {
        super.onResume();

        initViews();
        initEvents();
    }

    private void initViews() {
        try {
            initMainView(rootView);
            initToolbar();
            initProgressView(progressBarContainer);
            initSearchInfo();
            initShareableItems();
            initCommentSection();
            displayContent(true);
            initScrollView();

        } catch (NullPointerException npe) {
            displayContent(false);
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + "Failed to initViews");
        }
    }

    private void initToolbar() {
        initToolbar(false, getString(R.string.sharing_page_title), toolbar);
        setMainToolbarItem(ComponentToolbar.IDX_MAIN_TOOLBAR_ITEM_PAGE_TITLE);
        updateToolbarTitle(getResources().getString(R.string.sharing_page_title));
        updateToolbarIcon(true, ContextCompat.getDrawable(getApplicationContext(), R.drawable.ico_share_black));
        setToolbarStyle(ComponentToolbar.TOOLBAR_STYLE_LIGHT);
        showSearchBtn(false);
        showBackBtn(true);
        setCallbackToolbarButtons(mCallbackToolbar);
    }

    public void initSearchInfo() {
        try {

            if (isValidShareSingleSearch()) {
                valueSearchBeingSharedDescription.setText(getSharedContent().getSearchA().toString());

            } else if (isValidShareComparisonSearches()) {
                String title = getSharedContent().getSearchA().toString();
                title += "\n";
                title += getSharedContent().getSearchB().toString();

                valueSearchBeingSharedDescription.setText(title);

            } else {
                valueSearchBeingSharedDescription.setText(getString(R.string.sharing_page_msg_no_search_selected));
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to initSearchInfo");
        }
    }

    public void initShareableItems() {
        try {
            // Prepare list
            ArrayList<ShareableItem> shareableItems = new ArrayList<>();
            ArrayList<String> sharedImgPaths = getSharedContent().getListPathImgSharedContent();
            Collections.reverse(sharedImgPaths);

            for (int i = 0; i < sharedImgPaths.size(); i++) {
                String imgPath = sharedImgPaths.get(i);

                if (!imgPath.isEmpty()) {
                    ShareableItem shareableItem = new ShareableItem();
                    shareableItem.setId("si_" + String.valueOf(i));
                    shareableItem.setImg(imgPath);

                    shareableItems.add(shareableItem);
                }
            }

            // Init and load the shareable items adapter
            rvShareableItems.setVisibility(View.VISIBLE);
            rvShareableItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            mRvAdapterShareItems = new ShareableItemsAdapter(
                    mCallbackListShareableItems,
                    getApplicationContext(),
                    shareableItems);
            rvShareableItems.setAdapter(mRvAdapterShareItems);

            if (getSharedContent().getSelectedSocialNetworkId() == ConstantUtils.SOCIAL_NET_TWITTER) {
                msgSharedItems.setText(getString(R.string.sharing_page_msg_twitter_only_accept_one_img_time));
                msgSharedItems.setVisibility(View.VISIBLE);
            } else {
                msgSharedItems.setText("");
                msgSharedItems.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to initShareableItems " + e.getMessage());
        }
    }

    public void initCommentSection() {
        userCommentEditText.setEnabled(isValidShareSingleSearch() || isValidShareComparisonSearches());
    }

    private void displayContent(boolean display) {

        if (display && shareScrollView.getVisibility() != View.VISIBLE) {
            shareScrollView.setVisibility(View.VISIBLE);
            containerNoSearchSelectedMsg.setVisibility(View.GONE);

        } else if (!display && shareScrollView.getVisibility() != View.GONE){
            shareScrollView.setVisibility(View.GONE);
            containerNoSearchSelectedMsg.setVisibility(View.VISIBLE);
        }
    }

    private void initScrollView() {
        try {

            if (mVto == null) {
                mVto = shareScrollView.getViewTreeObserver();
                mVto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        shareScrollView.setSmoothScrollingEnabled(true);
                        shareScrollView.smoothScrollTo(0,0);
                    }
                });
            }
        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + " Failed to initScrollView");
        }
    }

    private void initEvents() {
        userCommentEditText.setOnFocusChangeListener(mCommentFocusChangeListener);
    }

    /**
     * Events
     */
    private final View.OnFocusChangeListener mCommentFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {

            if (!view.hasFocus()) {
                KeyboardUtils.dismissKeyboard(getApplicationContext(), userCommentEditText);
            }
        }
    };

    private final CToolbarCallbackContract mCallbackToolbar = new CToolbarCallbackContract() {
        @Override
        public void onSearchButtonClicked() {
            // ..
        }

        @Override
        public void onBackBtnClicked() {
            finish();
        }
    };

    private final ShareableItemAdapterContract mCallbackListShareableItems = new ShareableItemAdapterContract() {
        @Override
        public void onItemDeleted(ShareableItem item) {
            updateSharedItemsVisibility();
        }
    };

    private void updateSharedItemsVisibility() {
        try {

            if (mRvAdapterShareItems.getItemCount() == 0) {
                rvShareableItems.setVisibility(View.GONE);
                msgSharedItems.setText(getString(R.string.sharing_page_msg_no_img_selected));
                msgSharedItems.setVisibility(View.VISIBLE);

            } else {
                rvShareableItems.setVisibility(View.VISIBLE);
                msgSharedItems.setVisibility(View.GONE);
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + " Failed to updateSharedItemsVisibility");
        }
    }

    @OnClick(R.id.sharing_share_btn)
    public void shareContent() {
        try {

            if (getSharedContent().getSelectedSocialNetworkId() != -1) {

                switch (getSharedContent().getSelectedSocialNetworkId()) {

                    case ConstantUtils.SOCIAL_NET_WHATSAPP: {
                        executeSharingProcess(ConstantUtils.WHATSAPP_PACKAGE_NAME, false);
                        break;
                    }

                    case ConstantUtils.SOCIAL_NET_EMAIL: {
                        executeSharingProcess(ConstantUtils.GMAIL_PACKAGE_NAME, false);
                        break;
                    }

                    case ConstantUtils.SOCIAL_NET_FACEBOOK: {
                        executeSharingProcess(ConstantUtils.FACEBOOK_PACKAGE_NAME, false);
                        break;
                    }

                    case ConstantUtils.SOCIAL_NET_MESSENGER: {
                        executeSharingProcess(ConstantUtils.MESSENGER_PACKAGE_NAME, false);
                        break;
                    }

                    case ConstantUtils.SOCIAL_NET_TWITTER: {
                        executeSharingProcess(ConstantUtils.TWITTER_PACKAGE_NAME, true);
                        break;
                    }

                }
            } else {
                showMessage(getString(R.string.sharing_page_msg_please_select_social_network));
            }

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING, TAG + " Failed to shareContent");
            showMessage(getString(R.string.images_msg_error_creating_img));
        }
    }

    /**
     * Image Sharing on social networks
     */
    private String getComment() {
        try {
            String comment = userCommentEditText.getText().toString();
            comment += String.format(
                    getString(R.string.sharing_page_format_share),
                    getString(R.string.settings_page_url_project));

            return comment;

        } catch (NullPointerException npe) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getComment " + npe.getMessage());
        }

        return "";
    }

    private void executeSharingProcess(String patternFindShareActivity, boolean singleImg) {
        try {
            Intent shareIntent = getShareIntent(singleImg, getComment(),
                    mRvAdapterShareItems.getItems(getApplicationContext(), true));

            if (shareIntent != null) {
                PackageManager pm = getPackageManager();
                List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);

                for (final ResolveInfo app : activityList) {

                    if (app.activityInfo.name.contains(patternFindShareActivity)) {
                        final ActivityInfo activity = app.activityInfo;
                        final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);

                        shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        shareIntent.setComponent(name);

                        break;
                    }
                }

                startActivity(shareIntent);
                startActivityForResult(shareIntent, ConstantUtils.REQUEST_CODE_SHARE_IMG);

            } else {
                showMessage(getString(R.string.msg_could_not_share_information));

                shareIntent = getDefaultIntent();

                if (shareIntent != null) {
                    startActivityForResult(shareIntent, ConstantUtils.REQUEST_CODE_SHARE_NO_IMG);
                } else {
                    showMessage(getString(R.string.msg_could_not_share_information));

                    new CountDownTimer(ConstantUtils.TIMEOUT_DISMISS_MSG, 100) {
                        @Override
                        public void onTick(long l) {

                        }

                        @Override
                        public void onFinish() {
                            finish();
                        }
                    };
                }
            }

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to executeSharingProcess. " + e.getMessage());
        }
    }

    private Intent getShareIntent(boolean singleImg, String comment, ArrayList<Uri> sharedItems) {
        try {
            Intent shareIntent = new Intent();

            if (singleImg) {
                shareIntent.setAction(Intent.ACTION_SEND);
            } else {
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
            }

            shareIntent.setType(ConstantUtils.TEXT_TYPE);
            shareIntent.setType(ConstantUtils.IMAGE_FILE_TYPE);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_full_name_no_new_line));
            shareIntent.putExtra(Intent.EXTRA_TEXT, comment);

            if (singleImg) {
                shareIntent.putExtra(Intent.EXTRA_STREAM, sharedItems.get(0));
            } else {
                shareIntent.putExtra(Intent.EXTRA_STREAM, sharedItems);
            }

            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            return shareIntent;

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getShareIntent " + e.getMessage());
        }

        return null;
    }

    private Intent getDefaultIntent() {
        try {
            Intent defaultIntent = new Intent();
            defaultIntent.setAction(Intent.ACTION_SEND);
            defaultIntent.setType(ConstantUtils.TEXT_TYPE);
            defaultIntent.putExtra(Intent.EXTRA_TEXT, getComment());

            return defaultIntent;

        } catch (Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                    TAG + " Failed to getDefaultIntent " + e.getMessage());
        }

        return null;
    }

    /**
     * Navigation / End of the Activity life cycle
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ConstantUtils.REQUEST_CODE_SHARE_WHATSAPP ||
                requestCode == ConstantUtils.REQUEST_CODE_SHARE_NO_IMG ||
                    requestCode == ConstantUtils.REQUEST_CODE_SHARE_IMG) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        destroySharedImages();
        setSharedContent(null);

        super.onDestroy();
    }

    private void destroySharedImages() {
        try {

            for (String pathSharedImg : getSharedContent().getListPathImgSharedContent()) {
                File file = new File(pathSharedImg);

                if (file.exists()) {

                    if (file.delete()) {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                                TAG + " Deleted image: " + pathSharedImg);
                    } else {
                        mLogUtils.logMessage(TypeUtils.LogMsgType.WARNING,
                                TAG + " Failed to destroySharedImages.");
                    }
                }
            }

        } catch(Exception e) {
            mLogUtils.logMessage(TypeUtils.LogMsgType.REGULAR,
                    TAG + "Failed to destroySharedImages.");
        }
    }

    /**
     * Getters and Setters
     */
    public void setSharedContent(SharedContent sharedContent) {
        PreferencesManager.setSharedContent(sharedContent);
    }

    public SharedContent getSharedContent() {
        return PreferencesManager.getSharedContent();
    }

    public boolean isValidShareSingleSearch() {
        return getSharedContent() != null &&
               !getSharedContent().isSharingComparison() &&
               getSharedContent().getSearchA() != null;
    }

    public boolean isValidShareComparisonSearches() {
        return getSharedContent() != null &&
               getSharedContent().isSharingComparison() &&
               getSharedContent().getSearchA() != null &&
               getSharedContent().getSearchB() != null;
    }

}