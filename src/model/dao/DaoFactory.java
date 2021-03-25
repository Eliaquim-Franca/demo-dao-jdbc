package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

public class DaoFactory {

	
	//essa classe cria os Daos
	
	public static SellerDao createSellerDao() {
		return new SellerDaoJDBC(DB.getConnection());
	}
	
	// essa classe expoe a interface mas instancia um objeto dentro dela 
	// assim expomos so a interface ao inves de implementarmos o objeto
	
	// no programa principal não chamaremos Objeto obj = new Objet..
	// sera chamadoa assim - Objeto obj = DaoFactory.createSellerDao
}
