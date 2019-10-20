package com.example.repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.example.dto.Ingredient;
import com.example.dto.Taco;

@Repository
public class TacoRepositoryImpl implements TacoRepository{

	private JdbcTemplate jdbc;
	

	private final IngredientRepository ingredientRepository;
	
	@Autowired
	public TacoRepositoryImpl(JdbcTemplate jdbc,IngredientRepository ingredientRepository ) {
		super();
		this.jdbc = jdbc;
		this.ingredientRepository = ingredientRepository;
	}


	@Override
	public Taco save(Taco taco) {
		// TODO Auto-generated method stub
		
		List<Ingredient> ingredients = new ArrayList<Ingredient>();
		ingredientRepository.findAll().forEach(i-> ingredients.add(i));;
		
		long tacoId = saveTacoInfo(taco);
		
		taco.setId(tacoId);
	    for (String ingredientid : taco.getIngredients()) {
	    	//Ingredient ingredient2 = new Ingredient(id, , null)
	    	
	    	ingredients.stream().filter((i)->i.getId().toLowerCase().equalsIgnoreCase(ingredientid.toLowerCase()))
	    	.forEach((i)->{saveIngredientToTaco(i, tacoId);});
	        
	      }
		return taco;
		
	}


	private void saveIngredientToTaco(Ingredient ingredient, long tacoId) {
		// TODO Auto-generated method stub
		jdbc.update("insert into Taco_Ingredients(taco, ingredient) values (?,?)",+
		tacoId, ingredient.getId());
		
	}


	private long saveTacoInfo(Taco taco) {
		// TODO Auto-generated method stub
		taco.setCreatedAt(new Date());
		
		PreparedStatementCreatorFactory factory = 
				new PreparedStatementCreatorFactory("insert into Taco(name, createdAt) values(?,?)",Types.VARCHAR, Types.TIMESTAMP);
		
		factory.setGeneratedKeysColumnNames("id");
		PreparedStatementCreator psc = factory.newPreparedStatementCreator(Arrays.asList(
				                taco.getName(),
				                new Timestamp(taco.getCreatedAt().getTime())));
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc, keyHolder);
		return keyHolder.getKey().longValue();
		
	}

	
}
