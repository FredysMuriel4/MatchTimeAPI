package com.personal.match_time.field.repository;

import com.personal.match_time.field.model.Field;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FieldRepository extends JpaRepository<Field, Long> {
}
