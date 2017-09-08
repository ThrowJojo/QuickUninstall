package breakingscope.quickuninstall;

import android.app.SearchManager;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import breakingscope.quickuninstall.adapters.AppListAdapter;
import breakingscope.quickuninstall.adapters.AppListAdapterListener;
import breakingscope.quickuninstall.apps.AppData;
import breakingscope.quickuninstall.apps.Helpers;
import breakingscope.quickuninstall.databinding.ActivityMainBinding;
import breakingscope.quickuninstall.layout.BottomSheetDialog;
import breakingscope.quickuninstall.layout.BottomSheetDialogListener;
import breakingscope.quickuninstall.misc.Functions;
import breakingscope.quickuninstall.misc.SortType;
import breakingscope.quickuninstall.models.AppsModel;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, BottomSheetDialogListener, AppListAdapterListener, LifecycleRegistryOwner {

    private static final String LOG_TAG = MainActivity.class.getName();

    LinearLayoutManager layoutManager;
    AppListAdapter appListAdapter;
    BottomSheetDialog bottomSheet;
    ActivityMainBinding binding;

    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);
    AppsModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);

        model = ViewModelProviders.of(this).get(AppsModel.class);
        model.loadInstalledApps();
        model.getDisplayedApps().observe(this, new Observer<ArrayList<AppData>>() {
            @Override
            public void onChanged(@Nullable ArrayList<AppData> appData) {
                refreshList();
            }
        });
        model.getSelectedApps().observe(this, new Observer<ArrayList<AppData>>() {
            @Override
            public void onChanged(@Nullable ArrayList<AppData> appData) {
                refreshList();
            }
        });

        setup();

        bottomSheet = new BottomSheetDialog();
        bottomSheet.listener = this;
        setSupportActionBar(binding.toolbar);
        addAdView();
    }

    public void setup() {
        layoutManager = new LinearLayoutManager(this);
        binding.appList.setLayoutManager(layoutManager);
        binding.appList.setItemAnimator(new DefaultItemAnimator());
        appListAdapter = new AppListAdapter(this, model.getDisplayedApps().getValue(), model.getSelectedApps().getValue(), this);
        binding.appList.setAdapter(appListAdapter);
    }

    private void refreshList() {
        appListAdapter.updateData(model.getDisplayedApps().getValue(), model.getSelectedApps().getValue());
        appListAdapter.notifyDataSetChanged();
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onSelectionChanged(AppData data, boolean value) {
        if (value) {
            model.addSelectedApp(data);
        } else {
            model.removeSelectedApp(data);
        }
    }

    // Ads a banner to the top of the app
    private void addAdView() {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(Helpers.readConfigValue(this, Constants.CONFIG_KEY_ADMOB_ID_BANNER));
        AdRequest request = new AdRequest.Builder()
                .addTestDevice(Helpers.readConfigValue(this, Constants.CONFIG_KEY_TEST_DEVICE))
                .build();
        adView.loadAd(request);
        binding.adContainer.addView(adView);
    }

    // Method for when uninstall is clicked, if no apps are selected a Toast dialog appears
    public void onUninstallClicked() {
        if (model.canUninstallApps()) {
            model.uninstallSelectedApps();
        } else {
            Toast.makeText(this, getString(R.string.toast_uninstall), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (model != null) model.checkForReload();
    }

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        MenuItem unselectAll = menu.findItem(R.id.action_unselect_all);

        // If there is at least 1 selected app show the menu item, if not disable it
        if (model.getSelectedApps().getValue() != null) unselectAll.setVisible((model.getSelectedApps().getValue().size() > 0));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_rate) {
            // Takes the user to the store listing for the app to review it
            Functions.showAppListing(this, getPackageName());
            return true;
        } else if (id == R.id.action_share) {
            // Opens a dialog to let the user share the app
            Functions.shareApp(this);
            return true;
        } else if (id == R.id.action_sort) {
            // Opens the sort menu
            toggleBottomSheet();
            return true;
        } else if (id == R.id.action_select_all) {
            // Selects all apps
            model.selectAllApps();
            return true;
        } else if (id == R.id.action_unselect_all) {
            // Deselects any selected apps
            model.deselectAllApps();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method to show the sort menu
    private void toggleBottomSheet() {
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    // Method called when the text of a search changes
    @Override
    public boolean onQueryTextChange(String newText) {
        model.processSearchQuery(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // Delegate method for when a sort option is selected
    @Override
    public void onSortSelected(SortType type) {
        model.sortDisplayedApps(type);
    }

}
