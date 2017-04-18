package com.rockspin.apputils.cache;

import com.fernandocejas.arrow.checks.Preconditions;
import java.io.Serializable;

public final class ObjectAndSize<V extends Serializable>{
    public final V mValue;
    public final int mSizeInBytes;

    public ObjectAndSize(final V pValue, final int pSizeInBytes) {
        Preconditions.checkNotNull(pValue, "Value canot be null");
        Preconditions.checkArgument(pSizeInBytes >0, "size cannot be zero");
        this.mValue = pValue;
        this.mSizeInBytes = pSizeInBytes;
    }
}