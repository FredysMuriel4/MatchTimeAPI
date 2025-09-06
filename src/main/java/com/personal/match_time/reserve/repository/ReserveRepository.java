package com.personal.match_time.Reserve.Repository;

import com.personal.match_time.Field.Model.Field;
import com.personal.match_time.Reserve.Model.Reserve;
import com.personal.match_time.User.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    List<Reserve> findAllByUser(User user);
    List<Reserve> findAllByfield(Field field);
}
