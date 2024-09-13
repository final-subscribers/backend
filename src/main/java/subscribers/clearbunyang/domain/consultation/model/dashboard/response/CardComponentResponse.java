package subscribers.clearbunyang.domain.consultation.model.dashboard.response;


import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CardComponentResponse {
    CardTodayStatusResponse today;
    CardWeekProgressResponse thisWeekProgress;
    CardWeekProgressResponse lastWeekProgress;
    List<Integer> totalNumberByWeek;
    CardCountDescResponse highestConsultation;
    CardCountDescResponse lowestConsultation;
}
