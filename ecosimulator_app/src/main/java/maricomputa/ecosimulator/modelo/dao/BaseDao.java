// src/main/java/com/ecosimulator/dao/BaseDAO.java
package maricomputa.ecosimulator.modelo.dao;

import maricomputa.ecosimulator.config.DatabaseConfig;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseDao {
    
    protected Connection getConnection() throws SQLException {
        return DatabaseConfig.getConnection();
    }
    
    protected void closeResources(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (ps != null) ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    protected void closeResources(PreparedStatement ps, Connection conn) {
        closeResources(null, ps, conn);
    }
}