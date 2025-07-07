package fr.eni.encheres.exceptions;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {

    private static final long serialVersionUID = 1L;

    private List<String> messages;

    public BusinessException(){
        this.messages = new ArrayList<String>();
    }

    public BusinessException(List<String> messages) {
        this.messages = new ArrayList<String>();
    }
    public Iterable<String> getMessages() {
        return this.messages;
    }
    public void add(String message) {
        this.messages.add(message);
    }
    public boolean hasError()
    {
        return !this.messages.isEmpty();
    }

}
