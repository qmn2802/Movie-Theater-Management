package com.movie_theater.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketDTO {
    private List<Integer> seats;
    private List<FoodDTO> foods;
}
