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

    List<User> findAllByLoginPasswordEmail_variant2(User userFilter);
}
