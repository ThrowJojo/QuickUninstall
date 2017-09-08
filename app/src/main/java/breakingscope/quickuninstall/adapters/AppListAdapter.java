package breakingscope.quickuninstall.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

import breakingscope.quickuninstall.apps.AppData;
import breakingscope.quickuninstall.databinding.RowAppBinding;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    public ArrayList<AppData> appData = new ArrayList<>();
    public ArrayList<AppData> selectedApps = new ArrayList<>();

    private Context context;
    private AppListAdapterListener listener;

    public AppListAdapter(Context context, ArrayList<AppData> appData, ArrayList<AppData> selectedApps, AppListAdapterListener listener) {
        this.context = context;
        this.appData = appData;
        this.selectedApps = selectedApps;
        this.listener = listener;
    }

    public void updateData(ArrayList<AppData> appData, ArrayList<AppData> selectedApps) {
        this.appData = appData;
        this.selectedApps = selectedApps;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AppData data = appData.get(position);
        holder.binding.icon.setImageDrawable(data.icon);
        holder.binding.nameLabel.setText(data.label);
        holder.binding.packageNameLabel.setText(data.packageName);
        holder.binding.sizeLabel.setText(Formatter.formatShortFileSize(context, data.size));

        // Set the checkbox listener to null so no events are dispatched
        holder.binding.checkbox.setOnCheckedChangeListener(null);

        // Set checked and transparency values, depends on if the item is selected or not
        holder.binding.checkbox.setChecked(selectedApps.contains(data));
        holder.binding.container.setAlpha(selectedApps.contains(data) ? 0.7f : 1);
        holder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (listener != null) {
                    listener.onSelectionChanged(data, value);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return appData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RowAppBinding binding = RowAppBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final RowAppBinding binding;

        public ViewHolder(RowAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }

}
