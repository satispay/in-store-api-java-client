package com.satispay.instore.client.client_needs_to_implement_this_classes;

import com.satispay.protocore.dh.DHFlow;
import com.satispay.protocore.dh.UptimeMillisProvider;
import com.satispay.protocore.persistence.MemoryPersistenceManager;
import com.satispay.protocore.persistence.SecurePersistenceManager;

/**
 * --> Created by domenicovisconti on 16/09/16.
 * Here is an implementation of the DHFlow interface. Usually the only methods to override are getSecurePersistenceManager
 * and getUptimeMillisProvider.
 * It might be important to implement the {@link DHFlow} interface using the singleton pattern, depending on the client
 * implementor architecture.
 */
public class DHFlowClientImpl implements DHFlow {

    private static volatile DHFlowClientImpl instance = new DHFlowClientImpl();

    public static DHFlowClientImpl getInstance() {
        return instance;
    }

    private DHFlowClientImpl() {
    }

    @Override
    public SecurePersistenceManager getSecurePersistenceManager() {
        return MemoryPersistenceManager.getInstance();
    }

    @Override
    public UptimeMillisProvider getUptimeMillisProvider() {
        return UptimeMillisProviderClientImpl.getInstance();
    }

}
