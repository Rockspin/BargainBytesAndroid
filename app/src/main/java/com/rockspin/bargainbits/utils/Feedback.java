package com.rockspin.bargainbits.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

/**
 * http://stackoverflow.com/questions/10812432/how-to-use-send-feeback-feedbackactivity-in-android
 * really good post about how this class works.
 */
public final class Feedback {

    public static void sendEmailFeedback(final Context context) {
        try {
            final Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
            intent.setData(Uri.parse("mailto:" + "support@rockspin.co.uk"));
            // put device details in the subject
            intent.putExtra(Intent.EXTRA_SUBJECT, context.getApplicationContext()
                                                         .getApplicationInfo()
                                                         .loadLabel(context.getApplicationContext()
                                                                           .getPackageManager())
                                                         .toString() + "(" + context.getPackageManager()
                                                                                    .getPackageInfo(context.getApplicationInfo().packageName, 0).versionName + ")"
                + " Contact Form | Device: " + Build.MANUFACTURER + " " + Build.DEVICE + "(" + Build.MODEL + ") API: " + Build.VERSION.SDK_INT);

            context.startActivity(intent);
        } catch (final PackageManager.NameNotFoundException | ActivityNotFoundException ignored) {
        }
    }
}
