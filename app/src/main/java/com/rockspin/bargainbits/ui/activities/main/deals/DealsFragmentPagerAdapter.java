package com.rockspin.bargainbits.ui.activities.main.deals;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.rockspin.bargainbits.R;
import com.rockspin.bargainbits.data.repository.DealRepository;
import java.util.Locale;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class DealsFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Resources resources;

    public DealsFragmentPagerAdapter(final FragmentManager fm, final Resources resources) {
        super(fm);
        this.resources = resources;
    }

    @Override public final Fragment getItem(final int position) {
        // getItem is called to instantiate the fragment for the given page.
        final DealRepository.EDealsSorting dealTabType = getDealTabType(position);
        return DealsFragment.create(dealTabType);
    }

    @Override public final int getCount() {
        return 4;
    }

    @Override public final CharSequence getPageTitle(final int position) {
        final String[] dealList = resources.getStringArray(R.array.deal_list_type);
        final Locale l = Locale.getDefault();

        return dealList[position].toUpperCase(l);
    }

    private DealRepository.EDealsSorting getDealTabType(final int position) {
        switch (position) {
            case 0:
                return DealRepository.EDealsSorting.DEALS_RATING;
            case 1:
                return DealRepository.EDealsSorting.RELEASE;
            case 2:
                return DealRepository.EDealsSorting.SAVING;
            case 3:
                return DealRepository.EDealsSorting.PRICE;

            default:
                throw new IllegalStateException("position: " + position + " has no deal sorting" );
        }
    }
}