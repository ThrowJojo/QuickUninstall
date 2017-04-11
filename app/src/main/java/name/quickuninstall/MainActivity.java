package name.quickuninstall;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import name.quickuninstall.adapters.AppListAdapter;
import name.quickuninstall.apps.AppData;
import name.quickuninstall.apps.PackagesHandler;
import name.quickuninstall.databinding.ActivityMainBinding;
import name.quickuninstall.layout.BottomSheetDialog;
import name.quickuninstall.layout.BottomSheetDialogListener;
import name.quickuninstall.misc.Functions;
import name.quickuninstall.misc.SortType;
import name.quickuninstall.misc.Sorters;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, BottomSheetDialogListener {

    App app;
    ArrayList<AppData> appData;
    LinearLayoutManager layoutManager;
    AppListAdapter appListAdapter;
    BottomSheetDialog bottomSheet;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        app = (App) getApplication();
        app.selectedApps.addOnListChangedCallback(listListener);
        bottomSheet = new BottomSheetDialog();
        bottomSheet.app = app;
        bottomSheet.listener = this;
        setSupportActionBar(binding.toolbar);
        populateAppList();
        addAdView();
    }

    // Populates the list with apps that are installed on the current device
    private void populateAppList() {
        appData = PackagesHandler.getInstalledPackages(this);
        sortAppDataBy(app.sortType);
        layoutManager = new LinearLayoutManager(this);
        binding.appList.setLayoutManager(layoutManager);
        binding.appList.setItemAnimator(new DefaultItemAnimator());
        appListAdapter = new AppListAdapter(app, appData);
        app.appListAdapter = appListAdapter;
        binding.appList.setAdapter(appListAdapter);
    }

    // Ads a banner to the top of the app
    private void addAdView() {
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(Constants.AD_UNIT_ID_BANNER);
        // TODO: Change Test Device before release
        AdRequest request = new AdRequest.Builder()
                .addTestDevice("A15869F56E0F5EB30C64F129E81D2578")
                .build();
        adView.loadAd(request);
        binding.adContainer.addView(adView);
    }

    // Method for when uninstall is clicked, if no apps are selected a Toast dialog appears
    public void onUninstallClicked() {
        if (app.canUninstallApps()) {
            app.uninstallSelectedApps();
            appListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, getString(R.string.toast_uninstall), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Repopulates the app list if the after uninstalling/switching apps
        populateAppList();
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
        unselectAll.setVisible((app.selectedApps.size() > 0));
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
            selectAllApps();
            return true;
        } else if (id == R.id.action_unselect_all) {
            // Deselects any selected apps
            unselectAllApps();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method to select every app in the list
    private void selectAllApps() {
        app.replaceSelectedApps(appData);
    }

    // Method to deselect every app in the list
    private void unselectAllApps() {
        app.replaceSelectedApps(new ArrayList<AppData>());
    }

    // Method to show the sort menu
    private void toggleBottomSheet() {
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    // Method called when the text of a search changes
    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<AppData> queryResults = getAppsForQuery(newText);
        appListAdapter = new AppListAdapter(app, queryResults);
        app.appListAdapter = appListAdapter;
        binding.appList.setAdapter(appListAdapter);
        return false;
    }

    // Get apps for a particular search query
    private ArrayList<AppData> getAppsForQuery(String query) {
        ArrayList<AppData> results = new ArrayList<>();
        for (AppData data : appData) if (data.label.toLowerCase().contains(query.toLowerCase())) results.add(data);
        return results;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    // Delegate method for when a sort option is selected
    @Override
    public void onSortSelected(SortType type) {
        sortAppDataBy(type);
        appListAdapter.notifyDataSetChanged();
        bottomSheet.refreshSortChoice();
    }

    // Changes the selected sortType then sorts the appData ArrayList based on the value
    private void sortAppDataBy(SortType type) {
        app.sortType = type;
        switch (type) {
            case NAME:
                Sorters.byName(appData);
                break;
            case DATE:
                Sorters.byInstalledDate(appData);
                break;
            case FILE_SIZE:
                Sorters.bySize(appData);
                break;
            default:
                Sorters.byName(appData);
                break;
        }
    }

    // The observer for the list of selected apps
    private ObservableList.OnListChangedCallback<ObservableList<AppData>> listListener = new ObservableList.OnListChangedCallback<ObservableList<AppData>>() {

        @Override
        public void onItemRangeRemoved(ObservableList<AppData> appDatas, int i, int i1) {
            //noinspection RestrictedApi
            invalidateOptionsMenu();
        }

        @Override
        public void onItemRangeInserted(ObservableList<AppData> appDatas, int i, int i1) {
            //noinspection RestrictedApi
            invalidateOptionsMenu();
        }

        @Override
        public void onChanged(ObservableList<AppData> appDatas) {}
        @Override
        public void onItemRangeChanged(ObservableList<AppData> appDatas, int i, int i1) {}
        @Override
        public void onItemRangeMoved(ObservableList<AppData> appDatas, int i, int i1, int i2) {}
    };

}
