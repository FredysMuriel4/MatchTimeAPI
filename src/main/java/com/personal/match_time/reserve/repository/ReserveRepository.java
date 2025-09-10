package com.personal.match_time.reserve.repository;

import com.personal.match_time.field.model.Field;
import com.personal.match_time.reserve.model.Reserve;
import com.personal.match_time.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    List<Reserve> findAllByUser(User user);
    List<Reserve> findAllByfield(Field field);
}
