package com.movie_theater.service.impl;

import com.movie_theater.dto.InvoiceItemDTO;
import com.movie_theater.entity.Account;
import com.movie_theater.entity.InvoiceItem;
import com.movie_theater.repository.InvoiceItemRepository;
import com.movie_theater.service.InvoiceItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceItemServiceImpl implements InvoiceItemService {
    @Autowired
    InvoiceItemRepository invoiceItemRepository;

    @Override
    public List<InvoiceItemDTO> findInvoiceItemByAccountAndInvoiceId(Account account, Integer invoiceId) {
        return invoiceItemRepository.findInvoiceItemByAccountAndInvoiceId(account,invoiceId);
    }

    @Override
    public Page<InvoiceItemDTO> findInvoiceItemByAccount(Account account,String movieName, Pageable pageable) {
        return invoiceItemRepository.findInvoiceItemByAccountAndMovieName(account,movieName,pageable);
    }

    @Override
    public Page<InvoiceItem> findByInvoiceItemId(String invoiceItemId, Pageable pageable) {
        return invoiceItemRepository.findByInvoiceItemIdContains("%" + invoiceItemId + "%",pageable);
    }

    @Override
    public InvoiceItem save(InvoiceItem invoiceItem) {
        return invoiceItemRepository.save(invoiceItem);}
    @Override
    public  int updateInvoiceStatusByInvoiceItemId(int invoiceItemId, Boolean status){
        return invoiceItemRepository.updateInvoiceStatusByInvoiceItemId(invoiceItemId,status);
    }

    public InvoiceItem getById(Integer invoiceItemId){
      return  invoiceItemRepository.getByInvoiceItemId(invoiceItemId);
    }
}