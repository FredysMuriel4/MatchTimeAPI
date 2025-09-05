package com.personal.match_time.Reserve.Api;

import com.personal.match_time.Reserve.Model.Reserve;
import com.personal.match_time.Reserve.Request.ReserveRequest;
import com.personal.match_time.Reserve.Service.ReserveService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reserves")
public class ReserveController {

    private ReserveService reserveService;

    public ReserveController(ReserveService reserveService) {

        this.reserveService = reserveService;
    }

    @GetMapping
    public List<Reserve> getAllReserves() {

        return reserveService.getAllReserves();
    }

    @GetMapping("/by-user")
    public List<Reserve> getReservesByUser(
            @RequestParam("userId") Long id
    ) {

        return reserveService.getReservesByUser(id);
    }

    @GetMapping("/by-field")
    public List<Reserve> getReservesByField(
            @RequestParam("fieldId") Long id
    ) {

        return reserveService.getReservesByField(id);
    }

    @PostMapping("/save")
    public Reserve storeReserve(
            @Validated @RequestBody ReserveRequest reserveRequest
    ) {

       return reserveService.storeReserve(reserveRequest);
    }

    @PutMapping("/cancel/{id}")
    public Reserve cancelReservation(
            @PathVariable Long id
    ) {

        return reserveService.cancelReserve(id);
    }

    @PutMapping("/confirm/{id}")
    public Reserve confirmReservation(
            @PathVariable Long id
    ) {

        return reserveService.confirmReserve(id);
    }
}
