package com.ernyka.cardmanager.controller;

import com.ernyka.cardmanager.dto.CardTransactionDTO;
import com.ernyka.cardmanager.dto.DateRequest;
import com.ernyka.cardmanager.service.CardTransactionService;
import com.ernyka.cardmanager.viewModel.CardTransactionViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ernyka/card/transaction")
public class CardTransactionController {
    @Autowired
    private CardTransactionService service;

    @PostMapping("start")
    public CardTransactionDTO saveTransaction(@RequestBody CardTransactionViewModel transaction) {
        return service.save(transaction);
    }

    @GetMapping("get/from/{page}/{size}")
    public Page<CardTransactionDTO> getAllByCardFromUuid(@RequestParam("from") String fromUuid,
                                                         @PathVariable("page") Integer page,
                                                         @PathVariable("size") Integer size) {
        return service.getAllByCardFromUuid(fromUuid, page, size);
    }

    @GetMapping("get/to/{page}/{size}")
    public Page<CardTransactionDTO> getAllByCardToUuid(@RequestParam("to") String toUuid, @PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        return service.getAllByCardToUuid(toUuid, page, size);
    }

    @GetMapping("get/uuid/{page}/{size}")
    public Page<CardTransactionDTO> getAllByCardFromUuidAndCardToUuid(@RequestParam("from") String fromUuid, @RequestParam("to") String toUuid, @PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        return service.getAllByCardFromUuidAndCardToUuid(fromUuid, toUuid, page, size);
    }

    @GetMapping("get/successful/{page}/{size}")
    public Page<CardTransactionDTO> getAllByStatusSuccess(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        return service.getAllByStatusSuccess(page, size);
    }

    @GetMapping("get/unsuccessful/{page}/{size}")
    public Page<CardTransactionDTO> getAllByStatusNotSuccess(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        return service.getAllByStatusNotSuccess(page, size);
    }

    @GetMapping("getAll/between/{page}/{size}")
    public Page<CardTransactionDTO> getAllByCreatedDateBetween(@RequestBody DateRequest request,
                                                               @PathVariable("page") Integer page,
                                                               @PathVariable("size") Integer size) {
        return service.getAllByCreatedDateBetween(request.getFrom(), request.getTo(), page, size);
    }

    @GetMapping("getAll/after/{page}/{size}")
    public Page<CardTransactionDTO> getAllByCreatedDateBefore(@RequestBody DateRequest date,
                                                              @PathVariable("page") Integer page,
                                                              @PathVariable("size") Integer size) {
        return service.getAllByCreatedDateBefore(date.getFrom(), page, size);
    }

    @GetMapping("getAll/before/{page}/{size}")
    public Page<CardTransactionDTO> getAllByCreatedDateAfter(@RequestBody DateRequest date,
                                                             @PathVariable("page") Integer page,
                                                             @PathVariable("size") Integer size) {
        return service.getAllByCreatedDateAfter(date.getFrom(), page, size);
    }
}
