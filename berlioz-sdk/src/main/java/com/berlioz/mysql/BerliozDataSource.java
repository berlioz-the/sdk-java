package com.berlioz.mysql;

import com.berlioz.Berlioz;
import com.berlioz.Registry;
import com.berlioz.Service;
import com.berlioz.msg.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Component
public class BerliozDataSource extends AbstractDataSource {

//    @Autowired
    private DataSourceConfiguration config;

    private String _serviceName;
    private String _endpointName;

    private Service _service;
    private Connection _currentConnection = null;
    private Object _sync = new Object();

    public BerliozDataSource(ApplicationContext context) {
        config = DataSourceConfiguration.fromEnvironment(context.getEnvironment());

        _serviceName = config.service;

        _endpointName = config.endpoint;
        if (_endpointName == null || _endpointName.length() == 0) {
            _endpointName = "default";
        }

        this._init();
    }

//    public BerliozDataSource(String service)
//    {
//        this(service, "default");
//    }
//
//    public BerliozDataSource(String service, String endpoint)
//    {
//        this._serviceName = service;
//        this._endpointName = endpoint;
//
//        this._init();
//    }

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
                Connection connection = _service.mysql().getConnection(config);
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
