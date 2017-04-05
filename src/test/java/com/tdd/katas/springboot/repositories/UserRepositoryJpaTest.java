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
        User userToBeSaved1 = new User(null,"login","password", "email@email.com");
        User userToBeSaved2 = new User(null,"login2","password2", "email2@email.com");

        User savedUser1 =  entityManager.persist(userToBeSaved1);
        User savedUser2 =  entityManager.persist(userToBeSaved2);

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

        User userToBeRetrieved = new User(null,"login","password", "email@email.com");
        userToBeRetrieved = entityManager.persist(userToBeRetrieved);

        User foundUser = userRepository.findOne(userToBeRetrieved.getId());

        assertNotNull("The user should not be null", foundUser);
        assertUsersAreEqual(userToBeRetrieved, foundUser);

    }

    @Test
    public void testUpdateShouldUpdateUserAndReturnIt() {
        // Create it with EM
        User userToBeSaved = new User(null,"login","password", "email@email.com");
        User savedUser =  entityManager.persist(userToBeSaved);

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
        User userToBeSaved = new User(null,"login","password", "email@email.com");
        User savedUser =  entityManager.persist(userToBeSaved);

        // Delete it with the Repository
        userRepository.delete(savedUser.getId());

        // Check that the user does not exist with the EM
        User deletedUser = entityManager.find(User.class, savedUser.getId());

        assertNull("The user should no longer exist", deletedUser);

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
