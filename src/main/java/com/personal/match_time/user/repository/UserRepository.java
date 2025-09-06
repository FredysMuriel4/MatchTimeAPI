package com.personal.match_time.User.Repository;

import com.personal.match_time.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
