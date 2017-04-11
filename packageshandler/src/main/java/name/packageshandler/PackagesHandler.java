package name.packageshandler;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jordanholland on 2017/04/09.
 */

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

    // Check if the package is the current package, or if it is a system package
    private static boolean isValidPackage(Context context, PackageInfo info) {
        return !info.packageName.equals(context.getPackageName()) && !info.packageName.startsWith("com.android") && ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0);
    }



}
