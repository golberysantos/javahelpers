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
			
			rs = stmt.executeQuery("select * from usuario");
			System.out.println(rs);			
			while (rs.next()) {
				String nick = rs.getString("nick");
				String senha = rs.getString("senha");
				System.out.println(nick +" | senha: "+ senha);
			}
			
					
			
			conn.close();
		} catch (Exception e) {
			System.err.println("ERRO "+e.getMessage());
		}
	}

}
