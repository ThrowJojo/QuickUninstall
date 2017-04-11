package name.quickuninstall;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

import name.packageshandler.AppData;

/**
 * Created by jordanholland on 2017/04/09.
 */

public class App extends Application {

    // A list of apps that have been selected by the user
    private ArrayList<AppData> selectedApps = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    // Adds an app to the selected list
    public void addSelectedApp(AppData data) {
        selectedApps.add(data);
    }

    // Removes an app from the selected list
    public void removeSelectedApp(AppData data) {
        selectedApps.remove(data);
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
