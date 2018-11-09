package com.berlioz.sql;

import com.berlioz.PeerAccessor;
import com.berlioz.Zipkin;
import com.berlioz.msg.BaseEndpoint;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;

public class SqlPreparedStatement implements PreparedStatement {

    PeerAccessor _peerAccessor;
    PreparedStatement _inner;

    SqlPreparedStatement(PeerAccessor peerAccessor, PreparedStatement inner)
    {
        this._peerAccessor = peerAccessor;
        this._inner = inner;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return run("executeQuery", new InnerAction<ResultSet>() {
            public ResultSet perform(PreparedStatement statement) throws SQLException {
                return statement.executeQuery();
            }
        });
    }

    @Override
    public int executeUpdate() throws SQLException {
        return run("executeUpdate", new InnerAction<Integer>() {
            public Integer perform(PreparedStatement statement) throws SQLException {
                return statement.executeUpdate();
            }
        }).intValue();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        _inner.setNull(parameterIndex, sqlType);
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        _inner.setBoolean(parameterIndex, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        _inner.setByte(parameterIndex, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        _inner.setShort(parameterIndex, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        _inner.setInt(parameterIndex, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        _inner.setLong(parameterIndex, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        _inner.setFloat(parameterIndex, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        _inner.setDouble(parameterIndex, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        _inner.setBigDecimal(parameterIndex, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        _inner.setString(parameterIndex, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        _inner.setBytes(parameterIndex, x);
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        _inner.setDate(parameterIndex, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        _inner.setTime(parameterIndex, x);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        _inner.setTimestamp(parameterIndex, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        _inner.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        _inner.setUnicodeStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        _inner.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void clearParameters() throws SQLException {
        _inner.clearParameters();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        _inner.setObject(parameterIndex, x, targetSqlType);

    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        _inner.setObject(parameterIndex, x);
    }

    @Override
    public boolean execute() throws SQLException {
        return run("execute", new InnerAction<Boolean>() {
            public Boolean perform(PreparedStatement statement) throws SQLException {
                return statement.execute();
            }
        }).booleanValue();
    }

    @Override
    public void addBatch() throws SQLException {
        _inner.addBatch();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        _inner.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        _inner.setRef(parameterIndex, x);
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        _inner.setBlob(parameterIndex, x);
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        _inner.setClob(parameterIndex, x);
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        _inner.setArray(parameterIndex, x);
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return _inner.getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        _inner.setDate(parameterIndex, x, cal);
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        _inner.setTime(parameterIndex, x, cal);
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        _inner.setTimestamp(parameterIndex, x, cal);
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        _inner.setNull(parameterIndex, sqlType, typeName);
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        _inner.setURL(parameterIndex, x);
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return _inner.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        _inner.setRowId(parameterIndex, x);
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        _inner.setNString(parameterIndex, value);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        _inner.setNCharacterStream(parameterIndex, value, length);
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        _inner.setNClob(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        _inner.setClob(parameterIndex, reader, length);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        _inner.setBlob(parameterIndex, inputStream, length);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        _inner.setNClob(parameterIndex, reader, length);
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        _inner.setSQLXML(parameterIndex, xmlObject);
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {
        _inner.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        _inner.setAsciiStream(parameterIndex, x, length);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        _inner.setBinaryStream(parameterIndex, x, length);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        _inner.setCharacterStream(parameterIndex, reader, length);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        _inner.setAsciiStream(parameterIndex, x);
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        _inner.setBinaryStream(parameterIndex, x);
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        _inner.setCharacterStream(parameterIndex, reader);
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        _inner.setNCharacterStream(parameterIndex, value);
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        _inner.setClob(parameterIndex, reader);
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        _inner.setBlob(parameterIndex, inputStream);
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        _inner.setNClob(parameterIndex, reader);
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        return run("executeQuery", new InnerAction<ResultSet>() {
            public ResultSet perform(PreparedStatement statement) throws SQLException {
                return statement.executeQuery(sql);
            }
        });
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return run("executeUpdate", new InnerAction<Integer>() {
            public Integer perform(PreparedStatement statement) throws SQLException {
                return statement.executeUpdate(sql);
            }
        }).intValue();
    }

    @Override
    public void close() throws SQLException {
        _inner.close();
    }

    @Override
    public int getMaxFieldSize() throws SQLException {
        return _inner.getMaxFieldSize();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException {
        _inner.setMaxFieldSize(max);
    }

    @Override
    public int getMaxRows() throws SQLException {
        return _inner.getMaxRows();
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        _inner.setMaxRows(max);
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException {
        _inner.setEscapeProcessing(enable);
    }

    @Override
    public int getQueryTimeout() throws SQLException {
        return _inner.getQueryTimeout();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException {
        _inner.setQueryTimeout(seconds);
    }

    @Override
    public void cancel() throws SQLException {
        _inner.cancel();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return _inner.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        _inner.clearWarnings();
    }

    @Override
    public void setCursorName(String name) throws SQLException {
        _inner.setCursorName(name);
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        return run("execute", new InnerAction<Boolean>() {
            public Boolean perform(PreparedStatement statement) throws SQLException {
                return statement.execute(sql);
            }
        }).booleanValue();
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        return _inner.getResultSet();
    }

    @Override
    public int getUpdateCount() throws SQLException {
        return _inner.getUpdateCount();
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        return _inner.getMoreResults();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        _inner.setFetchDirection(direction);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return _inner.getFetchDirection();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        _inner.setFetchDirection(rows);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return _inner.getFetchSize();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException {
        return _inner.getResultSetConcurrency();
    }

    @Override
    public int getResultSetType() throws SQLException {
        return _inner.getResultSetType();
    }

    @Override
    public void addBatch(String sql) throws SQLException {
        _inner.addBatch(sql);
    }

    @Override
    public void clearBatch() throws SQLException {
        _inner.clearBatch();
    }

    @Override
    public int[] executeBatch() throws SQLException {
        return run("executeBatch", new InnerAction<int[]>() {
            public int[] perform(PreparedStatement statement) throws SQLException {
                return statement.executeBatch();
            }
        });
    }

    @Override
    public Connection getConnection() throws SQLException {
        return _inner.getConnection();
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException {
        return _inner.getMoreResults(current);
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException {
        return _inner.getGeneratedKeys();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        return run("executeUpdate", new InnerAction<Integer>() {
            public Integer perform(PreparedStatement statement) throws SQLException {
                return statement.executeUpdate(sql, autoGeneratedKeys);
            }
        }).intValue();
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
        return run("executeUpdate", new InnerAction<Integer>() {
            public Integer perform(PreparedStatement statement) throws SQLException {
                return statement.executeUpdate(sql, columnIndexes);
            }
        }).intValue();
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException {
        return run("executeUpdate", new InnerAction<Integer>() {
            public Integer perform(PreparedStatement statement) throws SQLException {
                return statement.executeUpdate(sql, columnNames);
            }
        }).intValue();
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        return run("execute", new InnerAction<Boolean>() {
            public Boolean perform(PreparedStatement statement) throws SQLException {
                return statement.execute(sql, autoGeneratedKeys);
            }
        }).booleanValue();
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException {
        return run("execute", new InnerAction<Boolean>() {
            public Boolean perform(PreparedStatement statement) throws SQLException {
                return statement.execute(sql, columnIndexes);
            }
        }).booleanValue();
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException {
        return run("execute", new InnerAction<Boolean>() {
            public Boolean perform(PreparedStatement statement) throws SQLException {
                return statement.execute(sql, columnNames);
            }
        }).booleanValue();
    }

    @Override
    public int getResultSetHoldability() throws SQLException {
        return _inner.getResultSetHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return _inner.isClosed();
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException {
        _inner.setPoolable(poolable);
    }

    @Override
    public boolean isPoolable() throws SQLException {
        return _inner.isPoolable();
    }

    @Override
    public void closeOnCompletion() throws SQLException {
        _inner.closeOnCompletion();
    }

    @Override
    public boolean isCloseOnCompletion() throws SQLException {
        return _inner.isCloseOnCompletion();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return _inner.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return _inner.isWrapperFor(iface);
    }

    private <T> T run(String actionName, InnerAction<T> action) throws SQLException {
        com.berlioz.Executor<T, SQLException> executor = new com.berlioz.Executor<T, SQLException>();
        executor.zipkin(_peerAccessor.getRemoteName(), actionName);
        executor.action(new com.berlioz.Executor.IAction<T, SQLException>() {
            public T perform(BaseEndpoint basePeer, Zipkin.Span span) throws SQLException {
                return action.perform(_inner);
            }});
        return executor.run();
    }

    private interface InnerAction<T> {
        T perform(PreparedStatement statement) throws SQLException;
    }
}
