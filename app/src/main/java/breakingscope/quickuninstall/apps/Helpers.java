package breakingscope.quickuninstall.apps;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import breakingscope.quickuninstall.R;

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

    // Reads a property from the Config file
    public static String readConfigValue(Context context, String key) {
        Resources resources = context.getResources();
        try {
            InputStream inputStream = resources.openRawResource(R.raw.config);
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
