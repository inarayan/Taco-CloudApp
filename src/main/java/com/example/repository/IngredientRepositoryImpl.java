package com.example.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.dto.Ingredient;

@Repository
public class IngredientRepositoryImpl implements IngredientRepository{
	
	private JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	public IngredientRepositoryImpl(JdbcTemplate jdbcTemplate) {
		super();
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public Iterable<Ingredient> findAll() {
		// TODO Auto-generated method stub
		return jdbcTemplate.query("select id, name, type from Ingredient", this::mapRowToIngredient);
	}

	@Override
	public Ingredient findOne(String id) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject("select id, name, type from Ingredient where id=?"
				, new RowMapper<Ingredient>() {

					@Override
					public Ingredient mapRow(ResultSet rs, int rowNum) throws SQLException {
						// TODO Auto-generated method stub
						return new Ingredient(
								rs.getString("id"),
								rs.getString("name"), 
								Ingredient.Type.valueOf(rs.getString("type")));
					}
				}, id);
	}

	@Override
	public Ingredient save(Ingredient ingredient) {
		// TODO Auto-generated method stub
		jdbcTemplate.update("insert into Ingredient (id, name, type) values (?, ?, ?)", 
				ingredient.getId(),
				ingredient.getName(),
				ingredient.getType().toString());
		
			return ingredient;
	}
	
	Ingredient mapRowToIngredient(ResultSet rs, int rowNum) throws SQLException{
		return new Ingredient(
				rs.getString("id"),
				rs.getString("name"),
				Ingredient.Type.valueOf(rs.getString("type")));
	}

}
