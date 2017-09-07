package breakingscope.quickuninstall.adapters;

import breakingscope.quickuninstall.apps.AppData;

/**
 * Created by jordanholland on 2017/09/07.
 */

public interface AppListAdapterListener {
    void onSelectionChanged(AppData data, boolean value);
}
