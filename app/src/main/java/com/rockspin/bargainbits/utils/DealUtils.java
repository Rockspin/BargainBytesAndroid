/*
 * Copyright (c) Rockspin 2014.
 *
 * All Rights Reserved
 */

package com.rockspin.bargainbits.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.text.Html;

import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.models.cheapshark.AbbreviatedDeal;
import com.rockspin.bargainbits.data.models.cheapshark.Deal;

import java.io.File;
import java.io.FileOutputStream;

//TODO: remove this class make Abbreviated deal and deal utils share an interface.
public final class DealUtils {

    private static final String IMAGE_SAVE_DIR = "imageSaveDir";
    private static final String BARGAIN_BYTE_SHARE_PNG = "BargainByteShare.png";

    private DealUtils() { /* not used */ }

    public static CharSequence getFormattedSavingsString(final Context context, final float savings) {
        final String percentageString = Math.round(savings) + "%";
        final String savingString = context.getString(R.string.saving_short);
        return Html.fromHtml("<strong>" + percentageString + "</strong>" + "<br />" + "<small>" + savingString + "</small>");
    }


    public static Deal GameInfo(final AbbreviatedDeal abbreviatedDeal) {
        final Deal deal = new Deal();
        deal.setDealID(abbreviatedDeal.getDealID());
        deal.setStoreID(abbreviatedDeal.getStoreID());
        deal.setSalePrice(abbreviatedDeal.getPrice());
        deal.setNormalPrice(abbreviatedDeal.getRetailPrice());
        deal.setSavings(abbreviatedDeal.getSavingsFraction() * 100.0f);

        return deal;
    }

    /**
     * Converts an abbreviated deal object to a deal object filling all the available information
     *
     * @param abbreviatedDeal AbbreviatedDeal to convert to deal
     * @return A Deal instance containing the abbreviated deal info
     */
    public static Deal abbreviatedDealToDeal(final AbbreviatedDeal abbreviatedDeal) {
        final Deal deal = new Deal();
        deal.setDealID(abbreviatedDeal.getDealID());
        deal.setStoreID(abbreviatedDeal.getStoreID());
        deal.setSalePrice(abbreviatedDeal.getPrice());
        deal.setNormalPrice(abbreviatedDeal.getRetailPrice());
        deal.setSavings(abbreviatedDeal.getSavingsFraction() * 100.0f);

        return deal;
    }

    public static void shareDeal(final Context context, final Bitmap bitmap, final String gameName, final float discount) {
        File directory = new File(context.getFilesDir(), IMAGE_SAVE_DIR);
        directory.mkdirs();

        File imageFile = new File(directory, BARGAIN_BYTE_SHARE_PNG);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imageFile, false);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String packageName = context.getPackageName();

        Uri imageUri = FileProvider.getUriForFile(context, packageName + ".fileprovider", imageFile);
        final Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/png");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

        String shareText;
        if (discount > 0) {
            String discountText = String.valueOf(Math.round(discount)) + "%";
            shareText = context.getString(R.string.share_deal, gameName, discountText, "http://bit.ly/BBargain");
        } else {
            shareText = context.getString(R.string.share_watched_item, gameName, "http://bit.ly/BBargain");
        }

        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        final String title = context.getString(R.string.share_bargain_bytes);
        context.startActivity(Intent.createChooser(shareIntent, title));
    }
}
