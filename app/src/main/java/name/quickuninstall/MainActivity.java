package name.quickuninstall;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import name.packageshandler.AppData;
import name.packageshandler.PackagesHandler;
import name.quickuninstall.adapters.AppListAdapter;
import name.quickuninstall.databinding.ActivityMainBinding;
import name.quickuninstall.layout.BottomSheetDialog;
import name.quickuninstall.layout.BottomSheetDialogListener;
import name.quickuninstall.misc.Functions;
import name.quickuninstall.misc.SortType;
import name.quickuninstall.misc.Sorters;

// TODO: Hardcode SearchView, searchable.xml
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, BottomSheetDialogListener {

    App app;
    ArrayList<AppData> appData;
    LinearLayoutManager layoutManager;
    AppListAdapter appListAdapter;
    BottomSheetDialog bottomSheet = new BottomSheetDialog();
    SortType sortType = SortType.NAME;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setActivity(this);
        app = (App) getApplication();
        bottomSheet.listener = this;
        setSupportActionBar(binding.toolbar);
        populateAppList();
    }

    // Populates the list with apps that are installed on the current device
    private void populateAppList() {
        appData = PackagesHandler.getInstalledPackages(this);
        sortAppDataBy(sortType);
        layoutManager = new LinearLayoutManager(this);
        binding.appList.setLayoutManager(layoutManager);
        binding.appList.setItemAnimator(new DefaultItemAnimator());
        appListAdapter = new AppListAdapter(app, appData);
        binding.appList.setAdapter(appListAdapter);
    }

    public void onUninstallClicked() {
        if (app.canUninstallApps()) {
            app.uninstallSelectedApps();
        } else {
            // TODO: Hardcode string or change FAB to not appear if nothing is selected
            Toast.makeText(this, "Select some apps to uninstall first", Toast.LENGTH_SHORT).show();
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
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
            Functions.shareApp(this);
            return true;
        } else if (id == R.id.action_sort) {
            toggleBottomSheet();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleBottomSheet() {
        bottomSheet.show(getSupportFragmentManager(), bottomSheet.getTag());
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ArrayList<AppData> queryResults = getAppsForQuery(newText);
        appListAdapter = new AppListAdapter(app, queryResults);
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

    @Override
    public void onSortSelected(SortType type) {
        sortAppDataBy(type);
        appListAdapter.notifyDataSetChanged();
    }

    private void sortAppDataBy(SortType type) {
        sortType = type;
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

}
