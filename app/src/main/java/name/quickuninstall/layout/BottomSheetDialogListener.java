package name.quickuninstall.layout;

import name.quickuninstall.misc.SortType;

/**
 * Created by jordanholland on 2017/04/10.
 */

// A delegate for when a sort option is selected
public interface BottomSheetDialogListener {
    void onSortSelected(SortType type);
}
