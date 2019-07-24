package sanri.utils.excel.exception;

/**
 * 作者: sanri
 * 时间: 2017/08/12 21:48
 * 功能: excel 解析异常
 */
public class ParseException extends  RuntimeException {

    public ParseException(String message,Throwable cause){
        super(message,cause);
    }
}
