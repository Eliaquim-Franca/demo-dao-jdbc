package application;

import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		System.out.println("++ Test 1 : FindById +++++");
		Seller seller = sellerDao.findById(3);
		System.out.println(seller);
		
		System.out.println("\n+++++ Test 2 : FindByIdDepartment +++++");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.findByDepartment(department);
		
		for(Seller obj: list) {
			System.out.println(obj);
		}
		
		System.out.println("\n+++++ Test 3 : FindAll +++++");
		list = sellerDao.findAll();
		
		for(Seller obj: list) {
			System.out.println(obj);
		}
		
		System.out.println("\n+++++ Test 4 : Insert +++++");
		Seller newSeller = new Seller (null, "Greg" , "greg@gmail.com", new Date(), 4000.0,department);
		sellerDao.insert(newSeller);
		System.out.println("Inserted ! new Id " + newSeller.getId());
		
		System.out.println("\n+++++ Test 5 : Update +++++");
		seller = sellerDao.findById(8);
		seller.setName("Batman");
		sellerDao.update(seller);
		System.out.println("Update completed ");
		
		System.out.println("\n+++++ Test 6 : Delete +++++");
		sellerDao.deleteById(11);
		System.out.println("Delete completed ");		
	}
}
