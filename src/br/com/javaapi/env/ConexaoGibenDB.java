package br.com.javaapi.env;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

public class ConexaoGibenDB {
	public void conectar() {
		try {
			String url = "jdbc:mysql://localhost/giben";
			Connection conn = DriverManager.getConnection(url, "root", "root");

			System.out.println("vai" + conn);
			Statement stmt = (Statement) conn.createStatement();
			ResultSet rs;
			rs = ((java.sql.Statement) stmt).executeQuery("SELECT * FROM customers");
			System.out.println(rs);
			while (rs.next()) {
				String lastName = rs.getString("nome");
				System.out.println(lastName);
			}
			conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}
	}

}
