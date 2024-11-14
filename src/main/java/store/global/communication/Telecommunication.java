package store.global.communication;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Telecommunication {

    private static final BlockingQueue<Object> FRONT_TO_BACK = new LinkedBlockingQueue<>();
    private static final BlockingQueue<Object> BACK_TO_FRONT = new LinkedBlockingQueue<>();


    public static void sendSuccessToFront(Object object) {
        sendToFront(CustomCommunicationCode.SUCCESS, "", object);
    }

    public static void sendErrorToFrontWithData(String message, Object response) {
        sendToFront(CustomCommunicationCode.ERROR, message, response);
    }

    private static void sendToFront(CustomCommunicationCode customCode, String message, Object response) {
        try {
            BACK_TO_FRONT.put(new CustomCommunicationData(customCode, message, response));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static CustomCommunicationData receiveFromFront() {
        try {
            return (CustomCommunicationData) FRONT_TO_BACK.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static CustomCommunicationData requestToBack(Object object) {
        try {
            FRONT_TO_BACK.put(new CustomCommunicationData(CustomCommunicationCode.SUCCESS, "", object));
            return receiveFromBack();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static CustomCommunicationData receiveFromBack() {
        try {
            return (CustomCommunicationData) BACK_TO_FRONT.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendTerminateSignalToBack() {
        try {
            FRONT_TO_BACK.put(new CustomCommunicationData(CustomCommunicationCode.TERMINATE, "", null));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
