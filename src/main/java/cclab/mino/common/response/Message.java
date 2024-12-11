package cclab.mino.common.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Message {
    private Response.StatusEnum status;
    private String message;
    private Object data;

    /*public Message(){
        this.status = Response.StatusEnum.BAD_REQUEST;
        this.data=null;
        this.message=null;
    }*/
}
