package br.com.javaapi.env;

import java.beans.Statement;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class ConexaoGibenDB {
	public void conectar () throws IOException, InterruptedException {
		try {
			String url = "jdbc:mysql://localhost:3306/giben";
			Connection conn = DriverManager.getConnection(url, "root", "root");

			java.sql.Statement stmt = conn.createStatement();
			System.out.println("vai" + conn);
			ResultSet rs;
			rs = ((java.sql.Statement) stmt).executeQuery("SELECT * FROM user");
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
