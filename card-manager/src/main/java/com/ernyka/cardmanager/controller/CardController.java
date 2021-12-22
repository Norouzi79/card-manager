package com.ernyka.cardmanager.controller;


import com.ernyka.cardmanager.dto.CardDTO;
import com.ernyka.cardmanager.model.Card;
import com.ernyka.cardmanager.service.CardService;
import com.ernyka.cardmanager.viewModel.CardViewModel;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("ernyka/card")
public class CardController {
    @Autowired
    private CardService service;

    @PostMapping("save")
    public String save(@RequestBody CardViewModel cardViewModel) {
        return service.save(new ModelMapper().map(cardViewModel, Card.class));
    }

    @GetMapping("list/{page}/{size}")
    public Page<CardDTO> getAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        return service.getAll(page, size);
    }

    @GetMapping("get/{uuid}")
    public CardDTO getCardDTOByUuid(@PathVariable String uuid) {
        return service.getCardDTOByUuid(uuid);
    }

    @DeleteMapping("delete/{uuid}")
    public void deleteByUuid(@PathVariable String uuid) {
        service.deleteByUuid(uuid);
    }

}
