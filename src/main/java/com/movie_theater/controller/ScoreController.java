package com.movie_theater.controller;

import com.movie_theater.dto.InvoiceItemDTO;
import com.movie_theater.dto.ResponseDTO;
import com.movie_theater.dto.ScoreHisDTO;
import com.movie_theater.entity.Account;
import com.movie_theater.entity.Invoice;
import com.movie_theater.entity.InvoiceItem;
import com.movie_theater.security.CustomAccount;
import com.movie_theater.service.AccountService;
import com.movie_theater.service.InvoiceItemService;
import com.movie_theater.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
public class ScoreController {


    InvoiceService invoiceService;

    AccountService accountService;

    InvoiceItemService invoiceItemService;


    @Autowired

    public ScoreController(InvoiceService invoiceService, AccountService accountService, InvoiceItemService invoiceItemService) {
        this.invoiceService = invoiceService;
        this.accountService = accountService;
        this.invoiceItemService = invoiceItemService;
    }

    @GetMapping("/get-history-of-score")
    public ResponseEntity<ResponseDTO> getHistoryOfScore(@RequestParam Integer pageNumber,
                                                         @RequestParam Integer pageSize,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
                                                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
                                                         @RequestParam(required = false) Boolean viewScoreAdding,
                                                         @RequestParam(required = false) Boolean viewScoreUsing,
                                                         Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDTO.builder().message("Login before").build());
        }

        Account account = accountService.getByUsername(((CustomAccount) authentication.getPrincipal()).getUsername());

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Invoice> invoiceList = invoiceService.findInvoicesByAccountAndDateRangeAndConditionalScores(account, fromDate.atStartOfDay(), toDate.atStartOfDay(), viewScoreAdding, viewScoreUsing, pageable);


        List<ScoreHisDTO> lstScoreHisDTO = new ArrayList<>();
        for(Invoice item : invoiceList){

            AtomicReference<Double> totalMoney = new AtomicReference<>(0d);
            List<InvoiceItem> lstInvoiceItem = item.getInvoiceItem();
            lstInvoiceItem.forEach(invoiceItem -> {
                if(invoiceItem.getFoodHis() != null){
                    totalMoney.updateAndGet(v -> v + invoiceItem.getFoodHis().getFoodPrice());
                }
                if(invoiceItem.getScheduleSeatHis() != null){
                    totalMoney.updateAndGet(v -> v + invoiceItem.getScheduleSeatHis().getPrice());
                }
            });

            lstScoreHisDTO.add( ScoreHisDTO.builder()
                    .bookingDate(item.getBookingDate())
                    .addScore(item.getAddScore())
                    .usedScore(item.getUseScore())
                    .invoiceId(item.getInvoiceId())
                    .status(item.getStatus())
                    .totalMoney(totalMoney.get() * (1 - (item.getPromotion() != null ? item.getPromotion().getDiscountLevel() : 0) / 100 ))
                    .discount(item.getPromotion() != null ? item.getPromotion().getDiscountLevel() : 0)
                    .build());
        }


        HashMap<String, Object> mapInvoice = new HashMap<>();
        mapInvoice.put("lstScoreHisDTO", lstScoreHisDTO);
        mapInvoice.put("pageNumber", invoiceList.getNumber());
        mapInvoice.put("pageSize", invoiceList.getSize());
        mapInvoice.put("totalPage", invoiceList.getTotalPages());


        return ResponseEntity.ok().body(ResponseDTO.builder().data(mapInvoice).message("Success").build());
    }

    @GetMapping("/get-invoice-detail")
    public ResponseEntity<ResponseDTO> getInvoiceDetail(@RequestParam Integer invoiceId, Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ResponseDTO.builder().message("Login Before").build());
        }

        CustomAccount customAccount = (CustomAccount) authentication.getPrincipal();
        Account account = accountService.getByUsername(customAccount.getUsername());

        List<InvoiceItemDTO> lstInvoiceItemDTO = invoiceItemService.findInvoiceItemByAccountAndInvoiceId(account, invoiceId);

        return ResponseEntity.ok().body(ResponseDTO.builder().message("OKE").data(lstInvoiceItemDTO).build());
    }
}
