package breakingscope.quickuninstall.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import breakingscope.quickuninstall.apps.AppData;

public class Sorters {

    // Sorts apps by file size, descending
    public static void bySize(ArrayList<AppData> data) {
        Collections.sort(data, new Comparator<AppData>() {
            @Override
            public int compare(AppData t1, AppData t2) {
                return Long.valueOf(t2.size).compareTo(t1.size);
            }
        });
    }

    // Sorts apps by installed date, descending
    public static void byInstalledDate(ArrayList<AppData> data) {
        Collections.sort(data, new Comparator<AppData>() {
            @Override
            public int compare(AppData t1, AppData t2) {
                return Long.valueOf(t2.installed).compareTo(t1.installed);
            }
        });
    }

    // Sorts apps by name, alphabetically ascending
    public static void byName(ArrayList<AppData> data) {
        Collections.sort(data, new Comparator<AppData>() {
            @Override
            public int compare(AppData t1, AppData t2) {
                return t1.label.compareTo(t2.label);
            }
        });
    }

}
