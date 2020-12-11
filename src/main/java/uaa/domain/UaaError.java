package uaa.domain;

import uaa.config.ReturnCode;

import java.util.ArrayList;
import java.util.List;

public class UaaError<T> {
    private T value;
    private List<String> errors = new ArrayList<>();
    public void addError(String message){
        errors.add(message);
    }
    public boolean hasError(){
        return errors.size()>0;
    }
    public String getFirstError(){
        if(errors.size()>0)
            return errors.get(0);
        return ReturnCode.DEFAULT_SUCCESS;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

//    public T getValue() {
//        return value;
//    }


    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public static UaaError success(){
        UaaError error = new UaaError();
        return error;
    }
    public static UaaError failure(String message){
        UaaError error = new UaaError();
        error.addError(message);
        return error;
    }
}
