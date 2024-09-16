package subscribers.clearbunyang.domain.property.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.likes.entity.Likes;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.model.request.PropertySaveRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.PropertyUpdateRequestDTO;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "property")
public class Property extends BaseEntity {

    @Setter
    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String constructor;

    @Column(nullable = false)
    private String areaAddr;

    @Column(nullable = false)
    private String modelHouseAddr;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = true)
    private String contactChannel;

    @Column(nullable = true)
    private String homePage;

    @Setter
    @Column(nullable = false)
    private int likeCount;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SalesType salesType;

    @Column(nullable = false)
    private int totalNumber;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String addrDo;

    @Column(nullable = false)
    private String addrGu;

    @Column(nullable = false)
    private String addrDong;

    @Column(nullable = false)
    private String buildingName;

    @Setter
    @Column(nullable = false)
    private Integer price;

    @Setter
    @Column(nullable = true)
    private Integer discountPrice;

    @Setter
    @Column(nullable = true)
    private Integer discountPercent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonBackReference
    private Admin admin;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Likes> likes;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<File> files;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Area> areas;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Keyword> keywords;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MemberConsultation> memberConsultations;

    public void setAdminId(Admin adminId) {
        this.admin = adminId;
    }

    public static Property toEntity(PropertySaveRequestDTO propertyDTO, Admin admin) {
        return Property.builder()
                .name(propertyDTO.getName())
                .constructor(propertyDTO.getConstructor())
                .areaAddr(propertyDTO.getAreaAddr())
                .modelHouseAddr(propertyDTO.getModelhouseAddr())
                .phoneNumber(propertyDTO.getPhoneNumber())
                .contactChannel(propertyDTO.getContactChannel())
                .homePage(propertyDTO.getHomepage())
                .startDate(propertyDTO.getStartDate())
                .endDate(propertyDTO.getEndDate())
                .propertyType(propertyDTO.getPropertyType())
                .salesType(propertyDTO.getSalesType())
                .totalNumber(propertyDTO.getTotalNumber())
                .companyName(propertyDTO.getCompanyName())
                .addrDo(propertyDTO.getAddrDo())
                .addrGu(propertyDTO.getAddrGu())
                .addrDong(propertyDTO.getAddrDong())
                .buildingName(propertyDTO.getBuildingName())
                .admin(admin)
                .build();
    }

    /**
     * 반정규화 필드에 값을 저장하는 메소드
     *
     * @param imageUrl
     * @param price
     * @param discountPrice
     * @param discountPercent
     */
    public void setDenormalizationFields(
            String imageUrl, Integer price, Integer discountPrice, Integer discountPercent) {
        this.setImageUrl(imageUrl);
        this.setPrice(price);
        this.setDiscountPercent(discountPercent);
        this.setDiscountPrice(discountPrice);
    }

    public void update(PropertyUpdateRequestDTO requestDTO) {
        this.name = requestDTO.getName();
        this.constructor = requestDTO.getConstructor();
        this.areaAddr = requestDTO.getAreaAddr();
        this.modelHouseAddr = requestDTO.getModelhouseAddr();
        this.phoneNumber = requestDTO.getPhoneNumber();
        this.contactChannel = requestDTO.getContactChannel();
        this.homePage = requestDTO.getHomepage();
        this.startDate = requestDTO.getStartDate();
        this.endDate = requestDTO.getEndDate();
        this.propertyType = requestDTO.getPropertyType();
        this.salesType = requestDTO.getSalesType();
        this.totalNumber = requestDTO.getTotalNumber();
        this.companyName = requestDTO.getCompanyName();
        this.addrDo = requestDTO.getAddrDo();
        this.addrGu = requestDTO.getAddrGu();
        this.addrDong = requestDTO.getAddrDong();
        this.buildingName = requestDTO.getBuildingName();
    }
}
