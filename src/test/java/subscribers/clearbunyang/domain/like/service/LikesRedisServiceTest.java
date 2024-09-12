package subscribers.clearbunyang.domain.like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import subscribers.clearbunyang.domain.likes.entity.Likes;
import subscribers.clearbunyang.domain.likes.model.response.LikesPropertyResponse;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.likes.service.LikesService;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.PropertyType;
import subscribers.clearbunyang.domain.property.entity.enums.SalesType;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.domain.user.entity.Admin;
import subscribers.clearbunyang.domain.user.entity.Member;
import subscribers.clearbunyang.domain.user.entity.enums.AdminState;
import subscribers.clearbunyang.domain.user.entity.enums.UserRole;
import subscribers.clearbunyang.domain.user.repository.AdminRepository;
import subscribers.clearbunyang.domain.user.repository.MemberRepository;
import subscribers.clearbunyang.global.config.BatchConfig;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;
import subscribers.clearbunyang.global.exception.notFound.EntityNotFoundException;
import subscribers.clearbunyang.global.model.PagedDto;

@SpringBootTest
@Testcontainers
class LikesRedisServiceTest {
    // 테스트 돌리기 전에 BatchConfig의 @Async 비동기 어노테이션 주석처리하고 돌려야 함

    @Autowired private LikesService likesService;

    @Autowired private RedisTemplate<String, Object> redisTemplate;

    @Autowired private BatchConfig batchConfig;

    @Autowired private LikesRepository likesRepository;

    @Autowired private MemberRepository memberRepository;

    @Autowired private PropertyRepository propertyRepository;

    @Autowired private AdminRepository adminRepository;

    @Autowired private JdbcTemplate jdbcTemplate;

    @Container
    private static GenericContainer<?> redisContainer =
            new GenericContainer<>(DockerImageName.parse("redis:6.2.5")).withExposedPorts(6379);

    @BeforeEach
    void setUp() {
        String redisUrl = redisContainer.getHost() + ":" + redisContainer.getMappedPort(6379);
        System.setProperty("redis.host", redisContainer.getHost());
        System.setProperty("redis.port", String.valueOf(redisContainer.getMappedPort(6379)));

        redisTemplate.getConnectionFactory().getConnection().flushAll();

        jdbcTemplate.execute("DELETE FROM likes");
        jdbcTemplate.execute("DELETE FROM property");
        jdbcTemplate.execute("DELETE FROM member");
        jdbcTemplate.execute("DELETE FROM admin");

        // Id값 자동 증가때문에 DB 데이터 밒 ID값 1부터 초기화
        jdbcTemplate.execute("ALTER TABLE member AUTO_INCREMENT = 1");
        jdbcTemplate.execute("ALTER TABLE property AUTO_INCREMENT = 1");
        jdbcTemplate.execute("ALTER TABLE likes AUTO_INCREMENT = 1");
        jdbcTemplate.execute("ALTER TABLE admin AUTO_INCREMENT = 1");

        Member member =
                Member.builder()
                        .address("Gwacheon Byeolyang")
                        .email("epoint0101@gmail.com")
                        .name("Test")
                        .password("password123!")
                        .phoneNumber("01040378485")
                        .role(UserRole.MEMBER)
                        .build();
        memberRepository.save(member);

        Admin admin =
                Admin.builder()
                        .name("Test2")
                        .email("epoint1126@gmail.com")
                        .password("password123!")
                        .companyName("testCompany")
                        .phoneNumber("01000000000")
                        .registrationNumber(0L)
                        .address("Sample Address")
                        .business("Test Business")
                        .status(AdminState.ACCEPTED)
                        .role(UserRole.ADMIN)
                        .build();
        adminRepository.save(admin);

        Property property =
                Property.builder()
                        .name("propertyTest")
                        .constructor("testtest")
                        .areaAddr("testaddr")
                        .modelHouseAddr("testtestaddr")
                        .phoneNumber("01000000000")
                        .contactChannel("test.com")
                        .homePage("test.comcom")
                        .likeCount(0)
                        .startDate(LocalDate.of(2024, 9, 1))
                        .endDate(LocalDate.of(2024, 9, 2))
                        .propertyType(PropertyType.APARTMENT)
                        .salesType(SalesType.PRIVATE_SALE)
                        .totalNumber(100)
                        .companyName("testcompany")
                        .addrDo("TestDo")
                        .addrGu("TeatGu")
                        .addrDong("TestDong")
                        .buildingName("testBuilding")
                        .admin(admin)
                        .build();
        propertyRepository.save(property);

        Property property2 =
                Property.builder()
                        .name("propertyTest")
                        .constructor("testtest")
                        .areaAddr("testaddr")
                        .modelHouseAddr("testtestaddr")
                        .phoneNumber("01000000000")
                        .contactChannel("test.com")
                        .homePage("test.comcom")
                        .likeCount(0)
                        .startDate(LocalDate.of(2024, 9, 1))
                        .endDate(LocalDate.of(2024, 9, 30))
                        .propertyType(PropertyType.APARTMENT)
                        .salesType(SalesType.PRIVATE_SALE)
                        .totalNumber(100)
                        .companyName("testcompany")
                        .addrDo("TestDo")
                        .addrGu("TeatGu")
                        .addrDong("TestDong")
                        .buildingName("testBuilding")
                        .admin(admin)
                        .build();
        propertyRepository.save(property2);
    }

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    @Transactional
    void testToggleLike_addLikeToRedis() {
        // 좋아요 토글
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(1L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String key = member.getId() + ":" + property.getId();

        likesService.toggleLike(member.getId(), property.getId());

        Boolean isLikedInRedis = (Boolean) redisTemplate.opsForHash().get("likes", key);
        assertThat(isLikedInRedis).isTrue();

        likesService.toggleLike(member.getId(), property.getId());
        isLikedInRedis = (Boolean) redisTemplate.opsForHash().get("likes", key);
        assertThat(isLikedInRedis).isFalse();
    }

    @Test
    @Transactional
    void testToggleLike_initialLikeStatusFromDb() {
        // redis 초기 좋아요 상태 db에 따라 결정
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(1L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String key = member.getId() + ":" + property.getId();

        Likes like = Likes.builder().member(member).property(property).build();
        likesRepository.save(like);

        likesService.toggleLike(member.getId(), property.getId());

        Boolean isLikedInRedis = (Boolean) redisTemplate.opsForHash().get("likes", key);
        assertThat(isLikedInRedis).isFalse();
    }

    @Test
    @Transactional
    void testSyncLikesToDatabase_addLike() {
        // db에 좋아요가 없고 redis에 키값이 true일 때 좋아요 추가
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(1L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String key = member.getId() + ":" + property.getId();

        redisTemplate.opsForHash().put("likes", key, true);

        batchConfig.syncLikesToDatabase();

        Likes like = likesRepository.findByMemberAndProperty(member, property).orElse(null);
        assertThat(like).isNotNull();

        Property updatedProperty = propertyRepository.findById(property.getId()).orElse(null);
        assertThat(updatedProperty).isNotNull();
        assertEquals(1, updatedProperty.getLikeCount());
    }

    @Test
    @Transactional
    void testSyncLikesToDatabase_noChange() {
        // db에 좋아요 없고 redis에 키값이 false일 때 변화없음
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(1L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String key = member.getId() + ":" + property.getId();

        redisTemplate.opsForHash().put("likes", key, false);

        batchConfig.syncLikesToDatabase();

        assertThat(likesRepository.findByMemberAndProperty(member, property)).isEmpty();
    }

    @Test
    @Transactional
    void testSyncLikesToDatabase_noActionWhenRedisTrue() {
        // db에 좋아요 있고 redis에 키값이 true일 때 변화없음
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(1L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String key = member.getId() + ":" + property.getId();

        property.setLikeCount(1);
        propertyRepository.save(property);

        Likes like = Likes.builder().member(member).property(property).build();
        likesRepository.save(like);

        redisTemplate.opsForHash().put("likes", key, true);

        batchConfig.syncLikesToDatabase();

        Likes existingLike = likesRepository.findByMemberAndProperty(member, property).orElse(null);
        assertThat(existingLike).isNotNull();

        Property updatedProperty = propertyRepository.findById(property.getId()).orElse(null);
        assertThat(updatedProperty).isNotNull();
        assertEquals(1, updatedProperty.getLikeCount());
    }

    @Test
    @Transactional
    void testSyncLikesToDatabase_removeLike() {
        // db에 좋아요가 있고 redis에는 키값이 false일 때 좋아요 삭제
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(1L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String key = member.getId() + ":" + property.getId();

        property.setLikeCount(1);
        propertyRepository.save(property);

        Likes like = Likes.builder().member(member).property(property).build();
        likesRepository.save(like);

        redisTemplate.opsForHash().put("likes", key, false);

        batchConfig.syncLikesToDatabase();

        Likes deletedLike = likesRepository.findByMemberAndProperty(member, property).orElse(null);
        assertThat(deletedLike).isNull();

        Property updatedProperty = propertyRepository.findById(property.getId()).orElse(null);
        assertThat(updatedProperty).isNotNull();
        assertEquals(0, updatedProperty.getLikeCount());
    }

    @Test
    void getMyFavoriteProperties_shouldReturnPropertiesFromRedisAndDatabase() {
        // Redis에는 데이터가 true고 데이터베이스에는 좋아요 데이터가 있는 경우 조회
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(2L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String status = "open";
        int page = 0;
        int size = 10;

        String redisKey = member.getId() + ":2";
        redisTemplate.opsForHash().put("likes", redisKey, true);

        likesRepository.save(Likes.builder().member(member).property(property).build());

        PagedDto<LikesPropertyResponse> response =
                likesService.getMyFavoriteProperties(member.getId(), status, page, size);

        assertThat(response.getContents()).isNotEmpty();
        assertThat(response.getContents().size()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);
    }

    @Test
    void getMyFavoriteProperties_shouldReturnOnlyRedisLikesWhenNoDBLikes() {
        // Redis에는 데이터가 true고 데이터베이스에는 좋아요 데이터가 없는 경우 조회
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(2L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String status = "open";
        int page = 0;
        int size = 10;

        String redisKey = member.getId() + ":2";
        redisTemplate.opsForHash().put("likes", redisKey, true);

        PagedDto<LikesPropertyResponse> response =
                likesService.getMyFavoriteProperties(member.getId(), status, page, size);

        assertThat(response.getContents()).isNotEmpty();
        assertThat(response.getContents().size()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);
    }

    @Test
    void getMyFavoriteProperties_shouldReturnOnlyDBLikesWhenNoRedisLikes() {
        // Redis에는 데이터가 false고 데이터베이스에는 좋아요 데이터가 있는 경우 조회
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(2L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String status = "open";
        int page = 0;
        int size = 10;

        String redisKey = member.getId() + ":2";
        redisTemplate.opsForHash().put("likes", redisKey, false);

        likesRepository.save(Likes.builder().member(member).property(property).build());

        PagedDto<LikesPropertyResponse> response =
                likesService.getMyFavoriteProperties(member.getId(), status, page, size);

        assertThat(response.getContents()).isEmpty();
    }

    @Test
    void getMyFavoriteProperties_shouldReturnEmptyWhenNoLikesInRedisOrDB() {
        // Redis에는 데이터가 false고 데이터베이스에는 좋아요 데이터가 없는 경우 조회
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(2L)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String status = "open";
        int page = 0;
        int size = 10;

        String redisKey = member.getId() + ":2";
        redisTemplate.opsForHash().put("likes", redisKey, false);

        PagedDto<LikesPropertyResponse> response =
                likesService.getMyFavoriteProperties(member.getId(), status, page, size);

        assertThat(response.getContents()).isEmpty();
    }

    @Test
    void getMyFavoriteProperties_shouldHandlePaginationCorrectly() {
        // 페이징이 올바르게 동작하는지 확인
        Member member =
                memberRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Admin admin =
                adminRepository
                        .findById(1L)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        // propertyId: 3,4,5 저장
        for (int i = 1; i <= 3; i++) {
            Property property =
                    Property.builder()
                            .name("propertyTest" + i)
                            .constructor("testtest" + i)
                            .areaAddr("testaddr" + i)
                            .modelHouseAddr("testtestaddr" + i)
                            .phoneNumber("0100000000" + i)
                            .contactChannel("test" + i + ".com")
                            .homePage("test" + i + ".comcom")
                            .likeCount(0)
                            .startDate(LocalDate.of(2024, 9, 1))
                            .endDate(LocalDate.of(2024, 9, 30))
                            .propertyType(PropertyType.APARTMENT)
                            .salesType(SalesType.PRIVATE_SALE)
                            .totalNumber(100)
                            .companyName("testcompany" + i)
                            .addrDo("TestDo" + i)
                            .addrGu("TestGu" + i)
                            .addrDong("TestDong" + i)
                            .buildingName("testBuilding" + i)
                            .admin(admin)
                            .build();
            propertyRepository.save(property);
        }

        // propertyId: 2(BeforeEach에서 저장), 3,4,5(위에 for문에서 저장)가 open 상태
        for (int i = 2; i <= 5; i++) {
            String redisKey = member.getId() + ":" + i;
            redisTemplate.opsForHash().put("likes", redisKey, true);
            System.out.println("Redis에 저장된 key: " + redisKey);
        }

        String status = "open";
        int page = 0;
        int size = 10;

        PagedDto<LikesPropertyResponse> response =
                likesService.getMyFavoriteProperties(member.getId(), status, page, size);

        assertThat(response.getContents()).isNotEmpty();
        assertThat(response.getContents().size()).isEqualTo(4); // PropertyId: 2,3,4,5
        assertThat(response.getTotalPages()).isEqualTo(1);
    }
}
