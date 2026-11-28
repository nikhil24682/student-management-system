package com.studentmanagement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StudentManagementSystemApplicationTests {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void usernameMigrationWasApplied() {
		Integer appliedMigrationCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM flyway_schema_history WHERE version = '2' AND success = 1",
				Integer.class);
		Integer uniqueUsernameIndexCount = jdbcTemplate.queryForObject(
				"SELECT COUNT(*) FROM information_schema.statistics "
						+ "WHERE table_schema = DATABASE() AND table_name = 'users' "
						+ "AND index_name = 'uk_users_username' AND non_unique = 0",
				Integer.class);

		assertEquals(1, appliedMigrationCount);
		assertEquals(1, uniqueUsernameIndexCount);
	}

}
