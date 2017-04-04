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
        assertEquals("The login should match ", userToBeSaved.getLogin(), savedUser.getLogin());
        assertEquals("The password should match ", userToBeSaved.getPassword(), savedUser.getPassword());
        assertEquals("The email should match ", userToBeSaved.getEmail(), savedUser.getEmail());

        User foundUser = entityManager.find(User.class, savedUser.getId());

        assertNotNull("The user should not be null", foundUser);
        assertEquals("The userId should match ", savedUser.getId(), foundUser.getId());
        assertEquals("The login should match ", savedUser.getLogin(), foundUser.getLogin());
        assertEquals("The password should match ", savedUser.getPassword(), foundUser.getPassword());
        assertEquals("The email should match ", savedUser.getEmail(), foundUser.getEmail());

    }

}
