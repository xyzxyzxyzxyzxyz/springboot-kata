package com.tdd.katas.springboot.repositories;

import com.tdd.katas.springboot.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testFindAllShouldReturnNoResults() {
        List<User> userEMList = findAllUsers();

        List<User> usersList = (List<User>) userRepository.findAll();

        assertTrue("There should be no users initially", userEMList.isEmpty());
        assertTrue("The repository should not return any users also", usersList.isEmpty());
    }

    @Test
    public void testFindAllShouldReturnAllResults() {
        User savedUser1 =  createNewUser("login","password", "email@email.com");
        User savedUser2 =  createNewUser("login2","password2", "email2@email.com");

        List<User> usersList = (List<User>) userRepository.findAll();

        assertEquals("The size", 2, usersList.size());

        int index;

        index = usersList.indexOf(savedUser1);
        assertNotEquals("savedUser1 should be in the list", -1, index);
        assertUsersAreEqual(savedUser1, usersList.get(index));

        index = usersList.indexOf(savedUser2);
        assertNotEquals("savedUser2 should be in the list", -1, index);
        assertUsersAreEqual(savedUser2, usersList.get(index));
    }

    private List<User> findAllUsers() {
        EntityManager em = entityManager.getEntityManager();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> cq = cb.createQuery(User.class);
        Root<User> rootEntry = cq.from(User.class);
        CriteriaQuery<User> all = cq.select(rootEntry);

        TypedQuery<User> allQuery = em.createQuery(all);

        return allQuery.getResultList();
    }

    @Test
    public void testSaveShouldStoreUserAndReturnIt() {

        User userToBeSaved = new User(null,"login","password", "email@email.com");

        User savedUser =  userRepository.save(userToBeSaved);

        assertNotNull("The user should not be null", savedUser);
        assertNotNull("The userId should not be null", savedUser.getId());
        assertUsersDataIsEqual(userToBeSaved, savedUser);

        User foundUser = entityManager.find(User.class, savedUser.getId());

        assertNotNull("The user should not be null", foundUser);
        assertUsersAreEqual(savedUser, foundUser);
    }

    @Test
    public void testFindOneShouldRetrieveTheSpecifiedUser() {

        User userToBeRetrieved = createNewUser("login","password", "email@email.com");

        User foundUser = userRepository.findOne(userToBeRetrieved.getId());

        assertNotNull("The user should not be null", foundUser);
        assertUsersAreEqual(userToBeRetrieved, foundUser);

    }

    @Test
    public void testUpdateShouldUpdateUserAndReturnIt() {
        // Create it with EM
        User savedUser = createNewUser("login","password", "email@email.com");

        // Update the user with the Repository
        User userToBeUpdated = new User(savedUser.getId(), "login2","password2", "email2@email.com");
        User updatedUser =  userRepository.save(userToBeUpdated);

        // Compare the data in the returned object
        assertUsersAreEqual(userToBeUpdated, updatedUser);

        // Look for the object in the database with EM
        User retrievedUser = entityManager.find(User.class, updatedUser.getId());

        // Look for the object in the database with EM
        assertNotNull("The user should not be null", retrievedUser);
        assertUsersAreEqual(retrievedUser, updatedUser);

    }

    @Test
    public void testDeleteShouldRemoveAnExistingUser() {

        // Create a new user with EM
        User savedUser = createNewUser("login","password", "email@email.com");

        // Delete it with the Repository
        userRepository.delete(savedUser.getId());

        // Check that the user does not exist with the EM
        User deletedUser = entityManager.find(User.class, savedUser.getId());

        assertNull("The user should no longer exist", deletedUser);

    }

    @Test
    public void testFindAllByLoginPasswordEmailShouldReturnOnlyMatchingUsers() {
        // Create
        User matchingUser1 = createNewUser("login","password", "email@email.com");
        User matchingUser2 = createNewUser("login","password", "email@email.com");

        User unmatchingUser1 = createNewUser("login2","password", "email@email.com");
        User unmatchingUser2 = createNewUser("login","password2", "email@email.com");
        User unmatchingUser3 = createNewUser("login","password", "email2@email.com");

        User userFilter = new User(null,"login","password", "email@email.com");
        List<User> foundUsers =  userRepository.findAllByLoginPasswordEmail(userFilter);

        // Check that all matching users are returned
        assertTrue("Should contain a matching user", foundUsers.contains(matchingUser1));
        assertTrue("Should contain a matching user", foundUsers.contains(matchingUser2));

        // Check that the unmatched users are not returned
        assertFalse("Should not contain an unmatching user", foundUsers.contains(unmatchingUser1));
        assertFalse("Should not contain an unmatching user", foundUsers.contains(unmatchingUser2));
        assertFalse("Should not contain an unmatching user", foundUsers.contains(unmatchingUser3));

        List<User> userEMList = entityManager.getEntityManager().
                createQuery("select u from User u where u.login = :login and u.password = :password and u.email = :email", User.class)
                    .setParameter("login", userFilter.getLogin())
                    .setParameter("password", userFilter.getPassword())
                    .setParameter("email", userFilter.getEmail())
                .getResultList();

        assertEquals("Both lists have the same size", userEMList.size(), foundUsers.size());
        assertTrue("Both lists have the same users", userEMList.containsAll(foundUsers));

    }


    private User createNewUser(String login, String password, String email) {
        User userToBeSaved = new User(null,login,password, email);
        return entityManager.persist(userToBeSaved);
    }


    private void assertUsersAreEqual(User expectedUser, User actualUser) {
        // Check the ID
        assertEquals("The userId should match", expectedUser.getId(), actualUser.getId());
        // Check the data
        assertUsersDataIsEqual(expectedUser, actualUser);
    }

    private void assertUsersDataIsEqual(User expectedUser, User actualUser) {
        assertEquals("The login should match", expectedUser.getLogin(), actualUser.getLogin());
        assertEquals("The password should match", expectedUser.getPassword(), actualUser.getPassword());
        assertEquals("The email should match", expectedUser.getEmail(), actualUser.getEmail());
    }

}
