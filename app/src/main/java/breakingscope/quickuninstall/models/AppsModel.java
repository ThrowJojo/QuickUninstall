package breakingscope.quickuninstall.models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.net.Uri;

import java.util.ArrayList;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;
import breakingscope.quickuninstall.apps.AppData;
import breakingscope.quickuninstall.apps.PackagesHandler;
import breakingscope.quickuninstall.misc.SortType;
import breakingscope.quickuninstall.misc.Sorters;

/**
 * Created by jordanholland on 2017/09/07.
 */

public class AppsModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<AppData>> installedApps = new MutableLiveData<>();
    private MutableLiveData<ArrayList<AppData>> displayedApps = new MutableLiveData<>();
    private MutableLiveData<ArrayList<AppData>> selectedApps = new MutableLiveData<>();

    // The current sort type being used to list the apps in order
    public SortType sortType = SortType.NAME;

    public AppsModel(Application application) {
        super(application);
        installedApps.setValue(new ArrayList<AppData>());
        displayedApps.setValue(new ArrayList<AppData>());
        selectedApps.setValue(new ArrayList<AppData>());
    }

    public LiveData<ArrayList<AppData>> getDisplayedApps() {
        return displayedApps;
    }

    public LiveData<ArrayList<AppData>> getSelectedApps() {
        return selectedApps;
    }

    // Sorts displayed apps
    public void sortDisplayedApps(SortType type) {
        this.sortType = type;
        if (displayedApps.getValue() == null) return;
        sortAppDataBy(displayedApps.getValue(), type);
        displayedApps.setValue(displayedApps.getValue());
    }

    // Changes the selected sortType then sorts the appData ArrayList based on the value
    private void sortAppDataBy(ArrayList<AppData> data, SortType type) {
        switch (type) {
            case NAME:
                Sorters.byName(data);
                break;
            case DATE:
                Sorters.byInstalledDate(data);
                break;
            case FILE_SIZE:
                Sorters.bySize(data);
                break;
            default:
                Sorters.byName(data);
                break;
        }
    }

    // Checks if the list of installed apps has to be reloaded or not
    public boolean uninstallCompleted() {
        if (installedApps.getValue() != null && installedApps.getValue().size() != PackagesHandler.getNumberOfInstalledApps(this.getApplication())) {
            selectedApps.setValue(new ArrayList<AppData>());
            displayedApps.setValue(new ArrayList<AppData>());
            installedApps.setValue(new ArrayList<AppData>());
            loadInstalledApps();
            return true;
        }
        return false;
    }

    // Loads the installed apps list asynchronously
    public void loadInstalledApps() {
        fetchInstalledApps().continueWith(new Continuation<ArrayList<AppData>, Void>() {
            @Override
            public Void then(Task<ArrayList<AppData>> task) throws Exception {
                ArrayList<AppData> result = task.getResult();
                sortAppDataBy(result, sortType);
                displayedApps.setValue(result);
                installedApps.setValue(result);
                return null;
            }
        });
    }

    // Adds an app to the selected list
    public void addSelectedApp(AppData data) {
        if (selectedApps.getValue() != null) {
            selectedApps.getValue().add(data);
            selectedApps.setValue(selectedApps.getValue());
        }
    }

    // Removes an app from the selected list
    public void removeSelectedApp(AppData data) {
        if (selectedApps.getValue() != null) {
            selectedApps.getValue().remove(data);
            selectedApps.setValue(selectedApps.getValue());
        }
    }

    // Method to select every installed app
    public void selectAllApps() {
        if (selectedApps.getValue() != null && displayedApps.getValue() != null) {
            selectedApps.setValue(displayedApps.getValue());
        }
    }

    // Method to deselect every selected app
    public void deselectAllApps() {
        if (selectedApps.getValue() != null) {
            selectedApps.setValue(new ArrayList<AppData>());
        }
    }

    // Checks if there is at least one app selected
    public boolean canUninstallApps() {
        return (selectedApps.getValue() != null && selectedApps.getValue().size() > 0);
    }

    // Shows uninstall prompts for all selected apps
    public void uninstallSelectedApps() {
        if (selectedApps.getValue() == null) return;
        for (AppData data : selectedApps.getValue()) {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:" + data.packageName));
            getApplication().startActivity(intent);
        }
    }

    private Task<ArrayList<AppData>> fetchInstalledApps() {
        final TaskCompletionSource<ArrayList<AppData>> completion = new TaskCompletionSource<>();
        completion.setResult(PackagesHandler.getInstalledPackages(this.getApplication()));
        return completion.getTask();
    }

    // Get apps for a particular search query
    public void processSearchQuery(String query) {
        if (installedApps.getValue() == null || displayedApps.getValue() == null) return;
        ArrayList<AppData> results = new ArrayList<>();
        for (AppData data : installedApps.getValue()) if (data.label.toLowerCase().contains(query.toLowerCase())) results.add(data);
        displayedApps.setValue(results);
    }

}
