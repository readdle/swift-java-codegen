package com.readdle.swiftjava.sample;

import android.support.annotation.NonNull;

public class ProviderImpl implements Provider {
    public void fillStorage(@NonNull Storage storage) {
        storage.add("Hello");
        storage.add("from");
        storage.add("java");
        storage.release();
    }
}

