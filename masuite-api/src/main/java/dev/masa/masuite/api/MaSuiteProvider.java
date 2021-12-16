package dev.masa.masuite.api;

public class MaSuiteProvider {

    public static MaSuiteAPI instance = null;

    public static MaSuiteAPI api() {
        if (instance == null) {
            throw new IllegalStateException("MaSuitAPI isn't loaded yet!");
        }

        return MaSuiteProvider.instance;
    }

    public static void register(MaSuiteAPI api) {
        MaSuiteProvider.instance = api;
    }

    public static void unregister() {
        MaSuiteProvider.instance = null;
    }
}
