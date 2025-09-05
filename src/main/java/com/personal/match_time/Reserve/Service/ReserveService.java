package com.personal.match_time.Reserve.Service;

import com.personal.match_time.Field.Model.Field;
import com.personal.match_time.Field.Service.FieldService;
import com.personal.match_time.Reserve.Model.Reserve;
import com.personal.match_time.Reserve.Repository.ReserveRepository;
import com.personal.match_time.Reserve.Request.ReserveRequest;
import com.personal.match_time.User.Model.User;
import com.personal.match_time.User.Service.UserService;
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
                reserveRequest.getEndTime()
        );

        this.validateUserDisponibility(
                reserveRequest.getFieldId(),
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

        Reserve reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserve Not found"));


        reserve.setStatus("CANCELLED");

        return reserveRepository.save(reserve);
    }

    public Reserve confirmReserve(Long id) {

        Reserve reserve = reserveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reserve Not found"));

        if(reserve.getStatus().equals("CANCELLED")) {

            throw new IllegalArgumentException("Invalid reserve!");
        }

        reserve.setStatus("CONFIRMED");

        return reserveRepository.save(reserve);
    }

    public void validateFieldDisponibility(
            Long fieldId, LocalDate date, LocalTime startTime, LocalTime endTime
    ) {

        List<Reserve> fieldReserves = this.getReservesByField(fieldId);

        for (Reserve reserve: fieldReserves) {

            if(reserve.getDate().equals(date)) {

                if(
                    (
                        startTime.isAfter(reserve.getStartTime()) &&
                        startTime.isBefore(reserve.getEndTime())
                    ) ||
                    (
                        startTime.equals(reserve.getStartTime()) &&
                        endTime.equals(reserve.getEndTime())
                    )
                ) {

                    throw new IllegalArgumentException("A reservation exist for this range of hours!");
                }
            }
        }
    }

    public void validateUserDisponibility(
            Long userId, LocalDate date, LocalTime startTime, LocalTime endTime
    ) {

        List<Reserve> userReserves = this.getReservesByField(userId);

        for (Reserve reserve: userReserves) {

            if(
                (reserve.getStatus().equals("PENDING")) ||
                (reserve.getStatus().equals("CONFIRMED"))
            ) {

                throw new IllegalArgumentException("The user have active reservations!");
            }

            if(reserve.getDate().equals(date)) {

                if(
                    (
                        startTime.isAfter(reserve.getStartTime()) &&
                        startTime.isBefore(reserve.getEndTime())
                    ) ||
                    (
                        startTime.equals(reserve.getStartTime()) &&
                        endTime.equals(reserve.getEndTime())
                    )
                ) {

                    throw new IllegalArgumentException("A reservation exist at this range of hours!");
                }
            }
        }
    }
}
