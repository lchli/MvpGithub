package com.lchli.angithub.common.base;

public abstract class BaseReactFragment extends BaseFragment {
    private ReactRootView mReactRootView;
    private ReactInstanceManager mReactInstanceManager;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mReactRootView = new ReactRootView(activity);
        mReactInstanceManager = ((MyApplication) getActivity().getApplication()).getReactNativeHost().getReactInstanceManager();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return mReactRootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mReactRootView.startReactApplication(mReactInstanceManager, getMainPageName(), null);
    }
    protected abstract String getMainPageName();
    protected void sendEvent(String eventName,
                             @Nullable WritableMap params) {
        if (((MyApplication) getActivity().getApplication()).getReactContext() != null) {
            ((MyApplication) getActivity().getApplication()).getReactContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }
}