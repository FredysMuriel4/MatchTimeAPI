package com.personal.match_time.Field.Service;

import com.personal.match_time.Field.Model.Field;
import com.personal.match_time.Field.Repository.FieldRepository;
import com.personal.match_time.Field.Request.FieldRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FieldService {

    private FieldRepository fieldRepository;

    public FieldService(FieldRepository fieldRepository) {

        this.fieldRepository = fieldRepository;
    }

    public List<Field> getAllFields() {

        return fieldRepository.findAll();
    }

    public Field getFieldById(Long id) {

        return fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field Not found"));
    }

    public Field storeField(FieldRequest fieldRequest) {

        Field field = new Field(fieldRequest);
        field.setCreatedAt(Instant.now());

        return fieldRepository.save(field);
    }


}
