package com.xenione.digit;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public abstract class AbstractTabAnimation {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-MainAbility-");

    protected final static int LOWER_POSITION = 0;
    protected final static int MIDDLE_POSITION = 1;
    protected final static int UPPER_POSITION = 2;

    protected final TabDigit.Tab mTopTab;
    protected final TabDigit.Tab mBottomTab;
    protected final TabDigit.Tab mMiddleTab;

    protected int state;
    protected int mAlpha = 0;
    protected long mTime = -1;
    protected float mElapsedTime = 1000.0f;

    public AbstractTabAnimation(TabDigit.Tab mTopTab, TabDigit.Tab mBottomTab, TabDigit.Tab mMiddleTab) {
        this.mTopTab = mTopTab;
        this.mBottomTab = mBottomTab;
        this.mMiddleTab = mMiddleTab;
        initState();
    }

    public void start() {
        HiLog.warn(LABEL_LOG, "AbstractTabAnimation: start "+mTime);
        makeSureCycleIsClosed();
        mTime = System.currentTimeMillis();
    }

    public void sync() {
        makeSureCycleIsClosed();
    }

    public abstract void initState();
    public abstract void initMiddleTab();
    public abstract void run();
    protected abstract void makeSureCycleIsClosed();

}
