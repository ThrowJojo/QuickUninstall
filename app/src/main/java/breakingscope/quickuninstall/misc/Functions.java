package breakingscope.quickuninstall.misc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import breakingscope.quickuninstall.R;

public class Functions {

    // Opens the Google Play store for an app using it's package yourname
    public static void showAppListing(Context context, String packageName) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
        }
    }

    // Shows a dialog to let a chooser share your app on other apps
    public static void shareApp(Context context) {
        String shareText = context.getString(R.string.share_text) + " https://play.google.com/store/apps/details?id=" + context.getPackageName();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(share, context.getString(R.string.share_chooser)));
    }

}
