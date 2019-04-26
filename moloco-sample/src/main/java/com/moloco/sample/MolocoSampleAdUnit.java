package com.moloco.sample;

import android.support.annotation.NonNull;

class MolocoSampleAdUnit implements Comparable<MolocoSampleAdUnit> {
    // Note that entries are also sorted in this order
    enum AdType {
        BANNER("Banner");

        String getName() {
            return name;
        }

        private final String name;

        AdType(final String name) {
            this.name = name;
        }
    }

    static class Builder {
        private final String mAdUnitId;
        private final AdType mAdType;

        private String mDescription;
        private long mId;

        Builder(final String adUnitId, final AdType adType) {
            mAdUnitId = adUnitId;
            mAdType = adType;
            mId = -1;
        }

        Builder description(final String description) {
            mDescription = description;
            return this;
        }

        Builder id(final long id) {
            mId = id;
            return this;
        }

        MolocoSampleAdUnit build() {
            return new MolocoSampleAdUnit(this);
        }
    }

    private final String mAdUnitId;
    private final AdType mAdType;
    private final String mDescription;
    private final long mId;

    private MolocoSampleAdUnit(final Builder builder) {
        mAdUnitId = builder.mAdUnitId;
        mAdType = builder.mAdType;
        mDescription = builder.mDescription;
        mId = builder.mId;
    }

    String getAdUnitId() {
        return mAdUnitId;
    }

    String getDescription() {
        return mDescription;
    }

    String getHeaderName() {
        return mAdType.name;
    }

    long getId() {
        return mId;
    }

    @Override
    public int compareTo(@NonNull MolocoSampleAdUnit that) {
        if (mAdType != that.mAdType) {
            return mAdType.ordinal() - that.mAdType.ordinal();
        }

        return mDescription.compareTo(that.mDescription);
    }

    @Override
    public int hashCode() {
        int result = 11;
        result = 31 * result + mAdType.ordinal();
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mAdUnitId.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        if (!(o instanceof MolocoSampleAdUnit)) {
            return false;
        }

        final MolocoSampleAdUnit that = (MolocoSampleAdUnit) o;

        return that.mAdType.equals(this.mAdType) &&
                that.mDescription.equals(this.mDescription) &&
                that.mAdUnitId.equals(this.mAdUnitId);
    }
}
