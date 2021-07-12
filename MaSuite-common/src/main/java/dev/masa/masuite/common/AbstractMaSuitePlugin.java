package dev.masa.masuite.common;

import dev.masa.masuite.api.MaSuiteAPI;

public abstract class AbstractMaSuitePlugin<T> implements MaSuiteAPI {

    public abstract T loader();

    public abstract void loader(T loader) throws IllegalStateException;

    public abstract void onEnable();

    public abstract void onDisable();
}
