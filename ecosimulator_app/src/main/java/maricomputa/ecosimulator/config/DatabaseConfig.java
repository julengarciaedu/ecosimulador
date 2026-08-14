// src/main/java/com/ecosimulator/config/DatabaseConfig.java
package maricomputa.ecosimulator.config;

import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static BasicDataSource dataSource;
    
    static {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        //dataSource.setUrl("jdbc:mysql://localhost:3306/ecosimulator_db?useSSL=false&serverTimezone=UTC");
        dataSource.setUrl("jdbc:mysql://localhost:3306/ecosimulator_db");
        dataSource.setUsername("ecouserdb");
        dataSource.setPassword("3k0ux3R!0c3");
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(20);
        dataSource.setMaxIdle(10);
        dataSource.setMinIdle(5);
        
//private static final String USUARIO = "ecouserdb";
//private static final String CONTRASENIA = "3k0ux3R!0c3";
    }
    
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
    
    public static void closeDataSource() {
        try {
            if (dataSource != null) {
                dataSource.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
        //Atributos privados y finales (constantes)
    private static final String CADENACONEXION = "jdbc:mysql://localhost:3306/ecosimulator_db";
    private static final String USUARIO = "ecouserdb";
    private static final String CONTRASENIA = "3k0ux3R!0c3";
    
    public static Connection conectarDB() {
        try {
            Connection conexion = DriverManager.getConnection(CADENACONEXION, USUARIO, CONTRASENIA);               
            return conexion;
        } catch (SQLException e) {
            System.out.println("No se ha podido realizar la conexión a base de datos. "+e.getMessage());
        } 
        return null;
    }
    
}