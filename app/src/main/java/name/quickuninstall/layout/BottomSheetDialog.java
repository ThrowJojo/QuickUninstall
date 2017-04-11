package name.quickuninstall.layout;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;

import name.quickuninstall.databinding.SheetSortBinding;
import name.quickuninstall.misc.SortType;

/**
 * Created by jordanholland on 2017/04/10.
 */

public class BottomSheetDialog extends BottomSheetDialogFragment {

    public BottomSheetDialogListener listener;

    SheetSortBinding binding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = SheetSortBinding.inflate(inflater);
        binding.setDialog(this);
        dialog.setContentView(binding.getRoot());
        return dialog;
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
