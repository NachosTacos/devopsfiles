package canary.paper;

import static org.junit.Assert.assertEquals;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CanaryApplicationTests {

	@Resource
	private UserService userService;

	/**
	 * Tests against an H2 in-memory database specified in
	 * /test/resources/application.properties
	 */
	@Test
	public void testCRUD() {

		User nullUser = userService.findUserByEmail("test@guy.com");
		assertEquals(null, nullUser);

		User user = new User();
		user.setEmail("test@guy.com");
		user.setFirstName("Test");
		user.setLastName("Guy");
		user.setPassword("password");
		userService.addUser(user);

		User user2 = userService.findUserByEmail("test@guy.com");
		assertEquals("Test", user2.getFirstName());

		user2.setFirstName("Test2");
		userService.updateUser(user2);

		User user3 = userService.findUserByEmail("test@guy.com");
		assertEquals("Test2", user3.getFirstName());

		user3.setEmail("test2@guy.com");
		userService.updateUser(user3);

		User user4 = userService.findUserByEmail("test2@guy.com");
		assertEquals("Test2", user4.getFirstName());

		User user5 = userService.findUserByEmail("test@guy.com");
		assertEquals(null, user5);

		userService.removeUser(user4);

		User user6 = userService.findUserByEmail("test2@guy.com");
		assertEquals(null, user6);
	}

}
