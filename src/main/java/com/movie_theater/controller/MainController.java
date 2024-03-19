package com.movie_theater.controller;

import com.movie_theater.dto.ResponseDTO;
import com.movie_theater.dto.TypeDTO;
import com.movie_theater.entity.Type;
import com.movie_theater.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class MainController {

    @GetMapping(value = {"/home", "/"})
    public String getHome() {
        return "home";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "redirect:/home";
    }

    @GetMapping("/forgot-password")
    public String getForgotPassword() {
        return "forgotPassword";
    }

    @GetMapping("/account-information")
    public String getEditAccountInformation() {
        return "editAccountInformation";
    }

    @GetMapping("/booked-ticket")
    public String getBookedTicket() {
        return "bookedTicket";
    }

    @GetMapping("/my-invoice")
    public String getViewScore() {
        return "viewScore";
    }

    @GetMapping("/admin/showtime")
    public String getShowtimeForAdmin() {
        return "showtime";
    }

    @GetMapping("/admin/select-seat")
    public String getSelectSeat() {
        return "selectSeat";
    }

    @GetMapping("/admin/movie-list")
    public String getMovieListForAdmin() {
        return "adminMovieList";
    }

    @GetMapping("/movie-list")
    public String getMovieList() {
        return "movieList";
    }

    @GetMapping("/admin/employee-manager")
    public String getEmployeeList() {
        return "employeeList";
    }

    @GetMapping("/admin/edit-employee")
    public String getEditEmployee() {
        return "editEmployee";
    }

    @GetMapping("/admin/confirm-ticket")
    public String getConfirmTicket() {
        return "confirmTicket";
    }

    @GetMapping("/admin/ticket-manager")
    public String getBookingList() {
        return "bookingList";
    }

    @GetMapping("/admin/add-employee")
    public String getAddEmployee() {
        return "addEmployee";
    }

    @GetMapping("/admin/promotion-management")
    public String getPromotionManagement() {
        return "promotionManagement";
    }

    @GetMapping("/admin/cinema-room")
    public String getCinemaRoomList() {
        return "cinemaRoom";
    }

    @GetMapping("/admin/schedule-management")
    public String getScheduleManagement() {
        return "scheduleManagement";
    }

    @GetMapping("/admin/food-management")
    public String getFoodManagement() {
        return "foodManagement";
    }
    @GetMapping("/payment-gateways")
    public String getOnlinePayment() {
        return "paymentGateways";
    }
    @GetMapping("/payment-error")
    public String getPaymentError() {
        return "paymentError";
    }

    @GetMapping("/booking-ticket")
    public String getBookingTicket() {
        return "bookTicket";
    }
}