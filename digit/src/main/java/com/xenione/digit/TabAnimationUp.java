package com.xenione.digit;

import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

/**
 * rotates middle tab upwards
 */
public final class TabAnimationUp extends AbstractTabAnimation {
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-MainAbility-");

    public TabAnimationUp(TabDigit.Tab mTopTab, TabDigit.Tab mBottomTab, TabDigit.Tab mMiddleTab) {
        super(mTopTab, mBottomTab, mMiddleTab);
    }

    @Override
    public void initState() {
        state = LOWER_POSITION;
    }

    @Override
    public void initMiddleTab() { /* nothing to do */ }

    @Override
    public void run() {
        HiLog.warn(LABEL_LOG, "TabAnimationUp: mTime -> "+mTime);
        if (mTime == -1) {
            return;
        }

        switch (state) {
            case LOWER_POSITION: {
                HiLog.warn(LABEL_LOG, "TabAnimationUp: LOWER_POSITION mAlpha -> "+mAlpha);
                mBottomTab.next();
                state = MIDDLE_POSITION;
                break;
            }
            case MIDDLE_POSITION: {
                HiLog.warn(LABEL_LOG, "TabAnimationUp: MIDDLE_POSITION mAlpha -> "+mAlpha);
                if (mAlpha > 90) {
                    mMiddleTab.next();
                    state = UPPER_POSITION;
                }
                break;
            }
            case UPPER_POSITION: {
                HiLog.warn(LABEL_LOG, "TabAnimationUp: UPPER_POSITION mAlpha -> "+mAlpha);
                if (mAlpha >= 180) {
                    mTopTab.next();
                    state = LOWER_POSITION;
                    mTime = -1; // animation finished
                }
                break;
            }
        }

        if (mTime != -1) {
            long delta = (System.currentTimeMillis() - mTime);
            mAlpha = (int) (180 * (1 - (1 * mElapsedTime - delta) / (1 * mElapsedTime)));
            mMiddleTab.rotate(mAlpha);
        }

    }

    @Override
    protected void makeSureCycleIsClosed() {
        if (mTime == -1) {
            return;
        }
        switch (state) {
            case LOWER_POSITION: {
                mMiddleTab.next();
                state = UPPER_POSITION;
            }
            case UPPER_POSITION: {
                mTopTab.next();
                state = LOWER_POSITION;
                mTime = -1; // animation finished
            }
        }
        mMiddleTab.rotate(180);
    }
}
