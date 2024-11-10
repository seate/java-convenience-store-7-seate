package store.back.store.order.controller;

import store.back.global.annotation.Controller;
import store.back.store.order.service.StoreService;
import store.global.dto.response.CurrentStorageResponseDTOs;

@Controller
public class OrderController {

    private final StoreService storeService;


    public OrderController(StoreService storeService) {
        this.storeService = storeService;
    }


    public CurrentStorageResponseDTOs getStorageState() {
        return storeService.currentProductInform();
    }
}
