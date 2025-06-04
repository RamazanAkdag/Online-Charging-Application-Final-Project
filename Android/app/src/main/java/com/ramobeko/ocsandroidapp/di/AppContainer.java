package com.ramobeko.ocsandroidapp.di;

import com.ramobeko.ocsandroidapp.data.repository.ForgotPasswordRepository;
import com.ramobeko.ocsandroidapp.data.repository.LoginRepository;
import com.ramobeko.ocsandroidapp.data.repository.RegisterRepository;
import com.ramobeko.ocsandroidapp.data.repository.SubscriberRepository;

public class AppContainer {
    public final LoginRepository loginRepository;
    public final RegisterRepository registerRepository;
    public final SubscriberRepository subscriberRepository;
    public final ForgotPasswordRepository forgotPasswordRepository;
    public AppContainer() {
        forgotPasswordRepository = new ForgotPasswordRepository();
        loginRepository = new LoginRepository();
        registerRepository = new RegisterRepository();
        subscriberRepository = new SubscriberRepository();
    }
}
