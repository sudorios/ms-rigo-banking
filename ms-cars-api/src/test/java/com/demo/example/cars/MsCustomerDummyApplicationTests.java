package com.demo.example.cars;

import com.demo.example.cars.model.Car;
import com.demo.example.cars.repository.CarRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MsCustomerDummyApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CarRepository carRepository;

	@BeforeEach
	void resetDatabase() throws Exception {
		// Reset collection to initial seed state
		String jsonContent = Files.readString(Paths.get("carsdb.cars.json"));
		MongoCollection<Document> collection = mongoTemplate.getDb().getCollection("cars");
		collection.drop();

		org.bson.BsonArray array = org.bson.BsonArray.parse(jsonContent);
		for (org.bson.BsonValue val : array) {
			Document doc = Document.parse(val.asDocument().toJson());
			collection.replaceOne(new Document("_id", doc.get("_id")), doc, new ReplaceOptions().upsert(true));
		}
	}

	@Test
	void test1_ListCars_OnlyReturnsAvailableCarsWithStatus1() throws Exception {
		// Should return only 2 cars: Elantra and Tucson (status: "1"), excluding i30 (status: "0")
		mockMvc.perform(get("/api/car"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[*].brand", everyItem(is("Hyundai"))))
				.andExpect(jsonPath("$[*].status", everyItem(is("1"))))
				.andExpect(jsonPath("$[*].model", containsInAnyOrder("Elantra", "Tucson")));
	}

	@Test
	void test2_GetCarById_Success200() throws Exception {
		mockMvc.perform(get("/api/car/672bae02077bfeb5056b92a3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("672bae02077bfeb5056b92a3")))
				.andExpect(jsonPath("$.brand", is("Hyundai")))
				.andExpect(jsonPath("$.model", is("Elantra")))
				.andExpect(jsonPath("$.status", is("1")));
	}

	@Test
	void test2_GetCarById_NotFound404() throws Exception {
		mockMvc.perform(get("/api/car/nonexistent_sku_id"))
				.andExpect(status().isNotFound());
	}

	@Test
	void test3_LogicalDeleteCar_SuccessAndFilteredFromList() throws Exception {
		// Logically delete Elantra (set status to "0")
		mockMvc.perform(put("/api/car/672bae02077bfeb5056b92a3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is("672bae02077bfeb5056b92a3")))
				.andExpect(jsonPath("$.status", is("0")));

		// Verify that GET /api/car now only returns Tucson (status "1")
		mockMvc.perform(get("/api/car"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].model", is("Tucson")));

		// Verify that GET /api/car/672bae02077bfeb5056b92a3 still exists in DB but with status "0"
		mockMvc.perform(get("/api/car/672bae02077bfeb5056b92a3"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", is("0")));
	}

	@Test
	void test3_LogicalDeleteCar_NotFound404() throws Exception {
		mockMvc.perform(put("/api/car/nonexistent_sku_id"))
				.andExpect(status().isNotFound());
	}

}



