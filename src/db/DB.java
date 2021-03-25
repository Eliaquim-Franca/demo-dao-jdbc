package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {

	// o Import deve ser do import java.sql.Connection
	// deixamos a variavel null apenas para iniciar
	private static Connection conn = null;

	// Esse é a metodo para conectar no banco de dados
	public static Connection getConnection() {
		// validamos se esta nulo pois se estiver temos que conectar com o banco
		if (conn == null) {
			try {
				// vamos pegar as propriedades do banco de dados
				Properties props = loadProperties();
				// agora vamos pegar a url do banco de dados
				String url = props.getProperty("bdurl");
				// o dburl esta no arquivo db.properties e acessamos ele assim
				conn = DriverManager.getConnection(url, props);
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}

		return conn;
	}

	// Agora vamos fechar a conexao
	public static void closeConnection() {
		if (conn == null) {
			// como pode gerar erro deixamos o try e mandamos uma mensagem caso haja erro 
			try {
				conn.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	// essa classe é para criarmos conexões com o banco
	// os metodos terão que ser estaticos

	// 1º vamos carregar as informações do db.properties

	private static Properties loadProperties() {
		// temos que abrir o db.properties ler os dados e guardar os objetos dentro do
		// properties
		try (FileInputStream fs = new FileInputStream("src/db.properties")) {
			
			// colocamos apenas o nome do arquivo pois estamos na raiz do arquivo
			
			Properties props = new Properties();
			props.load(fs);
			return props;
		} catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public static void closeStatement(Statement st) {
		if(st !=null) {
			try {
				st.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet rs) {
		if(rs !=null) {
			try {
				rs.close();
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}
}
