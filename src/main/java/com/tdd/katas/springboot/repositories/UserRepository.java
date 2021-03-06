package com.tdd.katas.springboot.repositories;

import com.tdd.katas.springboot.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long>{

    @Query(
            "select u " +
            "from User u " +
            "where " +
                    "u.login = :#{#userFilter.login} " +
                    "and u.password = :#{#userFilter.password} " +
                    "and u.email = :#{#userFilter.email}"
    )
    List<User> findAllByLoginPasswordEmail(@Param("userFilter") User userFilter);

    @Query(
            "select u " +
                    "from User u " +
                    "where " +
                    "u.login = ?#{[0]} " +
                    "and u.password = ?#{[1]} " +
                    "and u.email = ?#{[2]}"
    )
    List<User> findAllByLoginPasswordEmail_variant2(String login, String password, String email);

    @Query(
            "select u " +
                    "from User u " +
                    "where " +
                    "u.login = ?#{[0].login} " +
                    "and u.password = ?#{[0].password} " +
                    "and u.email = ?#{[0].email}"
    )
    List<User> findAllByLoginPasswordEmail_variant3(User userFilter);


}
