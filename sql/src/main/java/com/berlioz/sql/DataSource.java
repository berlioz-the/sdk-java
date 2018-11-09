package com.berlioz.sql;

import com.berlioz.Berlioz;
import com.berlioz.Registry;
import com.berlioz.Service;
import com.berlioz.msg.Endpoint;
import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class DataSource extends AbstractDataSource {

    private DataSourceConfiguration _config;

    private String _serviceName;
    private String _endpointName;

    private Service _service;
    private Connection _currentConnection = null;
    private Object _sync = new Object();

    public DataSource(DataSourceConfiguration config) {
        _config = config;

        _serviceName = config.service;
        _endpointName = config.endpoint;

        this._init();
    }

    private void _init() {
        _service = Berlioz.service(this._serviceName, this._endpointName);
        _service.monitorAll(new Registry.Callback<Map<String, Endpoint>>() {
            @Override
            public void callback(Map<String, Endpoint> value) {
                _reset();
            }
        });
    }

    private void _reset() {
        synchronized (_sync) {
            if (_currentConnection != null) {
                try {
                    _currentConnection.close();
                }
                catch(SQLException ex) {

                }
                _currentConnection = null;
            }
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        synchronized (_sync) {
            if (_currentConnection == null) {
                Connection connection  = new SqlConnection(_service.getPeerAccessor(), _config);
                _currentConnection = connection;
            }
            return _currentConnection;
        }
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return this.getConnection();
    }

}
