package space.lambdadriver.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {
    private String role;
    private String content;
}
