package phone.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ImageDTO {

    private Integer id;
    private String url;
    private String path;
    private long size;
    private String type;
    private String token;
    private LocalDateTime createdDate;
}
