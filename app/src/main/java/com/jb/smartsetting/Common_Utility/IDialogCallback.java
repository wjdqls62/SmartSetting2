package com.jb.smartsetting.Common_Utility;

/**
 * Created by jeongbin.son on 2017-07-27.
 * 독립된 CustomDialog Class내에서 사용자가 선택한 아이템을 Callback을 생성한 Activity에 Return해주기 위해 사용한다. *
 */

public interface IDialogCallback {
    public void onDialogEventCallback(String SelectedItem);
}
