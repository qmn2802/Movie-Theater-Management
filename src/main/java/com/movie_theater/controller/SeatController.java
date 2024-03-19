package com.movie_theater.controller;

import com.movie_theater.dto.ResponseDTO;
import com.movie_theater.dto.SeatDTO;
import com.movie_theater.entity.CinemaRoom;
import com.movie_theater.entity.Seat;
import com.movie_theater.security.CustomAccount;
import com.movie_theater.service.CinemaRoomService;
import com.movie_theater.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class SeatController {
    private CinemaRoomService cinemaRoomService;
    private SeatService seatService;

    @Autowired
    public SeatController(CinemaRoomService cinemaRoomService, SeatService seatService) {
        this.cinemaRoomService = cinemaRoomService;
        this.seatService = seatService;
    }

    @PostMapping("/admin/update-booked-seat")
    public ResponseEntity<String> updateBookedSeat(@RequestBody SeatDTO seatDTO) {
        List<Integer> seatIdsBooked = seatDTO.getSeatIdsBooked();
        List<Integer> seatIdsCancelled = seatDTO.getSeatIdsCancelled();

        for (int i = 0; i < seatIdsBooked.size(); i++) {
            seatService.updateSeatStatus(1, seatIdsBooked.get(i));
        }

        for (int i = 0; i < seatIdsCancelled.size(); i++) {
            seatService.updateSeatStatus(0, seatIdsCancelled.get(i));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/admin/update-type-seat")
    public ResponseEntity<String> updateTypeSeat(@RequestBody SeatDTO seatDTO) {
        List<Integer> seatIdsBookedVip = seatDTO.getSeatIdsBookedVip();
        List<Integer> seatIdsCancelledVip = seatDTO.getSeatIdsCancelledVip();

        for (int i = 0; i < seatIdsBookedVip.size(); i++) {
            seatService.updateSeatType(1, seatIdsBookedVip.get(i));
        }

        for (int i = 0; i < seatIdsCancelledVip.size(); i++) {
            seatService.updateSeatType(0, seatIdsCancelledVip.get(i));
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/admin/update-deleted-seat")
    public ResponseEntity<String> updateDeletedSeat(@RequestBody SeatDTO seatDTO) {
        List<String> seatNamesAdded = seatDTO.getSeatNamesAdded();
        List<String> seatNamesDeleted = seatDTO.getSeatNamesDeleted();
        CinemaRoom cinemaRoom = CinemaRoom.builder().cinemaRoomId(seatDTO.getCinemaRoomId()).build();
//        int numberRowEffect = 0;
        for (int i = 0; i < seatNamesAdded.size(); i++) {
              int isUpdated = seatService.updateDeletedBySeatRowAndSeatColumnAndCinemaRoom(false,
                    Integer.parseInt(String.valueOf(seatNamesAdded.get(i).charAt(0))),
                    String.valueOf(seatNamesAdded.get(i).charAt(1)), cinemaRoom
              );

              if(isUpdated != 1) {
                  Seat seat = Seat.builder()
                          .seatType(0)
                          .deleted(false)
                          .seatStatus(0)
                          .seatColumn(String.valueOf(seatNamesAdded.get(i).charAt(1)))
                          .seatRow(Integer.parseInt(String.valueOf(seatNamesAdded.get(i).charAt(0))))
                          .cinemaRoom(cinemaRoom)
                          .build();
                  seatService.save(seat);
              }
        }

//        if (numberRowEffect == 0){
//            return ResponseEntity.badRequest().body(ResponseDTO.builder().message("Error").build());
//        }

        for (int i = 0; i < seatNamesDeleted.size(); i++) {
            int isUpdated = seatService.updateDeletedBySeatRowAndSeatColumnAndCinemaRoom(true,
                    Integer.parseInt(String.valueOf(seatNamesDeleted.get(i).charAt(0))),
                    String.valueOf(seatNamesDeleted.get(i).charAt(1)), cinemaRoom
            );

            if(isUpdated != 1) {
                Seat seat = Seat.builder()
                        .seatType(0)
                        .deleted(true)
                        .seatStatus(0)
                        .seatColumn(String.valueOf(seatNamesDeleted.get(i).charAt(1)))
                        .seatRow(Integer.parseInt(String.valueOf(seatNamesDeleted.get(i).charAt(0))))
                        .cinemaRoom(cinemaRoom)
                        .build();
                seatService.save(seat);
            }
        }

        cinemaRoomService.updateSeatQuantity(cinemaRoom);

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
