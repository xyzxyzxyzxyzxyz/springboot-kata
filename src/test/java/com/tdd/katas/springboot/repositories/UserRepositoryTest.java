package com.tdd.katas.springboot.repositories;

import com.tdd.katas.springboot.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    /*
        findAll - 0 results
        createOne
        findAll - 1+ results
            - create
        findOne
        updateOne
        deleteOne

     */

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindAllShouldReturnNoResults() {
        List<User> usersList = (List<User>) userRepository.findAll();
        assertTrue("There should be no users initially", usersList.isEmpty());
    }

    @Test
    public void testSaveShouldStoreUserAndReturnIt() {

        User userToBeSaved = new User(null,"login","password", "email@email.com");

        User savedUser =  userRepository.save(userToBeSaved);

        assertNotNull("The user should not be null", savedUser);
        assertNotNull("The userId should not be null", savedUser.getId());
        assertEquals("The login should match ", userToBeSaved.getLogin(), savedUser.getLogin());
        assertEquals("The password should match ", userToBeSaved.getPassword(), savedUser.getPassword());
        assertEquals("The email should match ", userToBeSaved.getEmail(), savedUser.getEmail());

    }

    @Test
    public void testFindOneShouldRetrieveTheSpecifiedUser() {

        User userToBeRetrieved = new User(null,"login","password", "email@email.com");
        userToBeRetrieved = userRepository.save(userToBeRetrieved);

        User foundUser = userRepository.findOne(userToBeRetrieved.getId());

        assertNotNull("The user should not be null", foundUser);
        assertEquals("The ID should match ", userToBeRetrieved.getId(), foundUser.getId());
        assertEquals("The login should match ", userToBeRetrieved.getLogin(), foundUser.getLogin());
        assertEquals("The password should match ", userToBeRetrieved.getPassword(), foundUser.getPassword());
        assertEquals("The email should match ", userToBeRetrieved.getEmail(), foundUser.getEmail());

    }

    @Test
    public void testUpdateShouldUpdateUserAndReturnIt() {

        User userToBeSaved = new User(null,"login","password", "email@email.com");
        User savedUser =  userRepository.save(userToBeSaved);

        User userToBeUpdated = new User(savedUser.getId(), "login2","password2", "email2@email.com");

        User updatedUser =  userRepository.save(userToBeUpdated);

        assertEquals("The id should match ", userToBeUpdated.getId(), updatedUser.getId());
        assertEquals("The login should match ", userToBeUpdated.getLogin(), updatedUser.getLogin());
        assertEquals("The password should match ", userToBeUpdated.getPassword(), updatedUser.getPassword());
        assertEquals("The email should match ", userToBeUpdated.getEmail(), updatedUser.getEmail());

    }


}
