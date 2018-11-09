package com.berlioz.sql;

import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

public class DataSourceBuilder {

    private DataSourceConfiguration _config = new DataSourceConfiguration();

    public DataSourceBuilder()
    {

    }

    public DataSourceBuilder url(String value) {
        _config.url = value;
        return this;
    }

    public DataSourceBuilder endpoint(String value) {
        _config.endpoint = value;
        return this;
    }

    public DataSourceBuilder username(String value) {
        _config.username = value;
        return this;
    }

    public DataSourceBuilder password(String value) {
        _config.password = value;
        return this;
    }

    public javax.sql.DataSource build() throws IllegalArgumentException
    {
        _config.compile();
        javax.sql.DataSource impl = new DataSource(_config);
        return new LazyConnectionDataSourceProxy(impl);
    }
}
