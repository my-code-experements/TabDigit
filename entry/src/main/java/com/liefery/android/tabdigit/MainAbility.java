package com.liefery.android.tabdigit;

import com.liefery.android.tabdigit.slice.MainAbilitySlice;
import com.xenione.digit.TabDigit;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.app.dispatcher.TaskDispatcher;
import ohos.app.dispatcher.task.TaskPriority;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbility extends Ability  implements Runnable {
    TabDigit tabDigit1;
    TaskDispatcher uiTaskDispatcher = getUITaskDispatcher();
    private static final HiLogLabel LABEL_LOG = new HiLogLabel(HiLog.LOG_APP, 0x00201, "-MainAbility-");

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
//        super.setMainRoute(MainAbilitySlice.class.getName());
        super.setUIContent(ResourceTable.Layout_ability_main);

        Text text = (Text) findComponentById(ResourceTable.Id_text_helloworld);
        text.setText("asnkldnklsand");
        try {
            tabDigit1 = (TabDigit) findComponentById(ResourceTable.Id_charView1);
            HiLog.warn(LABEL_LOG, "MainAbility: onStart"+tabDigit1);
            assert tabDigit1 != null;

            uiTaskDispatcher = getUITaskDispatcher();

            HiLog.warn(LABEL_LOG, "MainAbility: uiTaskDispatcher  "+uiTaskDispatcher);

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
    public void run() {
//        HiLog.warn(LABEL_LOG, "MainAbility: run");
        tabDigit1.start();
        uiTaskDispatcher.delayDispatch(this,1000);

//        ViewCompat.postOnAnimationDelayed(tabDigit1, this, 1000);
    }
}
