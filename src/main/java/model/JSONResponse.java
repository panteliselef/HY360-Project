package model;

public class JSONResponse {

    private String message;
    private int statusCode;
    private Object result;

    public JSONResponse(String message, int statusCode, Object result){
        this.message = message;
        this.statusCode = statusCode;
        this.result = result;
    }

    public JSONResponse(String message, int statusCode){
        this.message = message;
        this.statusCode = statusCode;
        this.result = null;
    }
}
