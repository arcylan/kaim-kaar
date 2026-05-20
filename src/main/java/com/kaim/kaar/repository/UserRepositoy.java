package com.kaim.kaar.repository;

import com.kaim.kaar.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepositoy extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
