package com.elms.api.dto.pushnotificationoptions;

public class AdditionalData<T> {
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AdditionalData{" +
                ", data=" + data +
                '}';
    }
}
