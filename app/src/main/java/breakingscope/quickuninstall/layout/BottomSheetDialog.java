package breakingscope.quickuninstall.layout;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;

import breakingscope.quickuninstall.App;
import breakingscope.quickuninstall.databinding.SheetSortBinding;
import breakingscope.quickuninstall.misc.SortType;

public class BottomSheetDialog extends BottomSheetDialogFragment {

    public BottomSheetDialogListener listener;
    public App app;

    SheetSortBinding binding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = SheetSortBinding.inflate(inflater);
        binding.setDialog(this);
        dialog.setContentView(binding.getRoot());
        refreshSortChoice();
        return dialog;
    }

    // Refreshes the UI for the current sorting option
    public void refreshSortChoice() {
        if (app == null) return;
        binding.sortDate.setAlpha((app.sortType == SortType.DATE) ? 0.5f : 1);
        binding.sortName.setAlpha((app.sortType == SortType.NAME) ? 0.5f : 1);
        binding.sortSize.setAlpha((app.sortType == SortType.FILE_SIZE) ? 0.5f : 1);
    }

    // Binded method for when Sort By Name is selected
    public void onSortDateClicked() {
        if (listener != null) listener.onSortSelected(SortType.DATE);
        dismiss();
    }

    // Binded method for when Sort By Name is selected
    public void onSortNameClicked() {
        if (listener != null) listener.onSortSelected(SortType.NAME);
        dismiss();
    }

    // Binded method for when Sort By Size is selected
    public void onSortSizeClicked() {
        if (listener != null) listener.onSortSelected(SortType.FILE_SIZE);
        dismiss();
    }

}
