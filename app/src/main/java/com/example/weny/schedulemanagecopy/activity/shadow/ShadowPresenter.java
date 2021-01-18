package com.example.weny.schedulemanagecopy.activity.shadow;

public class ShadowPresenter implements IShadowPresenter {
    IShadowView iShadowView;
    IShadowMode iShadowMode;

    public ShadowPresenter(IShadowView iShadowView, IShadowMode iShadowMode) {
        this.iShadowView = iShadowView;
        this.iShadowMode = iShadowMode;
    }
}
