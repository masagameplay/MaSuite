package dev.masa.masuite.api;

import dev.masa.masuite.api.services.IUserService;

public interface MaSuiteAPI {

    static MaSuiteAPI get() {
        return MaSuiteProvider.api();
    }

    default void register(MaSuiteAPI api) {
        MaSuiteProvider.register(api);
    }

    default void unregister() {
        MaSuiteProvider.unregister();
    }

    IUserService<?> userService();

}
