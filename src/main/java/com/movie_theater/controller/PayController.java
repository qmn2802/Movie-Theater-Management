package com.movie_theater.controller;

import com.movie_theater.dto.PayDTO;
import com.movie_theater.dto.ResponseDTO;
import com.movie_theater.dto.SeatDTO;
import com.movie_theater.entity.Invoice;
import com.movie_theater.final_attribute.STATUS;
import com.movie_theater.service.InvoiceService;
import com.movie_theater.service.PayService;
import com.movie_theater.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PayController {

    private PayService payService;
    private InvoiceService invoiceService;
    private SeatService seatService;
    @Autowired
    public PayController(PayService payService, InvoiceService invoiceService, SeatService seatService) {
        this.payService = payService;
        this.invoiceService = invoiceService;
        this.seatService = seatService;
    }

    @PostMapping ("/payment-checking")
    public ResponseEntity<ResponseDTO> isCompletePayment(@RequestParam String transactionCode, @RequestParam Long amount, @RequestParam String invoiceId) {
        ResponseDTO responseDTO = new ResponseDTO();
        boolean existed = payService.isTransactionCompleted(transactionCode, amount);
        responseDTO.setData(existed);
        if(existed) {
            responseDTO.setMessage("Complete");
            invoiceService.updateStatus(STATUS.CONFIRM, Integer.parseInt(invoiceId));
        } else {
            responseDTO.setMessage("Fail");
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping ("/alter-booked-seat-status")
    public ResponseEntity<ResponseDTO> alterBookedSeatStatus(@RequestBody SeatDTO seatDTO) {
        ResponseDTO responseDTO = new ResponseDTO();
        List<Integer> listSeatId = seatDTO.getSeatIdsBooked();
        for(int seatId : listSeatId) {
            seatService.updateSeatStatus(0,seatId);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
    @GetMapping("/get-transaction-code")
    public ResponseEntity<ResponseDTO> getTransactionCode() {
        ResponseDTO responseDTO = new ResponseDTO();
        String newTransactionCode = payService.createTransactionCode();
        while (payService.isTransactionCodeExisted(newTransactionCode)) {
            newTransactionCode = payService.createTransactionCode();
        }
//        PayDTO payDT0 = PayDTO.builder().transactionCode(newTransactionCode).build();
        responseDTO.setData(newTransactionCode);
        responseDTO.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
