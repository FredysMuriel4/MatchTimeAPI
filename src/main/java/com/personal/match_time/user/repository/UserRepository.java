package com.personal.match_time.user.repository;

import com.personal.match_time.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
