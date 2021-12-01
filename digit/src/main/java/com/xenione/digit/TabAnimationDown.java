package com.xenione.digit;


import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * rotates middle tab downwards
 */
public final class TabAnimationDown extends AbstractTabAnimation {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-MainAbility-");

    private static final String TAG = "TabDigit";

    public TabAnimationDown(TabDigit.Tab mTopTab, TabDigit.Tab mBottomTab, TabDigit.Tab mMiddleTab) {
        super(mTopTab, mBottomTab, mMiddleTab);
    }

    public void initState() {
        state = UPPER_POSITION;
    }

    @Override
    public void initMiddleTab() {
        mMiddleTab.rotate(180);
    }

    @Override
    public void run() {

        HiLog.warn(LABEL_LOG, "TabAnimationDown: mTime " + mTime);
        if (mTime == -1) {
            return;
        }

        switch (state) {
            case LOWER_POSITION: {
//                HiLog.warn(LABEL_LOG, "run: LOWER_POSITION: mAlpha -> " + mAlpha + " : " + (mAlpha <= 0));
                if (mAlpha <= 0) {
//                    mBottomTab.next();
                    state = UPPER_POSITION;
                    mTime = -1; // animation finished
                }
                break;
            }
            case MIDDLE_POSITION: {
//                HiLog.warn(LABEL_LOG, "run: MIDDLE_POSITION: mAlpha -> " + mAlpha + " : " + (mAlpha <= 0));
                if (mAlpha < 90) {
//                    mMiddleTab.next();
                    state = LOWER_POSITION;
                }
                break;
            }
            case UPPER_POSITION: {
//                HiLog.warn(LABEL_LOG, "run: UPPER_POSITION: mAlpha -> " + mAlpha + " : " + (mAlpha <= 0));
//                mTopTab.next();
                state = MIDDLE_POSITION;
                break;
            }
        }

        if (mTime != -1) {
//            HiLog.warn(LABEL_LOG, "run: rotate: ( "+System.currentTimeMillis()+" )");
            long delta = (System.currentTimeMillis() - mTime);
            mAlpha = 180 - (int) (180 * (1 - (1 * mElapsedTime - delta) / (1 * mElapsedTime)));
            mMiddleTab.rotate(mAlpha);
            HiLog.warn(LABEL_LOG, "TabAnimationDown: rotate: ( " + System.currentTimeMillis() + " ) (" + mAlpha + " )");
        }else{
            HiLog.warn(LABEL_LOG, "TabAnimationDown: mAlpha skip ....");
        }

    }

    @Override
    protected void makeSureCycleIsClosed() {
        if (mTime == -1) {
            return;
        }
        switch (state) {
            case LOWER_POSITION: {
//                mBottomTab.next();
                state = UPPER_POSITION;
                mTime = -1; // animation finished
            }
        }
        mMiddleTab.rotate(180);
    }
}
