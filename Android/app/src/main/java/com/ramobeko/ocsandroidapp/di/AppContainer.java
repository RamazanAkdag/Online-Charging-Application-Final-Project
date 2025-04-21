package com.ramobeko.ocsandroidapp.di;

import com.ramobeko.ocsandroidapp.data.repository.RegisterRepository;

public class AppContainer {
    public final RegisterRepository registerRepository;

    public AppContainer() {
        // Initialize repository here
        registerRepository = new RegisterRepository();
    }
}
