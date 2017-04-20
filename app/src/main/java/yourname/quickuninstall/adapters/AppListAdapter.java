package yourname.quickuninstall.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;

import yourname.quickuninstall.App;
import yourname.quickuninstall.apps.AppData;
import yourname.quickuninstall.databinding.RowAppBinding;

/**
 * Created by Climbing Silver on 2017/04/09.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

    private ArrayList<AppData> appData = new ArrayList<>();
    private App app;

    public AppListAdapter(App app, ArrayList<AppData> appData) {
        this.app = app;
        this.appData = appData;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AppData data = appData.get(position);
        holder.binding.icon.setImageDrawable(data.icon);
        holder.binding.nameLabel.setText(data.label);
        holder.binding.packageNameLabel.setText(data.packageName);
        holder.binding.sizeLabel.setText(Formatter.formatShortFileSize(app, data.size));

        // Set the checkbox listener to null so no events are dispatched
        holder.binding.checkbox.setOnCheckedChangeListener(null);

        // Set checked and transparency values, depends on if the item is selected or not
        holder.binding.checkbox.setChecked(app.appIsSelected(data));
        holder.binding.container.setAlpha(app.appIsSelected(data) ? 0.7f : 1);

        holder.binding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (value) {
                    app.addSelectedApp(data);
                } else {
                    app.removeSelectedApp(data);
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
