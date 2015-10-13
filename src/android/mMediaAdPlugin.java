package com.rjfun.cordova.mmedia;

import org.json.JSONObject;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.util.Log;

import com.rjfun.cordova.ad.GenericAdPlugin;
// http://docs.millennialmedia.com/android-ad-sdk/apidocs/classes.html
import com.millennialmedia.InterstitialAd;
import com.millennialmedia.MMException;
// import com.millennialmedia.*;


public class mMediaAdPlugin extends GenericAdPlugin {

    private static final String LOGTAG = "mMedia";

    private static final String TEST_BANNER_ADID = "177367";
    private static final String TEST_INTERSTITIAL_ADID = "177369";

    private static final String OPT_IGNORE_SCALING = "ignoreScaling";
    private static final String OPT_TRANSITION_TYPE = "transitionType";

    private float screenDensity = 1.0f;
    private boolean ignoreScaling = false;
    private int transitionType = 0; // MMAdView.TRANSITION_NONE;

    // Constants for tablet sized ads (728x90)
    // private static final int IAB_LEADERBOARD_WIDTH = 728;
    // private static final int IAB_LEADERBOARD_HEIGHT = 90;

    // private static final int MED_BANNER_WIDTH = 480;
    // private static final int MED_BANNER_HEIGHT = 60;

    // Constants for phone sized ads (320x50)
    // private static final int BANNER_AD_WIDTH = 320;
    // private static final int BANNER_AD_HEIGHT = 50;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        // adWidth = BANNER_AD_WIDTH;
        // adHeight = BANNER_AD_HEIGHT;

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenDensity = metrics.density;
    }

    @Override
    protected String __getProductShortName() {
        return "mMedia";
    }

    @Override
    protected String __getTestBannerId() {
        return TEST_BANNER_ADID;
    }

    @Override
    protected String __getTestInterstitialId() {
        return TEST_INTERSTITIAL_ADID;
    }

    @Override
    public void setOptions(JSONObject options) {
        super.setOptions(options);

        if (options.has(OPT_IGNORE_SCALING))  this.ignoreScaling  = options.optBoolean( OPT_IGNORE_SCALING );
        if (options.has(OPT_TRANSITION_TYPE)) this.transitionType = options.optInt( OPT_TRANSITION_TYPE );
    }

    protected boolean canFit(int adWidth) {
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int adWidthPx = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, adWidth, metrics);
        return metrics.widthPixels >= adWidthPx;
    }

    // Banners

    @Override
    protected View __createAdView(String adId) {
        if (isTesting) adId = TEST_BANNER_ADID;
        /*
        MMAdView ad = new MMAdView(getActivity());
        ad.setApid( adId );
        ad.setMMRequest(new MMRequest());
        ad.setId(MMSDK.getDefaultAdId());
        ad.setIgnoresDensityScaling(ignoreScaling);
        ad.setTransitionType(transitionType);

        // Finds an ad that best fits a users device.
        if (canFit(MED_BANNER_WIDTH)) {
            adWidth = MED_BANNER_WIDTH;
            adHeight = MED_BANNER_HEIGHT;
        } else if (canFit(IAB_LEADERBOARD_WIDTH)) {
            adWidth = IAB_LEADERBOARD_WIDTH;
            adHeight = IAB_LEADERBOARD_HEIGHT;
        }
        ad.setWidth(adWidth);
        ad.setHeight(adHeight);

        ad.setListener(new RequestListener(){
            @Override
            public void MMAdOverlayClosed(MMAd arg0) {
                fireAdEvent(EVENT_AD_DISMISS, ADTYPE_BANNER);
            }

            @Override
            public void MMAdOverlayLaunched(MMAd arg0) {
                fireAdEvent(EVENT_AD_PRESENT, ADTYPE_BANNER);
            }

            @Override
            public void MMAdRequestIsCaching(MMAd arg0) {
                fireAdEvent(EVENT_AD_WILLPRESENT, ADTYPE_BANNER);
            }

            @Override
            public void onSingleTap(MMAd arg0) {
                fireAdEvent(EVENT_AD_LEAVEAPP, ADTYPE_BANNER);
            }

            @Override
            public void requestCompleted(MMAd arg0) {
                if ((!bannerVisible) && autoShowBanner) {
                    showBanner(adPosition, posX, posY);
                }
                fireAdEvent(EVENT_AD_LOADED, ADTYPE_BANNER);
            }

            @Override
            public void requestFailed(MMAd arg0, MMException arg1) {
                fireAdErrorEvent(EVENT_AD_FAILLOAD, arg1.getCode(), arg1.getLocalizedMessage(), ADTYPE_BANNER);
            }

        });

        return ad; */
        return null;
    }

    @Override
    protected int __getAdViewWidth(View view) {
        return (int) (adWidth * screenDensity);
    }

    @Override
    protected int __getAdViewHeight(View view) {
        return (int) (adHeight * screenDensity);
    }

    @Override
    protected void __loadAdView(View view) {
        /*if (view instanceof MMAdView) {
            MMAdView ad = (MMAdView) view;
            ad.getAd();
        }*/
    }

    @Override
    protected void __pauseAdView(View view) {
        /*if (view instanceof MMAdView) {
            MMAdView ad = (MMAdView) view;
            // ad.pause();
        }*/
    }

    @Override
    protected void __resumeAdView(View view) {
        /*if (view instanceof MMAdView) {
            MMAdView ad = (MMAdView) view;
            // ad.resume();
        }*/
    }

    @Override
    protected void __destroyAdView(View view) {
        /*if (view instanceof MMAdView) {
            MMAdView ad = (MMAdView) view;
            // ad.destroy();
        }*/
    }

    // Interstitials

    @Override
    protected Object __createInterstitial(String adId) {
        if (isTesting) adId = TEST_INTERSTITIAL_ADID;

        InterstitialAd ad = null;
        try {
            ad = InterstitialAd.createInstance(adId);
            ad.setListener(new InterstitialAd.InterstitialListener() {
                @Override
                public void onLoaded(InterstitialAd interstitial) {
                    Log.i(LOGTAG, "Interstitial Ad loaded.");
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            if (autoShowInterstitial) {
                                showInterstitial();
                            }
                            fireAdEvent(EVENT_AD_LOADED, ADTYPE_INTERSTITIAL);
                        }
                    });
                }

                @Override
                public void onLoadFailed(InterstitialAd interstitial, final InterstitialAd.InterstitialErrorStatus errorStatus) {
                    Log.i(LOGTAG, "Interstitial Ad load failed.");
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fireAdErrorEvent(EVENT_AD_FAILLOAD, errorStatus.getErrorCode(), errorStatus.getDescription(), ADTYPE_INTERSTITIAL);
                        }
                    });
                }

                @Override
                public void onShown(InterstitialAd interstitial) {
                    Log.i(LOGTAG, "Interstitial Ad shown.");
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fireAdEvent(EVENT_AD_PRESENT, ADTYPE_INTERSTITIAL);
                        }
                    });
                }

                @Override
                public void onShowFailed(InterstitialAd interstitial, final InterstitialAd.InterstitialErrorStatus errorStatus) {
                    Log.i(LOGTAG, "Interstitial Ad show failed.");
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fireAdErrorEvent(EVENT_AD_FAILLOAD, errorStatus.getErrorCode(), errorStatus.getDescription(), ADTYPE_INTERSTITIAL);
                        }
                    });
                }

                @Override
                public void onClosed(InterstitialAd interstitial) {
                    Log.i(LOGTAG, "Interstitial Ad closed.");
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fireAdEvent(EVENT_AD_DISMISS, ADTYPE_INTERSTITIAL);
                        }
                    });
                }

                @Override
                public void onClicked(InterstitialAd interstitial) {
                    Log.i(LOGTAG, "Interstitial Ad clicked.");
                }

                @Override
                public void onAdLeftApplication(InterstitialAd interstitial) {
                    Log.i(LOGTAG, "Interstitial Ad left application.");
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fireAdEvent(EVENT_AD_LEAVEAPP, ADTYPE_INTERSTITIAL);
                        }
                    });
                }

                @Override
                public void onExpired(InterstitialAd interstitial) {
                    Log.i(LOGTAG, "Interstitial Ad expired.");
                    cordova.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            fireAdErrorEvent(EVENT_AD_FAILLOAD, 0, "Interstitial request expired", ADTYPE_INTERSTITIAL);
                        }
                    });
                }
            });
        }
        catch (MMException e) {
            Log.i(LOGTAG, "Unable to create interstitial ad, exception occurred");
            e.printStackTrace();
        }
        return ad;
    }

    @Override
    protected void __loadInterstitial(Object interstitial) {
        if (interstitial instanceof InterstitialAd) {
            InterstitialAd ad = (InterstitialAd) interstitial;
            ad.load(getActivity(), null);
        }
    }

    @Override
    protected void __showInterstitial(Object interstitial) {
        if (interstitial instanceof InterstitialAd) {
            InterstitialAd ad = (InterstitialAd) interstitial;
            if (!ad.isReady()) Log.w(LOGTAG, "Unable to show interstitial. Ad not loaded.");
            else {
                try {
                    ad.show(getActivity());
                } catch (MMException e) {
                    Log.i(LOGTAG, "Unable to show interstitial ad content, exception occurred");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void __destroyInterstitial(Object interstitial) {
        if (interstitial instanceof InterstitialAd) {
            InterstitialAd ad = (InterstitialAd) interstitial;
            // ad.destroy();
        }
    }
}
