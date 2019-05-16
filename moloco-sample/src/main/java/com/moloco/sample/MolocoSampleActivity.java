package com.moloco.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.jakewharton.rxbinding2.view.RxView;
import com.moloco.ads.MolocoView;
import com.moloco.ads.MolocoView.BannerAdListener;
import com.moloco.nativeads.MolocoNative;
import com.moloco.nativeads.MolocoNative.NativeAdListener;
import com.moloco.common.Moloco;
import com.moloco.common.MolocoErrorCode;
import com.moloco.common.SdkConfiguration;
import com.moloco.network.DisposableManager;
import com.moloco.network.MolocoNativeAdResponse;

import com.squareup.picasso.Picasso;

import static com.moloco.common.logging.MLog.LogLevel;
import static com.moloco.common.logging.MLog.LogLevel.DEBUG;
import static com.moloco.common.logging.MLog.LogLevel.INFO;
import static com.moloco.sample.MolocoSampleAdUnit.AdType.BANNER;
import static com.moloco.sample.Utils.logToast;

public class MolocoSampleActivity extends AppCompatActivity implements BannerAdListener {
    private MolocoView mMolocoView;
    private MolocoSampleAdUnit mMolocoSampleAdUnit;
    private MolocoView mMolocoSquareView;
    private MolocoSampleAdUnit mMolocoSquareSampleAdUnit;
    private MolocoView mMolocoMiddleView;
    private MolocoSampleAdUnit mMolocoMiddleSampleAdUnit;
    private MolocoNative mMolocoMiddleNative;
    private MolocoNative mMolocoRollingNative;
    private MolocoNative mMolocoSquareNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moloco_sample);

        LogLevel logLevel = INFO;
        if (BuildConfig.DEBUG) {
            logLevel = DEBUG;
        }
        final SdkConfiguration sdkConfiguration = new SdkConfiguration(logLevel);

        Moloco.initializeSdk(this, sdkConfiguration);

        // 1. Banner
        //----------------------------------------------
        mMolocoView = findViewById(R.id.banner_molocoview);
        mMolocoSampleAdUnit = new MolocoSampleAdUnit
                .Builder(getString(R.string.ad_unit_id_rolling_banner), BANNER)
                .description("Moloco 720x160 Banner Sample")
                .build();

        mMolocoSquareView = findViewById(R.id.banner_molocosquareview);
        mMolocoSquareSampleAdUnit = new MolocoSampleAdUnit
                .Builder(getString(R.string.ad_unit_id_square_banner), BANNER)
                .description("Moloco 238x238 Square Banner Sample")
                .build();

        Button loadBtn = findViewById(R.id.banner_button);
        final String adUnitId = mMolocoSampleAdUnit.getAdUnitId();
        final String squareAdUnitId = mMolocoSquareSampleAdUnit.getAdUnitId();

        RxView.clicks(loadBtn)
                .subscribe(s -> {
                    loadMolocoView(mMolocoView, adUnitId);
                    loadMolocoView(mMolocoSquareView, squareAdUnitId);
                });

        Button loadMiddleBannerBtn = findViewById(R.id.middle_banner_button);
        mMolocoMiddleView = findViewById(R.id.banner_molocomiddleview);
        mMolocoMiddleSampleAdUnit = new MolocoSampleAdUnit
                .Builder(getString(R.string.ad_unit_id_middle_banner), BANNER)
                .description("Moloco 720x116 Middle Banner Sample")
                .build();
        final String middleAdUnitId = mMolocoMiddleSampleAdUnit.getAdUnitId();

        RxView.clicks(loadMiddleBannerBtn)
                .subscribe(s -> {
                    loadMolocoView(mMolocoMiddleView, middleAdUnitId);
                });

        mMolocoView.setBannerAdListener(this);
        mMolocoSquareView.setBannerAdListener(this);
        mMolocoMiddleView.setBannerAdListener(this);
        //----------------------------------------------

        // 2. Native
        // ----------------------------------------------
        mMolocoMiddleNative = new MolocoNative(this, getString(R.string.ad_unit_id_middle_native));
        ImageView molocoMiddleNativeView = (ImageView) findViewById(R.id.middle_native_image_view);
        SampleNativeListener molocoMiddleNativeListener = new SampleNativeListener(molocoMiddleNativeView);
        mMolocoMiddleNative.setAdListener(molocoMiddleNativeListener);
        Button loadMiddleNativeBannerBtn = findViewById(R.id.middle_native_button);
        RxView.clicks(loadMiddleNativeBannerBtn)
                .subscribe(s -> {
                    mMolocoMiddleNative.loadAd();
                });

        mMolocoRollingNative = new MolocoNative(this, getString(R.string.ad_unit_id_rolling_native));
        ImageView molocoRollingNativeView = (ImageView) findViewById(R.id.rolling_native_image_view);
        SampleNativeListener molocoRollingNativeListener = new SampleNativeListener(molocoRollingNativeView);
        mMolocoRollingNative.setAdListener(molocoRollingNativeListener);

        mMolocoSquareNative = new MolocoNative(this, getString(R.string.ad_unit_id_square_native));
        ImageView molocoSquareNativeView = (ImageView) findViewById(R.id.square_native_image_view);
        SampleNativeListener molocoSquareNativeListener = new SampleNativeListener(molocoSquareNativeView);
        mMolocoSquareNative.setAdListener(molocoSquareNativeListener);

        Button loadNativeBannerBtn = findViewById(R.id.rolling_native_button);
        RxView.clicks(loadNativeBannerBtn)
                .subscribe(s -> {
                    mMolocoRollingNative.loadAd();
                    mMolocoSquareNative.loadAd();
                });
        // ----------------------------------------------
    }

    @Override
    protected void onDestroy() {
        DisposableManager.dispose();
        super.onDestroy();
    }

    private void loadMolocoView(final MolocoView mView, final String adUnitId) {
        mView.setAdUnitId(adUnitId);
        mView.loadAd();
    }

    private String getName() {
        if (mMolocoSampleAdUnit == null) {
            return BANNER.getName();
        }
        return mMolocoSampleAdUnit.getHeaderName();
    }

    @Override
    public void onBannerLoaded(MolocoView banner) {
        logToast(this, getName() + " loaded.");
    }

    @Override
    public void onBannerFailed(MolocoView banner, MolocoErrorCode errorCode) {
        final String errorMessage = (errorCode != null) ? errorCode.toString() : "";
        logToast(this, banner.getAdUnitId() + ":" + getName() + " failed to load: " + errorMessage);
    }

    @Override
    public void onBannerClicked(MolocoView banner) {
        logToast(this, getName() + " clicked.");
    }

    @Override
    public void onBannerExpanded(MolocoView banner) {
        logToast(this, getName() + " expanded.");
    }

    @Override
    public void onBannerCollapsed(MolocoView banner) {
        logToast(this, getName() + " collapsed.");
    }

    private class SampleNativeListener implements NativeAdListener {
        private ImageView mImageView;

        SampleNativeListener(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        public void onNativeLoaded(final MolocoNativeAdResponse nativeAdResponse) {
            String imageURL = nativeAdResponse.getMainimage();
            Picasso.get().load(imageURL).into(mImageView);
            logToast(getBaseContext(), getName() + " loaded.: " + nativeAdResponse.toString());
            logToast(getBaseContext(), "Campaign ID: " + nativeAdResponse.getCid());
            logToast(getBaseContext(), "main image url: " + nativeAdResponse.getMainimage());
        }

        @Override
        public void onNativeFailed(final MolocoErrorCode errorCode) {
            final String errorMessage = (errorCode != null) ? errorCode.toString() : "";
            logToast(getBaseContext(), getName() + " failed to load: " + errorMessage);
        }
    }
}
