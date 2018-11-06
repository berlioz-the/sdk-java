package com.berlioz.mysql;

import com.berlioz.PeerAccessor;
import com.berlioz.Zipkin;
import com.berlioz.http.RestTemplate;
import com.berlioz.msg.BaseEndpoint;
import com.berlioz.msg.Endpoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MySqlConnection implements Connection {
    private static Logger logger = LogManager.getLogger(RestTemplate.class);
    private static Pattern URL_PATTERN = Pattern.compile("jdbc:\\w+:\\/\\/([\\w|\\d|:|.]+)\\/.*", Pattern.CASE_INSENSITIVE);

    PeerAccessor _peerAccessor;
    Connection _inner;
    String _url;
    java.util.Properties _properties;

    MySqlConnection(PeerAccessor peerAccessor, String url, java.util.Properties properties) throws SQLException {
        this._peerAccessor = peerAccessor;
        this._url = url;
        this._properties = properties;
        this._getInner();
    }

    Connection _getInner() throws SQLException
    {
        if (_inner != null) {
            return _inner;
        }

        com.berlioz.Executor<Connection, SQLException> executor = new com.berlioz.Executor<Connection, SQLException>();
        executor.zipkin(_peerAccessor.getRemoteName(), "connect");
        executor.selector(this._peerAccessor.getRandomSelector());
        executor.action(new com.berlioz.Executor.IAction<Connection, SQLException>() {
            public Connection perform(BaseEndpoint basePeer, Zipkin.Span span) throws SQLException {
                Endpoint peer = (Endpoint)basePeer;
                String actualUrl = massageUrl(peer);
                logger.debug("Connecting to: {}", actualUrl);
                return DriverManager.getConnection(actualUrl , _properties);
            }});
        _inner = executor.run();
        return _inner;
    }

    private String massageUrl(Endpoint peer) {
        Matcher m = URL_PATTERN.matcher(_url);
        StringBuffer sb = new StringBuffer();
        String hostPort = peer.getAddress() + ":" + String.valueOf(peer.getPort());
        // TODO: Debugging
//        hostPort = "localhost:40012";
        while (m.find()) {
            m.appendReplacement(sb, m.group(0).replaceFirst(Pattern.quote(m.group(1)), hostPort));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    @Override
    public Statement createStatement() throws SQLException {
        return execute("createStatement", new InnerAction<Statement>() {
            public Statement perform(Connection connection) throws SQLException {
                return new MySqlStatement(_peerAccessor, connection.createStatement());
            }
        });
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this._getInner().prepareStatement(sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return this._getInner().prepareCall(sql);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return this._getInner().nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this._getInner().setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this._getInner().getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        this._getInner().commit();
    }

    @Override
    public void rollback() throws SQLException {
        this._getInner().rollback();
    }

    @Override
    public void close() throws SQLException {
        this._getInner().close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this._getInner().isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this._getInner().getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this._getInner().setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this._getInner().isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        this._getInner().setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        this._getInner().setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this._getInner().getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this._getInner().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this._getInner().clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return this._getInner().createStatement(resultSetType, resultSetConcurrency);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return this._getInner().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return this._getInner().prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this._getInner().getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this._getInner().setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        this._getInner().setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this._getInner().getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return this._getInner().setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return this._getInner().setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        this._getInner().rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        this._getInner().releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this._getInner().createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this._getInner().prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return this._getInner().prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return this._getInner().prepareStatement(sql, autoGeneratedKeys);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        return this._getInner().prepareStatement(sql, columnIndexes);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        return this._getInner().prepareStatement(sql, columnNames);
    }

    @Override
    public Clob createClob() throws SQLException {
        return this._getInner().createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return this._getInner().createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return this._getInner().createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return this._getInner().createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return this._getInner().isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        this._inner.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this._inner.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return this._getInner().getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this._getInner().getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return this._getInner().createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return this._getInner().createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        this._getInner().setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return this._getInner().getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        this._getInner().abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        this._getInner().setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this._getInner().getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this._getInner().unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this._getInner().isWrapperFor(iface);
    }

    private <T> T execute(String actionName, InnerAction<T> action) throws SQLException {
        com.berlioz.Executor<T, SQLException> executor = new com.berlioz.Executor<T, SQLException>();
        executor.zipkin(_peerAccessor.getRemoteName(), actionName);
        executor.action(new com.berlioz.Executor.IAction<T, SQLException>() {
            public T perform(BaseEndpoint basePeer, Zipkin.Span span) throws SQLException {
                Connection connection = _getInner();
                return action.perform(connection);
            }});
        return executor.run();
    }

    private interface InnerAction<T> {
        T perform(Connection connection) throws SQLException;
    }
}
