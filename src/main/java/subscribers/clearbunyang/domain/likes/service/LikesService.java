package subscribers.clearbunyang.domain.likes.service;


import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subscribers.clearbunyang.domain.auth.entity.Member;
import subscribers.clearbunyang.domain.auth.repository.MemberRepository;
import subscribers.clearbunyang.domain.likes.dto.response.LikesPageResponse;
import subscribers.clearbunyang.domain.likes.dto.response.LikesPropertyResponse;
import subscribers.clearbunyang.domain.likes.repository.LikesRepository;
import subscribers.clearbunyang.domain.property.entity.Property;
import subscribers.clearbunyang.domain.property.entity.enums.KeywordType;
import subscribers.clearbunyang.domain.property.repository.KeywordRepository;
import subscribers.clearbunyang.domain.property.repository.PropertyRepository;
import subscribers.clearbunyang.global.exception.EntityNotFoundException;
import subscribers.clearbunyang.global.exception.InvalidValueException;
import subscribers.clearbunyang.global.exception.errorCode.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final MemberRepository memberRepository;
    private final PropertyRepository propertyRepository;
    private final KeywordRepository keywordRepository;

    @Autowired private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public void toggleLike(Long memberId, Long propertyId) {

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        Property property =
                propertyRepository
                        .findById(propertyId)
                        .orElseThrow(
                                () -> new EntityNotFoundException(ErrorCode.PROPERTY_NOT_FOUND));

        String key = memberId + ":" + propertyId;

        // DB에서 좋아요가 이미 존재하는지 확인
        boolean isLikedInDb = likesRepository.existsByMemberIdAndPropertyId(memberId, propertyId);

        // Redis에서 현재 좋아요 상태를 확인
        Boolean isLikedInRedis = (Boolean) redisTemplate.opsForHash().get("likes", key);

        System.out.println("Current like status for key " + key + ": " + isLikedInRedis);

        if (isLikedInRedis == null) {
            // Redis에 정보가 없을 경우, DB 상태에 따라 초기 값을 설정
            redisTemplate.opsForHash().put("likes", key, !isLikedInDb);
            System.out.println("Set initial like status for key " + key + " to " + !isLikedInDb);
        } else {
            // Redis에 정보가 있는 경우, 반전된 값으로 설정
            redisTemplate.opsForHash().put("likes", key, !isLikedInRedis);
            System.out.println("Toggled like status for key " + key + " to " + !isLikedInRedis);
        }
    }

    @Transactional(readOnly = true)
    public LikesPageResponse getMyFavoriteProperties(
            Long memberId, String status, int page, int size) {
        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));

        LocalDate currentDate = LocalDate.now();
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Property> properties;
        if (status.equals("open")) {
            properties = propertyRepository.findByDateRangeTrue(currentDate);
        } else if (status.equals("closed")) {
            properties = propertyRepository.findByDateRangeFalse(currentDate);
        } else {
            throw new InvalidValueException(ErrorCode.BAD_REQUEST);
        }

        List<Property> filteredProperties =
                properties.stream()
                        .filter(
                                property -> {
                                    String key = memberId + ":" + property.getId();
                                    Boolean isLikedInRedis =
                                            (Boolean) redisTemplate.opsForHash().get("likes", key);

                                    if (Boolean.TRUE.equals(isLikedInRedis)) {
                                        return true;
                                    }

                                    if (Boolean.FALSE.equals(isLikedInRedis)) {
                                        return false;
                                    }

                                    return likesRepository.existsByMemberIdAndPropertyId(
                                            memberId, property.getId());
                                })
                        .collect(Collectors.toList());

        int totalElements = filteredProperties.size();
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + size), totalElements);

        List<Property> pagedProperties;
        if (start >= totalElements) {
            pagedProperties = Collections.emptyList();
        } else {
            pagedProperties = filteredProperties.subList(start, end);
        }

        List<LikesPropertyResponse> likesPropertyResponses =
                pagedProperties.stream()
                        .map(
                                property -> {
                                    List<String> infraKeywords =
                                            keywordRepository
                                                    .findByPropertyIdAndTypeAndIsSearchableTrue(
                                                            property.getId(), KeywordType.INFRA)
                                                    .stream()
                                                    .map(keyword -> keyword.getName().name())
                                                    .collect(Collectors.toList());

                                    List<String> benefitKeywords =
                                            keywordRepository
                                                    .findByPropertyIdAndTypeAndIsSearchableTrue(
                                                            property.getId(), KeywordType.BENEFIT)
                                                    .stream()
                                                    .map(keyword -> keyword.getName().name())
                                                    .collect(Collectors.toList());

                                    return LikesPropertyResponse.fromEntity(
                                            property, infraKeywords, benefitKeywords);
                                })
                        .collect(Collectors.toList());

        Page<LikesPropertyResponse> pageResult =
                new PageImpl<>(likesPropertyResponses, pageRequest, totalElements);

        return LikesPageResponse.fromPage(pageResult, size, page);
    }

    /**
     * 좋아요가 되어있는지 redis에서 확인 후 없으면 디비에서 확인해서 리턴하는 메소드
     *
     * @param memberId
     * @param propertyId
     * @return
     */
    @Transactional(readOnly = true)
    public boolean isLiked(Long memberId, Long propertyId) {
        if (memberId == null) return false;
        String key = memberId + ":" + propertyId;
        Boolean isLikedInRedis = (Boolean) redisTemplate.opsForHash().get("likes", key);

        if (isLikedInRedis != null && isLikedInRedis) {
            return true;
        } else {
            return likesRepository.existsByMemberIdAndPropertyId(memberId, propertyId);
        }
    }
}
