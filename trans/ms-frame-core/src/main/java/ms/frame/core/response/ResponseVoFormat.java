package ms.frame.core.response;

/**
 * 需要实现的接口，返回报文头信息
 */
public interface ResponseVoFormat {

    ResponseHead responseHeadFormat(String errorCode, String errorMsg);
}
