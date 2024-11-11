package store.front;

import java.util.UUID;
import java.util.function.Function;
import store.back.store.storage.exception.LackProductData;
import store.back.store.storage.exception.LackPromotedProductException;
import store.back.store.order.exception.NeedMorePromotedProductException;
import store.back.store.order.exception.NoProductInOrderException;
import store.front.exception.PhaseTerminateException;
import store.front.exception.TerminateException;
import store.front.view.StoreRequestView;
import store.front.view.StoreResponseView;
import store.global.communication.CustomCommunicationCode;
import store.global.communication.CustomCommunicationData;
import store.global.communication.Telecommunication;
import store.global.dto.request.OrderRequestDTO;
import store.global.dto.request.PayRequestDTO;
import store.global.dto.response.CurrentStorageResponseDTOs;
import store.global.dto.response.OrderResponseDTO;
import store.global.dto.response.OrderResultResponseDTO;
import store.global.exception.CustomIllegalArgumentException;
import store.global.status.Agreement;

public class FrontApplication {

    public static void run() {
        try {
            runAll();
        } finally {
            Telecommunication.sendTerminateSignalToBack();
        }
    }

    private static void runAll() {
        Function<Object, Object> orderFunction = o -> orderProducts();
        Function<Object, Object> allFunction = o -> {
            runPhases(getPrintProductFunction(), orderFunction, getPayFunction());
            return null;
        };
        try {
            executeWithRetry(allFunction);
        } catch (TerminateException e) {
            // do nothing
        }
    }

    private static Function<Object, Object> getPrintProductFunction() {
        return o -> {
            printProducts();
            return null;
        };
    }

    private static Function<Object, Object> getPayFunction() {
        return uuidObject -> {
            pay((UUID) uuidObject);
            return null;
        };
    }

    private static void runPhases(Function<Object, Object> printProductFunction,
                                  Function<Object, Object> orderFunction,
                                  Function<Object, Object> payFunction) {
        StoreResponseView.printGreetingMessage();
        printProductFunction.apply(null);
        UUID uuid = (UUID) executeWithPhaseRetry(orderFunction, null);
        executeWithPhaseRetry(payFunction, uuid);
        Agreement agreement = StoreRequestView.buyMoreProducts();
        if (!agreement.isAgreed()) {
            throw new TerminateException();
        }
        throw new PhaseTerminateException();
    }

    private static Object executeWithPhaseRetry(Function<Object, Object> function, Object param) {
        while (true) {
            try {
                return function.apply(param);
            } catch (CustomIllegalArgumentException e) {
                StoreResponseView.printErrorMessage(e.getMessage());
            } /*catch (Exception e) {
                System.out.println("why?: " + e.getMessage());
            }*/
        }
    }

    private static void executeWithRetry(Function<Object, Object> function) {
        while (true) {
            try {
                function.apply(null);
                return;
            } catch (CustomIllegalArgumentException e) {
                StoreResponseView.printErrorMessage(e.getMessage());
            } catch (PhaseTerminateException e) {
                // do nothing
            }
        }
    }

    private static void printProducts() {
        CustomCommunicationData data = Telecommunication.requestToBack(null);
        if (data.code() == CustomCommunicationCode.ERROR) {
            throw new CustomIllegalArgumentException(data.errorMessage());
        }

        StoreResponseView.printProducts((CurrentStorageResponseDTOs) data.response());
    }


    private static UUID orderProducts() {
        OrderRequestDTO orderRequestDTO = StoreRequestView.purchase();
        CustomCommunicationData data = Telecommunication.requestToBack(orderRequestDTO);
        if (data.code() != CustomCommunicationCode.ERROR) {
            return handleSuccessfulOrder(data);
        }
        return handleOrderError(data, orderRequestDTO);
    }

    private static UUID handleSuccessfulOrder(CustomCommunicationData data) {
        OrderResponseDTO orderResponseDTO = (OrderResponseDTO) data.response();
        return orderResponseDTO.uuid();
    }

    private static UUID handleOrderError(CustomCommunicationData data, OrderRequestDTO orderRequestDTO) {
        String errorMessage = data.errorMessage();
        if (errorMessage.equals(new NeedMorePromotedProductException(null, null).getMessage())) {
            return handleNeedMorePromotedProduct(data, orderRequestDTO);
        } else if (errorMessage.equals(new LackPromotedProductException(null, null).getMessage())) {
            return handleLackPromotedProduct(data, orderRequestDTO);
        } else if (errorMessage.equals(new NoProductInOrderException().getMessage())) {
            throw new PhaseTerminateException();
        }
        throw new CustomIllegalArgumentException(errorMessage);
    }

    private static UUID handleNeedMorePromotedProduct(CustomCommunicationData data, OrderRequestDTO orderRequestDTO) {
        LackProductData lackProduct = (LackProductData) data.response();
        Agreement agreement = StoreRequestView
                .isPromotionProductAddable(lackProduct.productName(), lackProduct.quantity());

        if (agreement.isAgreed()) {
            return processUpdatedOrderRequest(OrderRequestDTO.fillLackQuantity(orderRequestDTO));
        }
        return processUpdatedOrderRequest(OrderRequestDTO.notFillLackQuantity(orderRequestDTO));
    }

    private static UUID handleLackPromotedProduct(CustomCommunicationData data, OrderRequestDTO orderRequestDTO) {
        LackProductData lackProduct = (LackProductData) data.response();
        Agreement agreement = StoreRequestView.payWhenLackPromotionProduct(lackProduct.productName(),
                lackProduct.quantity());

        if (agreement.isAgreed()) {
            return processUpdatedOrderRequest(OrderRequestDTO.agreeWithLack(orderRequestDTO));
        }
        return processUpdatedOrderRequest(OrderRequestDTO.disagreeWithLack(orderRequestDTO));
    }

    private static UUID processUpdatedOrderRequest(OrderRequestDTO updatedRequest) {
        CustomCommunicationData data = Telecommunication.requestToBack(updatedRequest);
        if (data.errorMessage().equals(new NoProductInOrderException().getMessage())) {
            throw new PhaseTerminateException();
        }

        OrderResponseDTO orderResponseDTO = (OrderResponseDTO) data.response();
        return orderResponseDTO.uuid();
    }

    private static void pay(UUID uuid) {
        Agreement membershipApplied = StoreRequestView.isMembershipApplied();
        PayRequestDTO payRequestDTO = new PayRequestDTO(uuid, membershipApplied.isAgreed());

        CustomCommunicationData data = Telecommunication.requestToBack(payRequestDTO);
        if (data.code() == CustomCommunicationCode.ERROR) {
            throw new CustomIllegalArgumentException(data.errorMessage());
        }

        OrderResultResponseDTO orderResultResponseDTO = (OrderResultResponseDTO) data.response();
        StoreResponseView.printOrderResult(orderResultResponseDTO);
    }

}
