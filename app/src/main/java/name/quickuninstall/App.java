package name.quickuninstall;

import android.app.Application;
import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.net.Uri;

import java.util.ArrayList;

import name.quickuninstall.adapters.AppListAdapter;
import name.quickuninstall.apps.AppData;
import name.quickuninstall.misc.SortType;
import name.quickuninstall.misc.Sorters;

/**
 * Created by jordanholland on 2017/04/09.
 */

public class App extends Application {

    AppListAdapter appListAdapter;

    // A list of apps that have been selected by the user, is observable
    public ObservableList<AppData> selectedApps = new ObservableArrayList<>();

    // The current sort type being used to list the apps in order
    public SortType sortType = SortType.NAME;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // Adds an app to the selected list
    public void addSelectedApp(AppData data) {
        selectedApps.add(data);
        if (appListAdapter != null) appListAdapter.notifyDataSetChanged();
    }

    // Removes an app from the selected list
    public void removeSelectedApp(AppData data) {
        selectedApps.remove(data);
        if (appListAdapter != null) appListAdapter.notifyDataSetChanged();
    }

    // Replaces the current selected list with the passed list
    public void replaceSelectedApps(ArrayList<AppData> newAppData) {
        selectedApps.clear();
        selectedApps.addAll(newAppData);
        if (appListAdapter != null) appListAdapter.notifyDataSetChanged();
    }

    // Checks whether an app is in the selected list
    public boolean appIsSelected(AppData data) {
        return selectedApps.contains(data);
    }

    public boolean canUninstallApps() {
        return selectedApps.size() > 0;
    }

    // Uninstalls all the apps on the selected list then clears it
    public void uninstallSelectedApps() {
        for (AppData data : selectedApps) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + data.packageName));
            startActivity(intent);
        }
        selectedApps.clear();
    }

}
