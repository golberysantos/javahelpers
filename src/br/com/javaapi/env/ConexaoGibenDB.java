package br.com.javaapi.env;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class ConexaoGibenDB {
	public void conectar () throws IOException, InterruptedException {
		try {
			String url = "jdbc:mysql://localhost:3306/giben";
			Connection conn = DriverManager.getConnection(url, "root", "root");
			System.out.println("CONEXAO ESTABELECIADA");
			
			java.sql.Statement stmt = conn.createStatement();			
			ResultSet rs;
			
			rs = stmt.executeQuery("SELECT * FROM giben.user");
			System.out.println(rs);
			while (rs.next()) {
				String lastName = rs.getString("nome");
				System.out.println(lastName);
			}
			conn.close();
		} catch (Exception e) {
			System.err.println("ERRO "+e);
		}
	}

}
