package name.quickuninstall.layout;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = SheetSortBinding.inflate(inflater);
        binding.setDialog(this);
        dialog.setContentView(binding.getRoot());
        return dialog;
    }

    public void onSortDateClicked() {
        if (listener != null) listener.onSortSelected(SortType.DATE);
        dismiss();
    }

    public void onSortNameClicked() {
        if (listener != null) listener.onSortSelected(SortType.NAME);
        dismiss();
    }

    public void onSortSizeClicked() {
        if (listener != null) listener.onSortSelected(SortType.FILE_SIZE);
        dismiss();
    }

}
