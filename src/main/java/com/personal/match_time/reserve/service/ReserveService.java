package com.personal.match_time.reserve.service;

import com.personal.match_time.field.model.Field;
import com.personal.match_time.field.service.FieldService;
import com.personal.match_time.reserve.model.Reserve;
import com.personal.match_time.reserve.repository.ReserveRepository;
import com.personal.match_time.reserve.request.ReserveRequest;
import com.personal.match_time.user.model.User;
import com.personal.match_time.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReserveService {

    private ReserveRepository reserveRepository;
    private UserService userService;
    private FieldService fieldService;

    public ReserveService(ReserveRepository reserveRepository, UserService userService, FieldService fieldService) {

        this.reserveRepository = reserveRepository;
        this.userService = userService;
        this.fieldService = fieldService;
    }

    public List<Reserve> getAllReserves() {

        return reserveRepository.findAll();
    }

    public Reserve storeReserve(ReserveRequest reserveRequest) {

        this.validateFieldDisponibility(
                reserveRequest.getFieldId(),
                reserveRequest.getDate(),
                reserveRequest.getStartTime(),
                reserveRequest.getEndTime(),
                null
        );

        this.validateUserDisponibility(
                reserveRequest.getUserId(),
                reserveRequest.getDate(),
                reserveRequest.getStartTime(),
                reserveRequest.getEndTime()
        );

        Reserve reserve = new Reserve(reserveRequest);

        User user = userService.getUserById(reserveRequest.getUserId());
        Field field = fieldService.getFieldById(reserveRequest.getFieldId());

        reserve.setUser(user);
        reserve.setField(field);
        reserve.setCreatedAt(Instant.now());
        reserve.setStatus("PENDING");

        return reserveRepository.save(reserve);
    }

    public List<Reserve> getReservesByUser(Long id) {

        User user = userService.getUserById(id);

        return reserveRepository.findAllByUser(user);
    }

    public List<Reserve> getReservesByField(Long id) {

        Field field = fieldService.getFieldById(id);

        return reserveRepository.findAllByfield(field);
    }

    public Reserve cancelReserve(Long id) {

        Reserve reserve = this.getReserveById(id);

        reserve.setStatus("CANCELLED");

        return reserveRepository.save(reserve);
    }

    public Reserve confirmReserve(Long id) {

        Reserve reserve = this.getReserveById(id);

        if(reserve.getStatus().equals("CANCELLED")) {

            throw new IllegalArgumentException("Invalid reserve!");
        }

        reserve.setStatus("CONFIRMED");

        return reserveRepository.save(reserve);
    }

    public Reserve completeReserve(Long id) {

        Reserve reserve = this.getReserveById(id);

        if(!reserve.getStatus().equals("CONFIRMED")) {

            throw new IllegalArgumentException("Invalid reserve!");
        }

        reserve.setStatus("COMPLETED");

        return reserveRepository.save(reserve);
    }

    public Reserve updateReserve(Long id, ReserveRequest reserveRequest) {

        this.validateFieldDisponibility(
                reserveRequest.getFieldId(),
                reserveRequest.getDate(),
                reserveRequest.getStartTime(),
                reserveRequest.getEndTime(),
                id
        );

        Reserve reserve = this.getReserveById(id);

        if(!reserve.getStatus().equals("PENDING")) {

            throw new IllegalArgumentException("Invalid reserve!");
        }

        User user = userService.getUserById(reserveRequest.getUserId());
        Field field = fieldService.getFieldById(reserveRequest.getFieldId());

        reserve.setUser(user);
        reserve.setField(field);
        reserve.setStatus("PENDING");

        return reserveRepository.save(reserve);
    }

    public void validateFieldDisponibility(
            Long fieldId, LocalDate date, LocalTime startTime, LocalTime endTime, Long reserveId
    ) {

        List<Reserve> fieldReserves = this.getReservesByField(fieldId);

        for (Reserve reserve: fieldReserves) {
            if(
                (reserve.getDate().equals(date)) &&
                (reserveId == null)
            ) {

                this.validateReserveByHours(startTime, endTime, reserve);
            }

            if(
                (reserve.getDate().equals(date)) &&
                (reserveId != null) &&
                (!reserveId.equals(reserve.getId()))
            ) {

                this.validateReserveByHours(startTime, endTime, reserve);
            }
        }
    }

    public void validateReserveByHours(LocalTime startTime, LocalTime endTime, Reserve reserve) {

        if(
            (
                startTime.isAfter(reserve.getStartTime()) &&
                startTime.isBefore(reserve.getEndTime())
            ) ||
            (
                endTime.isAfter(reserve.getStartTime()) &&
                endTime.isBefore(reserve.getEndTime())
            ) ||
            (
                startTime.equals(reserve.getStartTime()) &&
                endTime.equals(reserve.getEndTime())
            )
        ) {

            throw new IllegalArgumentException("A reservation exist for this range of hours!");
        }
    }

    public void validateUserDisponibility(
            Long userId, LocalDate date, LocalTime startTime, LocalTime endTime
    ) {

        List<Reserve> userReserves = this.getReservesByUser(userId);

        for (Reserve reserve: userReserves) {

            if(
                (reserve.getStatus().equals("PENDING")) ||
                (reserve.getStatus().equals("CONFIRMED"))
            ) {

                throw new IllegalArgumentException("The user have active reservations!");
            }
        }
    }

    public Reserve getReserveById(Long id) {

        return reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserve Not found"));
    }
}
