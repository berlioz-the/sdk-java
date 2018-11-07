package com.berlioz.mysql;

import com.berlioz.PeerAccessor;

import java.sql.Connection;
import java.sql.SQLException;

public class Driver {

    private PeerAccessor _peerAccessor;

    public Driver(PeerAccessor peerAccessor)
    {
        this._peerAccessor = peerAccessor;
    }

    public Connection getConnection(String url,
                                           java.util.Properties info) throws SQLException {
        return new MySqlConnection(this._peerAccessor, url, info);
    }

    public Connection getConnection(String url,
                                           String user, String password) throws SQLException {
        java.util.Properties info = new java.util.Properties();

        if (user != null) {
            info.put("user", user);
        }
        if (password != null) {
            info.put("password", password);
        }

        return (getConnection(url, info));
    }

    public Connection getConnection(String url) throws SQLException {
        java.util.Properties info = new java.util.Properties();
        return (getConnection(url, info));
    }

    public Connection getConnection(DataSourceConfiguration config) throws SQLException {
        return getConnection(config.url, config.username, config.password);
    }
}
