package com.bbva.rbvd.lib.r303.impl.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonHelper {

    private static final JsonHelper INSTANCE = new JsonHelper();

    private final Gson gson;

    private JsonHelper() {
        gson = new GsonBuilder().create();
    }

    public static JsonHelper getInstance() { return INSTANCE; }

    public String convertObjectToJsonString(final Object source) {
        return this.gson.toJson(source);
    }

}
