package name.packageshandler;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by jordanholland on 2017/04/09.
 */

public class AppData {

    public String label = "";
    public String packageName = "";
    public String versionName = "";
    public int versionCode = 0;
    public long size = 0;
    public long installed = 0;
    public Drawable icon;

    public AppData(Context context, PackageInfo info) {
        this.label = info.applicationInfo.loadLabel(context.getPackageManager()).toString();
        this.packageName = info.packageName;
        this.versionName = info.versionName;
        this.versionCode = info.versionCode;
        this.installed = info.firstInstallTime;
        this.size = Helpers.readPackageSize(info);
        this.icon = info.applicationInfo.loadIcon(context.getPackageManager());
    }

    // Used for checking if AppData objects are the same or not
    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != AppData.class) return false;
        AppData otherData = (AppData) obj;
        return otherData.packageName.equals(this.packageName);
    }

}
