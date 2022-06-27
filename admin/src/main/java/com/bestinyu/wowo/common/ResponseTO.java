package com.bestinyu.wowo.common;

public class ResponseTO<T> {
    private int status;
    private T data;
    private String error;
    private String[] errorArgs;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String[] getErrorArgs() {
        return errorArgs;
    }

    public void setErrorArgs(String[] errorArgs) {
        this.errorArgs = errorArgs;
    }

    public ResponseTO(int status, T data) {
        this.status = status;
        this.data = data;
    }
}
