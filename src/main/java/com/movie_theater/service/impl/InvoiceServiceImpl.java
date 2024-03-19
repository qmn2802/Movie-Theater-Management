package com.movie_theater.service.impl;

import com.movie_theater.entity.Account;
import com.movie_theater.entity.Invoice;
import com.movie_theater.repository.InvoiceRepository;
import com.movie_theater.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    InvoiceRepository invoiceRepository;



    @Override
    public Page<Invoice> findInvoicesByAccountAndDateRangeAndConditionalScores(Account account, LocalDateTime fromDate, LocalDateTime toDate, boolean viewScoreAdding, boolean viewScoreUsing, Pageable pageable) {
        return invoiceRepository.findInvoicesByAccountAndDateRangeAndConditionalScores(account,fromDate,toDate,viewScoreAdding,viewScoreUsing,pageable);
    }

    @Override
    public Invoice save(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    @Override
    public Invoice getInvoicesByInvoiceId(int invoiceId) {
        return invoiceRepository.getInvoicesByInvoiceId(invoiceId);
    }

    @Override
    public int updateStatus(String status, int invoiceId) {
        return invoiceRepository.updateStatus(status, invoiceId);
    }
}
