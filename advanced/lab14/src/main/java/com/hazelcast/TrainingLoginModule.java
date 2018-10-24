package com.hazelcast;

import com.hazelcast.security.ClusterLoginModule;
import com.hazelcast.security.UsernamePasswordCredentials;

import javax.security.auth.login.LoginException;

public class TrainingLoginModule extends ClusterLoginModule {

    @Override
    protected boolean onLogin() throws LoginException {
        if (!(this.credentials instanceof UsernamePasswordCredentials)) {
            return false;
        }

        UsernamePasswordCredentials credentials = (UsernamePasswordCredentials) this.credentials;

        String expectedPassword = (String) this.options.get(credentials.getUsername());
        if (expectedPassword == null) {
            return false;
        }

        String password = credentials.getPassword();
        return expectedPassword.equals(password);
    }

    @Override
    protected boolean onCommit() throws LoginException {
        return this.loginSucceeded;
    }

    @Override
    protected boolean onAbort() throws LoginException {
        return true;
    }

    @Override
    protected boolean onLogout() throws LoginException {
        return true;
    }
}
