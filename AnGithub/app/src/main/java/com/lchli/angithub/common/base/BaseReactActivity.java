package com.lchli.angithub.common.base;

public class BaseReactActivity extends BaseActivity implements DefaultHardwareBackBtnHandler {
    /*
     * Get the ReactInstanceManager, AKA the bridge between JS and Android
     * We use a singleton here so we can reuse the instance throughout our app
     * instead of constantly re-instantiating and re-downloading the bundle
     */
    private ReactInstanceManager mReactInstanceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * Get the reference to the ReactInstanceManager
         */
         mReactInstanceManager =
                 ((MyApplication) getActivity().getApplication()).getReactNativeHost().getReactInstanceManager();
    }
    @Override
    public void invokeDefaultOnBackPressed() {
        super.onBackPressed();
    }
    /*
     * Any activity that uses the ReactFragment or ReactActivty
     * Needs to call onHostPause() on the ReactInstanceManager
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostPause();
        }
    }
    /*
     * Same as onPause - need to call onHostResume
     * on our ReactInstanceManager
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (mReactInstanceManager != null) {
            mReactInstanceManager.onHostResume(this, this);
        }
    }
}