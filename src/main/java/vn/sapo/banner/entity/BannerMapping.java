package vn.sapo.banner.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "banner_mapping")
public class BannerMapping extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(nullable = false, name = "banner_id")
    private Long bannerId;

    @NotNull
    @Column(nullable = false, name = "section_id")
    private Long sectionId;

    @NotNull
    @Column(columnDefinition = "0", name = "page_id")
    private Long pageId;

    @NotNull
    @Column( columnDefinition = "0", name = "percentage")
    private Integer percentage;

    @NotNull
    @Column( columnDefinition = "0", name = "position")
    private String position;

    @NotNull
    @Column( columnDefinition = "0", name = "position_value")
    private String positionValue;

    public BannerMapping(Long id, Long bannerId, Long sectionId,Long pageId, String position, String positionValue, Integer percentage, String createdBy, String lastModifiedBy) {
        super(id);
        this.bannerId = bannerId;
        this.sectionId = sectionId;
        this.pageId = pageId;
        this.position = position;
        this.positionValue = positionValue;
        this.percentage = percentage;
        this.setCreatedBy(createdBy);
        this.setLastModifiedBy(lastModifiedBy);
    }

    public BannerMapping(Long bannerId, Long sectionId, Long pageId,String position, String positionValue, Integer percentage, String createdBy, String lastModifiedBy) {
        this.bannerId = bannerId;
        this.sectionId = sectionId;
        this.pageId = pageId;
        this.position = position;
        this.positionValue = positionValue;
        this.percentage = percentage;
        this.setCreatedBy(createdBy);
        this.setLastModifiedBy(lastModifiedBy);
    }
}
