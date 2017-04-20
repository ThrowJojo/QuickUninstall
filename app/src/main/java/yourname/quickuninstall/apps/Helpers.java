package yourname.quickuninstall.apps;

import android.content.pm.PackageInfo;

import java.io.File;

/**
 * Created by Climbing Silver on 2017/04/09.
 */

public class Helpers {

    // Read the file size of a package
    public static long readPackageSize(PackageInfo info) {
        try {
            File file = new File(info.applicationInfo.sourceDir);
            return file.length();
        } catch (Exception e) {
            return 0;
        }
    }

}
