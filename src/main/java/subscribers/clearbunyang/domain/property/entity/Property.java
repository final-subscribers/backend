package subscribers.clearbunyang.domain.property.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.like.entity.Likes;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.global.entity.BaseEntity;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "property")
public class Property extends BaseEntity {

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

    /*에러 때문에 주석 처리
    @Column(nullable = false)
    private String areaCategory;*/

    @Column(nullable = false)
    private String addr_dong;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    @JsonBackReference
    private Admin admin;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Likes> likes;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Area> areas;

    public void setAdminId(Admin adminId) {
        this.admin = adminId;
    }
}
