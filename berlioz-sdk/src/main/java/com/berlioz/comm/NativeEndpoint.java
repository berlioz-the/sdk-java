package com.berlioz.comm;

import com.google.gson.annotations.SerializedName;

public class NativeEndpoint extends BaseEndpoint {
    @SerializedName("class")
    private String className;
    private String subClass;
    private NativeEndpointConfig config;
}
