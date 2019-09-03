package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class Database {
    private Connection connection = null;
    private Statement st;

    // Datos de la conexión MySQL
    private static final String SERVIDOR = "";
    private static final String BASEDEDATOS = "";
    private static final String USUARIO = "";
    private static final String PASSWORD = "";
	
	public Database() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://"+SERVIDOR+"/"+BASEDEDATOS+"?useLegacyDatetimeCode=false&serverTimezone=UTC", USUARIO, PASSWORD);
            st = connection.createStatement();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error al registrar el driver de MySQL: " + ex);
        } catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void addCertificado(String user, String cerf, String hash) throws SQLException {
		st.executeUpdate("INSERT INTO usuario (user, cert, hash) VALUES ('"+user+"','"+cerf+"', '"+hash+"')");
	}
	
	public void addToken(String user, String token) throws SQLException {
		st.executeUpdate("INSERT INTO token (user, token, date) VALUES ('"+user+"','"+token+"', NOW())");
	}
	
	public void removeToken(String token) throws SQLException {
		st.executeUpdate("DELETE FROM token WHERE token=\""+token+"\"");
	}
	
	public String getToken(String usuario, String hash) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE user=\""+usuario+"\" AND hash=\""+hash+"\""); 
		if(!rs.next()) {
			rs.close();
			return "0";
		}
		rs.close();
		
		String token = UUID.randomUUID().toString();
		addToken(usuario, token);
		return token;
	}

	public boolean checkToken(String token) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM token WHERE token=\""+token+"\""); 
		boolean encontrado = rs.next();
		rs.close();
		return encontrado;
	}

	public boolean checkUser(String user) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE user=\""+user+"\""); 
		boolean encontrado = rs.next();
		rs.close();
		return encontrado;
	}


	public String getCertf(String user) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE user=\""+user+"\""); 
		boolean encontrado = rs.next();
		if(!encontrado) {
			rs.close();
			return null;
		}
		
		String cerft = rs.getString("cert");
		rs.close();
		return cerft;
	}
	
	public boolean checkCerf(String cerf) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM usuario WHERE cerf=\""+cerf+"\""); 
		boolean encontrado = rs.next();
		rs.close();
		return encontrado;
	}

}
