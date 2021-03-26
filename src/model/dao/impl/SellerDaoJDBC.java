package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	// Este nosso Dao terá uma dependencia com a conexão
	
	private Connection conn;
	
	// criamos o construtor para forçar a injeção de dependencia
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller "
					+"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+" VALUES "
					+ "(?,?,?,?,?)" ,
					Statement.RETURN_GENERATED_KEYS
					);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			// se houver rowsAffected , significa que foi inserido
			if(rowsAffected > 0) {
				ResultSet  rs = st.getGeneratedKeys();
				// como vamos inserir um dado vamos colocar o if
				if(rs.next()) {
					int id  = rs.getInt(1);
					obj.setId(id);
				}
			}else {
				throw new DbException("UNEXPECTED error! No rows Affected!");
			}
			
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(st);
		}
		
		
		
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteByid(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "  
					+ "WHERE seller.id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			// AGORA VAMOS ARRUMAR PARA deixar a Função mais enxuta
			if(rs.next()) {
				Department dep = instatiateDepartment(rs);
				Seller obj = instantiateSeller(rs,dep);
				return obj;
			}
			return null;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instatiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
		
		// se não propagarmos o erro que o rs pode gerar ficará com erro 
		// por isso temos que colocar add throws declaration
		// lembrando que o erro ja esta sendo tratado no metodo findyId
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " 
					+"FROM seller INNER JOIN department " 
					+"ON seller.DepartmentId = department.Id " 
					+"ORDER BY Name" 
					);
			
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			// o map tambem servirá aqui pois ele validara se o departamento ja existe
			//caso exista será reaproveitado
			
			while(rs.next()) {
				
				Department	dep = map.get(rs.getInt("DepartmentId"));
				if(dep == null ) {
					dep = instatiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);//desse jeito salvamos dentro do map
				}
				
				Seller obj = instantiateSeller(rs ,dep);
				list.add(obj);
				
			}
			
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " 
					+"FROM seller INNER JOIN department " 
					+"ON seller.DepartmentId = department.Id " 
					+ "WHERE DepartmentId = ? "
					+"ORDER BY Name" 
					);
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			
			// temos que deixar o department ser o mesmo e não instanciar um novo sempre
			//vamos usar o map
			
			Map<Integer, Department> map = new HashMap<>();
			// vamos validar se o departamento existe usando este map
			
			// colocamos while nessa seção pois podemos receber o valor zero
			while(rs.next()) {
				
				Department	dep = map.get(rs.getInt("DepartmentId"));
				// Validamos o seguinte
				// este map.get ve se na lista tem algum id igual, e se não tiver
				// ele retorna nulo, sendo assim para instanciar um novo dep 
				//seguiremos este ponto
				
				if(dep == null ) {
					dep = instatiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);//desse jeito salvamos dentro do map
				}
				
				Seller obj = instantiateSeller(rs ,dep);
				list.add(obj);
				
			}
			// apos pegarmos todo mundo vamos retornar a lista
			return list;
		}catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

		
	}

	
}
