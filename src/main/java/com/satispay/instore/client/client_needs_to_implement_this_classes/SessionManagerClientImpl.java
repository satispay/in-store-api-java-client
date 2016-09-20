package com.satispay.instore.client.client_needs_to_implement_this_classes;

import com.satispay.protocore.persistence.MemoryPersistenceManager;
import com.satispay.protocore.persistence.SecurePersistenceManager;
import com.satispay.protocore.session.SessionManager;

/**
 * --> Created by domenicovisconti on 19/09/16.
 * It might be important to implement the {@link SessionManager} interface using the singleton pattern, depending on the client
 * implementor architecture.
 */
public class SessionManagerClientImpl implements SessionManager {

    private static volatile SessionManagerClientImpl instance = new SessionManagerClientImpl();

    public static SessionManagerClientImpl getInstance() {
        return instance;
    }

    private SessionManagerClientImpl() {
    }

    @Override
    public SecurePersistenceManager getSecurePersistenceManager() {
        return MemoryPersistenceManager.getInstance();
    }

}
