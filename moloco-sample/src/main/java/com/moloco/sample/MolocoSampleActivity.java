package com.moloco.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.moloco.ads.MolocoView;
import com.moloco.ads.MolocoView.BannerAdListener;
import com.moloco.common.Moloco;
import com.moloco.common.MolocoErrorCode;
import com.moloco.common.SdkConfiguration;
import com.moloco.network.DisposableManager;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moloco_sample);

        LogLevel logLevel = INFO;
        if (BuildConfig.DEBUG) {
            logLevel = DEBUG;
        }
        final SdkConfiguration sdkConfiguration = new SdkConfiguration(
                "b195f8dd8ded45fe847ad89ed1d016da",
                logLevel
        );

        Moloco.initializeSdk(this, sdkConfiguration);

        //----------------------------------------------
        mMolocoView = findViewById(R.id.banner_molocoview);
        mMolocoView.setWidthPixel(720);
        mMolocoView.setHeightPixel(160);
        mMolocoSampleAdUnit = new MolocoSampleAdUnit
                .Builder(getString(R.string.ad_unit_id_rolling_banner), BANNER)
                .description("Moloco 720x160 Banner Sample")
                .build();

        mMolocoSquareView = findViewById(R.id.banner_molocosquareview);
        mMolocoSquareView.setWidthPixel(238);
        mMolocoSquareView.setHeightPixel(238);
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
        mMolocoMiddleView.setWidthPixel(720);
        mMolocoMiddleView.setHeightPixel(116);
        mMolocoMiddleSampleAdUnit = new MolocoSampleAdUnit
                .Builder(getString(R.string.ad_unit_id_middle_banner), BANNER)
                .description("Moloco 720x116 Middle Banner Sample")
                .build();
        final String middleAdUnitId = mMolocoMiddleSampleAdUnit.getAdUnitId();
        RxView.clicks(loadMiddleBannerBtn)
                .subscribe(s -> { loadMolocoView(mMolocoMiddleView, middleAdUnitId); });

        mMolocoView.setBannerAdListener(this);
        mMolocoSquareView.setBannerAdListener(this);
        mMolocoMiddleView.setBannerAdListener(this);
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
}
