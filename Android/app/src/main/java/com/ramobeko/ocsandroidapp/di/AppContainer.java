package com.ramobeko.ocsandroidapp.di;

import com.ramobeko.ocsandroidapp.data.repository.LoginRepository;
import com.ramobeko.ocsandroidapp.data.repository.RegisterRepository;

public class AppContainer {
    public final LoginRepository loginRepository;
    public final RegisterRepository registerRepository;

    public AppContainer() {
        loginRepository = new LoginRepository();
        registerRepository = new RegisterRepository();
    }
}
