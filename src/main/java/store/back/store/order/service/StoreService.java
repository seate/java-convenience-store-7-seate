package store.back.store.order.service;

import store.global.dto.request.OrderRequestDTO;
import store.global.dto.response.CurrentStorageResponseDTOs;
import store.global.dto.response.OrderResponseDTO;

public interface StoreService {

    CurrentStorageResponseDTOs currentProductInform();

    OrderResponseDTO order(OrderRequestDTO orderRequestDTO);
}
