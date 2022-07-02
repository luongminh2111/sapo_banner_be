package vn.sapo.banner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BannerResponse {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String divId;
    private String pageUrl;
    private String code;
    private String title;
    private String imgUrl;
    private String url;
    private String type;
    private Short popUp;
    private Integer width;
    private Integer height;
    private Short modeHide;
    private Integer numberHide;
    private String position;
    private String positionValue;

}
