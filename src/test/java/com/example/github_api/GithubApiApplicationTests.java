package com.example.github_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GithubApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testWorkflow() {
		assertEquals(2, 2);
	}

}
