package com.movie_theater.repository;

import com.movie_theater.entity.ScheduleSeatHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleSeatHisRepository extends JpaRepository<ScheduleSeatHis,Integer> {
    List<ScheduleSeatHis> getByCinemaRoomIdAndMovieIdAndScheduleTime(int cinemaRoomId, int movieId, LocalDateTime scheduleTime);
}
