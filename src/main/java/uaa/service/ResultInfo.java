package uaa.service;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ResultInfo<T> {

    private ResultInfo() {
    }

    private T t;

    private List<ErrorInfo> errorInfoList;

    public T getValue() {
        return t;
    }

    public static ResultInfo instance() {
        ResultInfo resultInfo = new ResultInfo();
        resultInfo.errorInfoList = new ArrayList();
        return resultInfo;
    }

    public static ResultInfo instance(final String errorCode, final String messageFormat, Object... objects) {
        ResultInfo resultInfo = instance();
        final ErrorInfo e = new ErrorInfo(errorCode, messageFormat, objects);
        resultInfo.errorInfoList.add(e);
        return resultInfo;
    }
    public static ResultInfo instance(final String errorCode, final String message) {
        ResultInfo resultInfo = instance();
        resultInfo.errorInfoList.add(new ResultInfo.ErrorInfo(errorCode,message));
        return resultInfo;
    }
    public static ResultInfo instance(final String errorCode) {
        ResultInfo resultInfo = instance();
        resultInfo.errorInfoList.add(new ResultInfo.ErrorInfo(errorCode));
        return resultInfo;
    }

    public static <R> ResultInfo<R> instanceObjectAndErrorCode(final R r, final String errorCode) {
        ResultInfo instance = instance();
        instance.t = r;
        instance.errorInfoList.add(new ResultInfo.ErrorInfo(errorCode));
        return instance;
    }
    public static <R> ResultInfo<R> instance(final R r) {
        ResultInfo instance = instance();
        instance.t = r;
        return instance;
    }
    public static  ResultInfo instance(ResultInfo resultInfo) {
        ResultInfo instance = instance();
        instance.errorInfoList = resultInfo.errorInfoList;
//        instance.t = resultInfo.t;//这里不能这么写，若类型不同，会报异常
        return instance;
    }

    public static ResultInfo instance(final String errorCode, final Exception e) {
        ResultInfo resultInfo = instance();
        resultInfo.errorInfoList.add(new ResultInfo.ErrorInfo(errorCode,e));
        return resultInfo;
    }

    public boolean hasErrors() {
        return errorInfoList.size() > 0;
    }
    public boolean firstErrorHasException() {
        if(!hasErrors()){
            return false;
        }
        if(errorInfoList.get(0).getException() != null){
            return true;
        }
        return false;
    }

    public String getFirstErrorCode() {
        if(this.errorInfoList.size() == 0){
            return null;
        }
        return this.errorInfoList.get(0).getErrorCode();
    }
    public Exception getFirstErrorException() {
        if(this.errorInfoList.size() == 0){
            return null;
        }
        return this.errorInfoList.get(0).getException();
    }

    public String getFirstErrorMessage() {
        if(this.errorInfoList.size() == 0){
            return null;
        }
        return this.errorInfoList.get(0).getMessage();
    }


    private static class ErrorInfo {
        private String message;
        private String errorCode;
        private Exception exception;


        public ErrorInfo( final String errorCode,final String message) {
            this.message = message;
            this.errorCode = errorCode;
        }
        public ErrorInfo( final String errorCode) {
            this.errorCode = errorCode;
        }

        public ErrorInfo(final String errorCode, final String errorMessageFormat, final Object[] errorMessageParam) {
            if(errorMessageParam == null || errorMessageParam.length == 0){
                this.message = errorMessageFormat;
                this.errorCode = errorCode;
            }else{
                String message = MessageFormat.format(errorMessageFormat, errorMessageParam);
                this.message = message;
                this.errorCode = errorCode;
            }
        }

        public ErrorInfo(final String errorCode, final Exception exception) {
            this.errorCode = errorCode;
            this.exception = exception;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(final String errorCode) {
            this.errorCode = errorCode;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(final Exception exception) {
            this.exception = exception;
        }
    }
}
