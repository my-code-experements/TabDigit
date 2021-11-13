package com.liefery.android.tabdigit.slice;

import com.liefery.android.tabdigit.ResourceTable;
import com.xenione.digit.TabDigit;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice implements Runnable {
    TabDigit tabDigit1;
    TaskDispatcher uiTaskDispatcher = getUITaskDispatcher();
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-MainAbility-");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        try {
            Text text = (Text) findComponentById(ResourceTable.Id_text_helloworld);
            text.setText("asnkldnklsand");
            tabDigit1 = (TabDigit) findComponentById(ResourceTable.Id_charView1);
            assert tabDigit1 != null;
            uiTaskDispatcher.delayDispatch(this,1000);  
            HiLog.warn(LABEL_LOG, "MainAbilitySlice: onStart");
        }catch (Exception ex){
            HiLog.warn(LABEL_LOG, "MainAbilitySlice: ex "+ex);
            for (StackTraceElement st: ex.getStackTrace()){
                HiLog.warn(LABEL_LOG, ""+st);

            }
        }
        
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    @Override
    public void run() {
        tabDigit1.start();
        uiTaskDispatcher.delayDispatch(this,1000);

//        ViewCompat.postOnAnimationDelayed(tabDigit1, this, 1000);
    }
}
