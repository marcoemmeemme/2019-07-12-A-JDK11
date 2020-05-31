package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodCouple;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> listFood(int quantita, Map<Integer,Food> idMap)
	{
		String sql="SELECT p.food_code, f.display_name, count(p.portion_id) AS conta\n" + 
				"FROM food f,`portion` p\n" + 
				"WHERE f.food_code=p.food_code\n" + 
				"GROUP BY p.food_code\n" + 
				"HAVING conta<=?\n" +
				"ORDER BY f.display_name ASC";
		
		try {
			List<Food> list = new ArrayList<>() ;
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, quantita);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Food f=new Food(res.getInt("p.food_code"),res.getString("f.display_name"));
					list.add(f);
					idMap.put(res.getInt("p.food_code"), new Food(res.getInt("p.food_code"),res.getString("f.display_name")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	public List<FoodCouple> listCouple(int quantita, Map<Integer,Food> idMap)
	{
		String sql="SELECT  f1.food_code, f2.food_code, AVG (c.condiment_calories) AS peso, COUNT(distinct c.condiment_code) AS comune\n" + 
			"FROM food f1, food f2, food_condiment fc1, food_condiment fc2, condiment c, `portion` p1, `portion` p2\n" + 
			"WHERE fc1.food_code=f1.food_code\n" + 
			"AND fc2.food_code=f2.food_code\n" + 
			"AND f1.food_code<f2.food_code\n" + 
			"AND fc1.condiment_code=c.condiment_code\n" + 
			"AND fc2.condiment_code=c.condiment_code\n" + 
			"AND fc1.condiment_code=fc2.condiment_code\n" + 
			"AND p1.food_code=f1.food_code\n" + 
			"AND p2.food_code=f2.food_code\n" + 
			"GROUP BY f1.food_code, f2.food_code\n" + 
			"HAVING COUNT(distinct p1.portion_id)<=?\n" + 
			"AND COUNT(distinct p2.portion_id)<=?";
	try {
		Connection conn = DBConnect.getConnection() ;
		PreparedStatement st = conn.prepareStatement(sql) ;
		st.setInt(1, quantita);
		st.setInt(2, quantita);
		List<FoodCouple> coppie = new ArrayList<>() ;
		
		ResultSet res = st.executeQuery() ;
		
		while(res.next()) {
			try 
			{
				Food f1=idMap.get(res.getInt("f1.food_code"));
				Food f2=idMap.get(res.getInt("f2.food_code"));
				FoodCouple f=new FoodCouple(f1,f2,res.getDouble("peso"));
				coppie.add(f);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		conn.close();
		return coppie;

	} catch (SQLException e) {
		e.printStackTrace();
		return null ;
	}

	
	}
}