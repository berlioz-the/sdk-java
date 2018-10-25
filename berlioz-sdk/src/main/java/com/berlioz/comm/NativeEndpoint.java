package com.berlioz.comm;

import com.google.gson.annotations.SerializedName;

public class NativeEndpoint {
    private String name;

    @SerializedName("class")
    private String className;
    private String subClass;
    private NativeEndpointConfig config;
}
