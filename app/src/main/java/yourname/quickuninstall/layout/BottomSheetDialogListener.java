package yourname.quickuninstall.layout;

import yourname.quickuninstall.misc.SortType;

/**
 * Created by jordanholland on 2017/04/10.
 */

// A delegate for when a sort option is selected
public interface BottomSheetDialogListener {
    void onSortSelected(SortType type);
}
