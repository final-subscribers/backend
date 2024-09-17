package subscribers.clearbunyang.domain.property.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

import jakarta.persistence.EntityManager;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.consultation.entity.MemberConsultation;
import subscribers.clearbunyang.domain.file.entity.File;
import subscribers.clearbunyang.domain.file.entity.enums.FileType;
import subscribers.clearbunyang.domain.file.repository.FileRepository;
import subscribers.clearbunyang.domain.likes.entity.Likes;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.entity.Area;
import subscribers.clearbunyang.domain.property.entity.Keyword;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.model.request.KeywordRequestDTO;
import subscribers.clearbunyang.domain.property.model.request.PropertyUpdateRequestDTO;
import subscribers.clearbunyang.domain.property.repository.AreaRepository;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;
import subscribers.clearbunyang.testfixtures.*;

@SpringBootTest
@DisplayName("PropertyService-통합 테스트1")
@Transactional
public class PropertyServiceIntegrationTest1 {

    @Autowired private PropertyService propertyService;

    @Autowired private AreaRepository areaRepository;

    @Autowired private KeywordRepository keywordRepository;
    @Autowired private FileRepository fileRepository;
    @Autowired private LikesRepository likesRepository;
    @Autowired private PropertyRepository propertyRepository;
    @Autowired private AdminRepository adminRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private EntityManager entityManager;

    private Property savedProperty;
    private Admin savedAdmin;

    @BeforeEach
    void setUp() {
        this.savedAdmin = adminRepository.save(AdminRegisterFixture.createDefault());

        this.savedProperty =
                propertyService.saveProperty(
                        PropertySaveRequestDTOFixture.createDefault(), savedAdmin.getId());
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("물건 저장 테스트")
    void saveProperty() {
        List<Keyword> keywords = keywordRepository.findByPropertyId(savedProperty.getId());
        List<Area> areas = areaRepository.findByPropertyId(savedProperty.getId());
        List<KeywordRequestDTO> searchEnabledKeywords =
                PropertySaveRequestDTOFixture.createDefault().getKeywords().stream()
                        .filter(keyword -> keyword.getSearchEnabled())
                        .toList();

        for (int i = 0; i < searchEnabledKeywords.size(); i++) {
            assertThat(keywords.get(i).getName()).isEqualTo(searchEnabledKeywords.get(i).getName());
            assertThat(keywords.get(i).getType()).isEqualTo(searchEnabledKeywords.get(i).getType());
        }
        assertThat(keywords.get(searchEnabledKeywords.size()).getName()).isNull();
        assertThat(keywords.get(searchEnabledKeywords.size()).getType()).isNull();
        assertThat(areas).hasSize(PropertySaveRequestDTOFixture.createDefault().getAreas().size());
        String imageUrl =
                PropertySaveRequestDTOFixture.createDefault().getFiles().stream()
                        .filter(file -> file.getType() == FileType.PROPERTY_IMAGE)
                        .findFirst()
                        .get()
                        .getUrl();
        assertThat(savedProperty.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("상담 신청 테스트-비회원일 경우")
    void saveConsultation1() {
        MemberConsultation memberConsultation =
                propertyService.saveConsultation(
                        savedProperty.getId(),
                        MemberConsultationRequestDTOFixture.createDefault(),
                        null);

        assertThat(memberConsultation.getAdminConsultation()).isNotNull();
        assertThat(memberConsultation.getMember()).isNull();
        assertThat(memberConsultation.getPreferredAt())
                .isEqualTo(MemberConsultationRequestDTOFixture.createDefault().getPreferredAt());
        assertThat(memberConsultation.getAdminConsultation()).isNotNull();
    }

    @Test
    @DisplayName("상담 신청 테스트-회원일 경우")
    void saveConsultation2() {
        Member savedMember = memberRepository.save(MemberRegisterFixture.createDefault());
        MemberConsultation memberConsultation =
                propertyService.saveConsultation(
                        savedProperty.getId(),
                        MemberConsultationRequestDTOFixture.createDefault(),
                        savedMember.getId());

        assertThat(memberConsultation.getAdminConsultation()).isNotNull();
        assertThat(memberConsultation.getMember()).isNotNull();
        assertThat(memberConsultation.getPreferredAt())
                .isEqualTo(MemberConsultationRequestDTOFixture.createDefault().getPreferredAt());
        assertThat(memberConsultation.getAdminConsultation()).isNotNull();
    }

    @Test
    @DisplayName("매물 삭제 테스트")
    void deleteProperty1() {
        Long propertyId = savedProperty.getId();
        propertyService.deleteProperty(propertyId, savedAdmin.getId());
        entityManager.flush(); // 트랜잭션 내에서 변경 사항을 강제로 데이터베이스에 반영
        entityManager.clear();

        List<Area> areas = areaRepository.findByPropertyId(propertyId);
        List<File> files = fileRepository.findByPropertyId(propertyId);
        List<Likes> likes = likesRepository.findByPropertyId(propertyId);
        List<Keyword> keywords = keywordRepository.findByPropertyId(propertyId);

        assertThrows(
                EntityNotFoundException.class,
                () -> {
                    propertyRepository.findPropertyById(propertyId);
                });
        assertThat(areas.size()).isEqualTo(0);
        assertThat(files.size()).isEqualTo(0);
        assertThat(likes.size()).isEqualTo(0);
        assertThat(keywords.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("매물 삭제 테스트-삭제할 property 못찾음")
    void deleteProperty2() {
        assertThrows(
                EntityNotFoundException.class,
                () -> {
                    propertyService.deleteProperty(100000L, 100000L);
                });
    }

    @Test
    @DisplayName("매물 수정 테스트")
    void updateProperty() {
        PropertyUpdateRequestDTO updateRequestDTO =
                PropertyUpdateRequestDTOFixture.createDefault2();
        propertyService.updateProperty(savedProperty.getId(), updateRequestDTO, savedAdmin.getId());
        entityManager.flush();
        entityManager.clear();

        Property property = propertyRepository.findPropertyById(savedProperty.getId());
        assertThat(property.getImageUrl()).isEqualTo(updateRequestDTO.getPropertyImage().getUrl());
        assertThat(property.getAddrDo()).isEqualTo(updateRequestDTO.getAddrDo());
    }
}
