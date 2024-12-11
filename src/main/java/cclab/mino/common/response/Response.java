package cclab.mino.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Response<T>{
    private int code;
    private String message;
    private T data;

    public enum StatusEnum {
        OK(200, "OK"),
        BAD_REQUEST(400,"BAD_REQUEST"),
        NOT_FOUND(404, "NOT FOUND"),
        INTERNAL_SERVER_ERROR(500,"INTERNAL_SERVER_ERROR");

        int statusCode;
        String message;

        StatusEnum(int statusCode, String message){
            this.statusCode=statusCode;
            this.message=message;
        }
    }
}
