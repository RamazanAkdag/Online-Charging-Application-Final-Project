package com.ramobeko.ocsandroidapp.di;

import com.ramobeko.ocsandroidapp.data.repository.LoginRepository;
import com.ramobeko.ocsandroidapp.data.repository.RegisterRepository;
import com.ramobeko.ocsandroidapp.data.repository.SubscriberRepository;

public class AppContainer {
    public final LoginRepository loginRepository;
    public final RegisterRepository registerRepository;

    public final SubscriberRepository subscriberRepository;
    public AppContainer() {
        loginRepository = new LoginRepository();
        registerRepository = new RegisterRepository();
        subscriberRepository = new SubscriberRepository();
    }
}
