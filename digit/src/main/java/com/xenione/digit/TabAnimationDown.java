package com.xenione.digit;


/**
 * rotates middle tab downwards
 */
public final class TabAnimationDown extends AbstractTabAnimation {

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

        if (mTime == -1) {
            return;
        }

        switch (state) {
            case LOWER_POSITION: {
//                Log.d(TAG, "run: LOWER_POSITION: mAlpha -> "+mAlpha+ " : " + (mAlpha <= 0));
                if (mAlpha <= 0) {
                    mBottomTab.next();
                    state = UPPER_POSITION;
                    mTime = -1; // animation finished
                }
                break;
            }
            case MIDDLE_POSITION: {
//                Log.d(TAG, "run: MIDDLE_POSITION: mAlpha -> "+mAlpha+ " : " + (mAlpha <= 0));
                if (mAlpha < 90) {
                    mMiddleTab.next();
                    state = LOWER_POSITION;
                }
                break;
            }
            case UPPER_POSITION: {
//                Log.d(TAG, "run: UPPER_POSITION: mAlpha -> "+mAlpha+ " : " + (mAlpha <= 0));
                mTopTab.next();
                state = MIDDLE_POSITION;
                break;
            }
        }

        if (mTime != -1) {
            long delta = (System.currentTimeMillis() - mTime);
            mAlpha = 180 - (int) (180 * (1 - (1 * mElapsedTime - delta) / (1 * mElapsedTime)));
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
                mBottomTab.next();
                state = UPPER_POSITION;
                mTime = -1; // animation finished
            }
        }
        mMiddleTab.rotate(180);
    }
}