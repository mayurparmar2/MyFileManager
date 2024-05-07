package com.demo.filemanager

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class AdsGoogle(activity: Activity) {
    init {
        MobileAds.initialize(activity) {
            // Initialization callback
        }
    }

    fun Interstitial_Show(activity: Activity) {
        Ad_Popup(activity)
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(activity, AD_Interstitial_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.show(activity)
                    progressDialog?.dismiss()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    progressDialog?.dismiss()
                }
            })
    }

    fun setCounter_Ads(mContext: Context, string: Int) {
        mContext.getSharedPreferences(mContext.packageName, 0).edit()
            .putInt(Counter_Ads, string).apply()
    }

    fun getCounter_Ads(mContext: Context): Int {
        return mContext.getSharedPreferences(mContext.packageName, 0)
            .getInt(Counter_Ads, 1)
    }

    fun Interstitial_Show_Counter(activity: Activity) {
        var counter_ads = getCounter_Ads(activity)
        if (counter_ads >= 3) {
            setCounter_Ads(activity, 1)
            try {
                Ad_Popup(activity)
                val adRequest = AdRequest.Builder().build()
                InterstitialAd.load(activity, AD_Interstitial_ID, adRequest,
                    object : InterstitialAdLoadCallback() {
                        override fun onAdLoaded(interstitialAd: InterstitialAd) {
                            interstitialAd.show(activity)
                            progressDialog?.dismiss()
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            progressDialog?.dismiss()
                        }
                    })

            } catch (e: Exception) {
                // Handle exception
            }
        } else {
            counter_ads++
            setCounter_Ads(activity, counter_ads)
        }
    }

    private fun Ad_Popup(mContext: Context) {
        progressDialog = ProgressDialog.show(mContext, "Please Wait...", "Loading Ads", true)
        progressDialog?.setCancelable(true)
        progressDialog?.show()
    }

    fun Banner_Show(adLayout: RelativeLayout, activity: Activity) {
        val mAdView = AdView(activity).apply {
            adSize.apply {
                AdSize.BANNER
            }
            adUnitId = AD_Banner_ID
            val adRequest = AdRequest.Builder().build()
            loadAd(adRequest)
        }

        adLayout.addView(mAdView)
        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                adLayout.visibility = View.VISIBLE
                Log.e("ddddd", "dddd")
            }

            override fun onAdOpened() {
                super.onAdOpened()
                adLayout.visibility = View.INVISIBLE
                Log.e("ddddd1", "dddd")
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                mAdView.destroy()
                adLayout.visibility = View.INVISIBLE
                Log.e("ddddd2", "dddd" + loadAdError.message)
            }
        }
    }

    companion object {
        const val AD_Banner_ID = "/6499/example/banner"
        const val AD_Interstitial_ID = "/6499/example/interstitial"
        private const val Counter_Ads = "Counter_Ads"
        private var progressDialog: ProgressDialog? = null
    }
}