package br.com.eadfiocruzpe.Views.Pages.Terms;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

import br.com.eadfiocruzpe.R;
import br.com.eadfiocruzpe.Views.Pages.Global.BaseActivity;

public class TermsActivity extends BaseActivity {

    @BindView(R.id.root)
    public CoordinatorLayout rootView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progress_bar_container)
    public View progressBarContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initViews();
    }

    /**
     * Initialization
     */
    private void initViews() {
        initMainView(rootView);
        initProgressView(progressBarContainer);
        initToolbar(true, getString(R.string.page_terms), toolbar);
        setToolbarTitleColor(R.color.color_white);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(menuItem);
    }

}
