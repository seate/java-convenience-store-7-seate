package store.back.store.order.controller;

import store.back.global.annotation.Controller;
import store.back.store.order.service.StoreService;
import store.global.dto.request.OrderRequestDTO;
import store.global.dto.response.CurrentStorageResponseDTOs;
import store.global.dto.response.OrderResponseDTO;

@Controller
public class OrderController {

    private final StoreService storeService;


    public OrderController(StoreService storeService) {
        this.storeService = storeService;
    }


    public CurrentStorageResponseDTOs getStorageState() {
        return storeService.currentProductInform();
    }

    public OrderResponseDTO order(OrderRequestDTO orderRequestDTO) {
        return storeService.order(orderRequestDTO);
    }
}
