package com.tdd.katas.springboot.repositories;

import com.tdd.katas.springboot.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long>{

    List<User> findAllByLoginPasswordEmail(User userFilter);

}
