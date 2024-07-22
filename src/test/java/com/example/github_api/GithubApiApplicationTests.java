package com.example.github_api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GithubApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("Testing function: testWorkflow 2 == 2?")
	void testForTrueWorkflow() {
		assertEquals(2, 2);
	}

}
