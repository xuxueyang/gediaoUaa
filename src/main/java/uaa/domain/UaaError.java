package uaa.domain;

import core.ReturnCode;

import java.util.ArrayList;
import java.util.List;

public class UaaError {
    private Object value;
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
    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
