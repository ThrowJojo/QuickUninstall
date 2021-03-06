package breakingscope.quickuninstall.apps;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import java.util.ArrayList;
import java.util.List;

public class PackagesHandler {

    // Get a list of installed packages(excluding the current package/system packages)
    public static ArrayList<AppData> getInstalledPackages(Context context) {

        ArrayList<AppData> installedPackages = new ArrayList<>();
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);

        for (PackageInfo packageInfo : packages) {
            if (!isValidPackage(context, packageInfo)) continue;
            AppData data = new AppData(context, packageInfo);
            installedPackages.add(data);
        }

        return installedPackages;

    }

    // Gets the number of installed non-system apps
    public static int getNumberOfInstalledApps(Context context) {
        int counter = 0;
        List<PackageInfo> packages = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if (!isValidPackage(context, packageInfo)) continue;
            counter++;
        }
        return counter;
    }

    // Check if the package is the current package, or if it is a system package
    private static boolean isValidPackage(Context context, PackageInfo info) {
        return !info.packageName.equals(context.getPackageName()) && !info.packageName.startsWith("com.android") && ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0);
    }

}
