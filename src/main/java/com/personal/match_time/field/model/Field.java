package com.personal.match_time.field.model;

import com.personal.match_time.field.request.FieldRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "fields")
public class Field {

    public Field() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "hour_price", nullable = false)
    private Double hourPrice;

    @Column(name = "type", nullable = false)
    private String type;

    @CreatedDate
    private Instant createdAt;

    public Field(FieldRequest fieldRequest) {

        this.name = fieldRequest.getName();
        this.description = fieldRequest.getDescription();
        this.hourPrice = fieldRequest.getHourPrice();
        this.type = fieldRequest.getType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getHourPrice() {
        return hourPrice;
    }

    public void setHourPrice(Double hourPrice) {
        this.hourPrice = hourPrice;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
