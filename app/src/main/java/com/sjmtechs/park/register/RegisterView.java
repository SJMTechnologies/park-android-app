package com.sjmtechs.park.register;

import com.sjmtechs.park.model.Register;

public interface RegisterView {
    Register getRegisterData();

    void showError(int resId);
    void showError(String msg);
    void startActivity();
    void showDialog(String msg);
}
