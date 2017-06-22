// IRemoteService.aidl
package com.jb.smartsetting;

// Declare any non-default types here with import statements

interface IRemoteService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void registerCallback(IRemoteServiceCallback callback);
    void unregisterCallback(IRemoteServiceCallback callback);
    String onService(int msg);
}
