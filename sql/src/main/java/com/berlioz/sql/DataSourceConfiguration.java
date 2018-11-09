package com.berlioz.sql;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class DataSourceConfiguration {
    private final static String URL_REGEX = "^([\\w:]+\\/\\/)([\\w\\d]+)(:([\\w\\d]+))?(\\/\\w+)?(\\?\\S*)?$";
    private final static Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);

    DataSourceConfiguration() {

    }

    public String url;
    public String username;
    public String password;

    public String schema;
    public String service;
    public String endpoint;
    public String database;
    public String params;

    public void compile() throws IllegalArgumentException
    {
        Matcher m = URL_PATTERN.matcher(url);
        if (m.find()) {
            this.schema = m.group(1);
            this.service = m.group(2);
            this.endpoint = m.group(4);
            this.database = m.group(5);
            if (this.database == null) {
                this.database = "";
            }
            this.params = m.group(6);
            if (this.params == null) {
                this.params = "";
            }
        } else {
            throw new IllegalArgumentException("Invalid datasource url: " + url);
        }
    }

    public java.util.Properties extractProperties()
    {
        java.util.Properties info = new java.util.Properties();
        if (username != null) {
            info.put("user", username);
        }
        if (password != null) {
            info.put("password", password);
        }
        return info;
    }
}
