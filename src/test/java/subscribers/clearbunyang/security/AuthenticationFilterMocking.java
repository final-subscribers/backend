package subscribers.clearbunyang.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.springframework.boot.test.mock.mockito.MockBean;
import subscribers.clearbunyang.global.security.token.JwtTokenProcessor;
import subscribers.clearbunyang.global.security.util.CookieUtil;

/** AuthenticationFilter를 모킹해서 항상 정상적으로 통과하도록 해주는 클래스 */
public class AuthenticationFilterMocking {
    private MockedStatic<CookieUtil> mockedCookieUtil;
    @MockBean private JwtTokenProcessor jwtTokenProcessor;

    @BeforeEach
    public void setUp() throws IOException {
        mockedCookieUtil = mockStatic(CookieUtil.class);

        mockedCookieUtil
                .when(() -> CookieUtil.getCookieNames(any(HttpServletRequest.class)))
                .thenReturn("accessToken");
        doNothing()
                .when(jwtTokenProcessor)
                .processToken(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @AfterEach
    public void tearDown() {
        if (mockedCookieUtil != null) {
            mockedCookieUtil.close();
        }
    }
}
