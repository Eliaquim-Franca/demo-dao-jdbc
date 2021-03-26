package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {

	void insert(Seller obj);
	void update(Seller obj);
	void deleteByid(Seller obj);
	Seller findById(Integer id);
	List<Seller> findAll(); // usamos lista para pegar todos
	List<Seller> findByDepartment(Department department);
}
