package store.back.global.support;

import store.back.store.order.controller.OrderController;
import store.global.communication.CustomCommunicationCode;
import store.global.communication.CustomCommunicationData;
import store.global.communication.Telecommunication;
import store.global.dto.request.OrderRequestDTO;
import store.global.dto.request.PayRequestDTO;
import store.global.exception.CustomIllegalArgumentException;

public class FrontController {

    public static void run() {
        while (true) {
            CustomCommunicationData customCommunicationData = Telecommunication.receiveFromFront();
            if (customCommunicationData.code() == CustomCommunicationCode.TERMINATE) {
                break;
            }

            handleResponse(customCommunicationData);
        }
    }

    private static void handleResponse(CustomCommunicationData customCommunicationData) {
        try {
            Telecommunication.sendSuccessToFront(orderRequestHandling(customCommunicationData.response()));
        } catch (CustomIllegalArgumentException e) {
            Telecommunication.sendErrorToFrontWithData(e.getMessage(), e.errorData());
        }
    }

    private static Object orderRequestHandling(Object requestObject) {
        OrderController orderController = ApplicationContext.getBean(OrderController.class);
        if (requestObject == null) {
            return orderController.getStorageState();
        } else if (requestObject instanceof OrderRequestDTO orderRequestDTO) {
            return orderController.order(orderRequestDTO);
        } else if (requestObject instanceof PayRequestDTO payRequestDTO) {
            return orderController.pay(payRequestDTO);
        }

        throw new RuntimeException("잘못된 요청입니다.");
    }
}
