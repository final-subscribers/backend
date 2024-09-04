import http from 'k6/http';
import {check, sleep} from 'k6';
import {Counter} from 'k6/metrics';

export const options = {
  vus: 30, // 동시 사용자 수
  duration: '1s', // 테스트 지속 시간
};

// "C:\Project\fcfinal\src\test\java\subscribers\clearbunyang\domain\consultation\service\lockTest.js"

// 테스트 실패 수를 기록할 커스텀 카운터
export const errorCounter = new Counter('errors');

export default function () { //시나리오 실행
  const ip = 'localhost';
  const baseUrl = 'http://' + ip + ':8080/api/admin/consultations'; // 실제 API 엔드포인트로 변경

  const adminConsultationId = 170;
  const consultant = `a-11`;
  //로그인 후 토큰값 획득 후 설정
  const MOCK_ACCESS_TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmdWxsbW9vbjAyMDdAbmF2ZXIuY29tIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzI1MjgwMzU4LCJleHAiOjE3MjY4ODUxNTh9.k3O38D6n21-NmsrGiGXSvA7c3H_rOJPeiBZX-Lz98XM';
  const MOCK_REFRESH_TOKEN = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmdWxsbW9vbjAyMDdAbmF2ZXIuY29tIiwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzI1MjgwMzU4LCJleHAiOjE3Mjc4ODUxNTh9.0dotdXiSt2EhYIzK5ApQdevAtN7wwuREXHGwQaOmmi0';

  const payload = JSON.stringify({
    consultant: "a-10"// 테스트 데이터
  });

  const fullUrl = `${baseUrl}/${adminConsultationId}?consultant=${encodeURIComponent(
      consultant)}`;


  const params = {
    headers: {
      'Content-Type': 'application/json',
      'Cookie': `accessToken=${MOCK_ACCESS_TOKEN}; refreshToken=${MOCK_REFRESH_TOKEN}`,
    },
  };

  // 각 가상 사용자는 한 번의 PUT 요청을 보냄
  const response = http.put(fullUrl, payload, params);

  // 응답 상태 코드가 200인지 확인
  const checkRes = check(response, {
    'is status 200': (r) => r.status === 200,
  });

  // 상태 코드가 200이 아니면 에러 카운터 증가
  if (!checkRes) {
    errorCounter.add(1);
  }

  // 요청 후 짧은 대기 시간
  sleep(1);
  console.log(response.body);
}