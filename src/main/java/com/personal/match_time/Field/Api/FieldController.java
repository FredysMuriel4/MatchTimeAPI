package com.personal.match_time.Field.Api;

import com.personal.match_time.Field.Model.Field;
import com.personal.match_time.Field.Request.FieldRequest;
import com.personal.match_time.Field.Service.FieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fields")
public class FieldController {

    private FieldService fieldService;

    public FieldController(FieldService fieldService) {

        this.fieldService = fieldService;
    }

    @GetMapping
    public List<Field> getAllFields() {

        return fieldService.getAllFields();
    }

    @PostMapping("/save")
    public Field storeField(
            @Validated @RequestBody FieldRequest fieldRequest
    ) {

        return fieldService.storeField(fieldRequest);
    }
}
