package com.tao.mvpbaselibrary.basic.utils;

public class CommandResult {
    int result;
    String message;
    String error;

    public CommandResult(int result, String message, String error) {
        this.result = result;
        this.message = message;
        this.error = error;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "CommandResult{" +
                "result=" + result +
                ", message='" + message + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
