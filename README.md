# 👴 어르신을 부탁해

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/Language-Java-007396?style=flat-square&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Backend-Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black"/>
  <img src="https://img.shields.io/badge/Map-Kakao%20Maps%20SDK-FFCD00?style=flat-square&logo=kakao&logoColor=black"/>
  <img src="https://img.shields.io/badge/동국대-캡스톤디자인%20총장상(1등)-gold?style=flat-square"/>
</p>

> 노인 디지털 소외 해소를 위한 **보호자↔어르신 1:1 안심 케어 안드로이드 앱**

고령화 사회에서 보호자가 어르신의 안전을 쉽고 직관적으로 관리할 수 있도록 설계된 케어 플랫폼입니다.
어르신은 간단한 UI로 알림을 받고, 보호자는 실시간 위치·활동량·알림 전송 기능으로 원격에서도 든든하게 케어할 수 있습니다.

---

## 🏆 수상 & 성과

| 구분 | 내용 |
|------|------|
| 🥇 **수상** | 동국대학교 캡스톤디자인 **총장상 (1등)** |
| 📄 **특허** | 노인용 1대1 모바일 관리 시스템 및 데이터 처리 방법 (출원) |
| 📝 **논문** | One-to-one mobile care system for the elderly (학술대회 발표) |

---

## 📸 화면 미리보기

<table>
  <tr>
    <td align="center"><img src="https://github.com/CSID-DGU/2023-2-SCS4031-01-introverted_elephant/blob/main/image/Tutorial_1.png" width="200"/><br/><b>알림 전송 (보호자)</b></td>
    <td align="center"><img src="https://github.com/CSID-DGU/2023-2-SCS4031-01-introverted_elephant/blob/main/image/Tutorial_2.png" width="200"/><br/><b>알림 수신 (어르신)</b></td>
    <td align="center"><img src="https://github.com/CSID-DGU/2023-2-SCS4031-01-introverted_elephant/blob/main/image/Tutorial_6.png" width="200"/><br/><b>실시간 위치 확인</b></td>
    <td align="center"><img src="https://github.com/CSID-DGU/2023-2-SCS4031-01-introverted_elephant/blob/main/image/Tutorial_5.png" width="200"/><br/><b>걸음 수 활동 통계</b></td>
  </tr>
</table>

---

## 🚀 핵심 기능

### 🔐 회원가입 & 로그인
- 이메일·비밀번호 기반 회원가입
- Google 소셜 로그인 지원 (Firebase Auth)
- 로그인 후 **보호자 / 어르신** 역할 선택

### 🤝 1:1 매칭 시스템
- 보호자와 어르신을 1대1로 연결하는 고유 매칭 구조
- 매칭된 쌍을 기준으로 모든 기능(알림·위치·활동)이 동작

### 🔔 이원화 알림 시스템
- **일반 알림**: 보호자가 어르신에게 안부 메시지 전송 → 상태바 팝업으로 수신
- **경고 알림**: 긴급 안부 확인 알림 → 커스텀 알림음 + 강한 진동 패턴 + 전용 화면으로 이동
- Firestore 실시간 리스너(`addSnapshotListener`)로 메시지 즉시 감지
- **ForegroundService** (START_STICKY)로 앱이 종료되어도 알림 수신 유지
- 어르신이 알림을 확인하면 보호자에게 읽음 알림 자동 발송

### 📍 실시간 위치 서비스
- Kakao Maps SDK로 어르신의 현재 위치 및 이동 경로 실시간 확인
- **안전 구역 설정**: 지도에서 주소 검색 후 반경(m) 직접 입력 → 원형 안전 구역 표시
- 안전 구역 이탈 시 보호자에게 즉시 알림 발송
- Kakao REST API (주소 검색, Geocoding)로 위치 데이터 처리
- **Haversine 공식** 자체 구현으로 GPS 좌표 간 정확한 거리 계산
- 주변 병원·복지시설 등 의료 인프라 정보 표시

### 🦶 걸음 수 모니터링
- `ACCELEROMETER` 센서 기반 걸음 감지 알고리즘 자체 구현 (magnitude 임계값 적용)
- **ForegroundService**로 백그라운드에서도 걸음 수 상시 측정
- 일별 걸음 수 Firestore 저장, **7일 막대 그래프** (MPAndroidChart) 시각화
- 보호자가 어르신의 **일일 걸음 목표** 원격 설정 가능
- 목표 달성 여부 및 **전체 사용자 랭킹** 실시간 집계
- 주간 평균 걸음 수 통계 제공

### 🌐 커뮤니티 & 복지 정보
- 어르신 간 소통을 위한 커뮤니티 게시판
- 주변 복지기관 및 지원 서비스 정보 연결

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| **언어** | Java |
| **UI** | Android XML Layout, Fragment |
| **Firebase** | Auth, Firestore (실시간 리스너) |
| **지도** | Kakao Maps SDK, Kakao REST API (Retrofit2 + Gson) |
| **센서** | Android ACCELEROMETER (걸음 감지) |
| **백그라운드** | ForegroundService (START_STICKY), NotificationChannel |
| **알림** | NotificationCompat (이중 채널), 커스텀 알림음, 진동 패턴 |
| **차트** | MPAndroidChart (BarChart) |
| **위치 계산** | Haversine 공식 자체 구현 |

---

## 🏗️ 앱 구조

```
어르신을 부탁해
├── 보호자 앱 (Master)
│   ├── 알림 전송 (일반 / 경고)
│   ├── 실시간 위치 확인 & 안전 구역 설정
│   ├── 걸음 수 모니터링 & 목표 설정
│   └── 커뮤니티
│
├── 어르신 앱 (Old)
│   ├── 알림 수신 (일반 / 경고 팝업)
│   ├── 만보기 (걸음 수 표시)
│   └── 간소화된 UI
│
└── 공통
    ├── 로그인 / 회원가입 (Firebase Auth)
    ├── 1:1 매칭 시스템
    └── ForegroundService (알림 수신 & 걸음 수 측정)
```

---

## 🔥 기술적 고민 & 해결

### 1. 알림 중요도에 따른 이중 채널 구성
- **문제**: 일반 안부 알림과 긴급 경고 알림을 같은 방식으로 처리하면 사용자가 위급 상황을 인지하지 못할 수 있음
- **해결**: `IMPORTANCE_DEFAULT` 채널(일반)과 `IMPORTANCE_HIGH` 채널(경고)로 분리, 경고 채널에는 커스텀 알림음·강한 진동 패턴 적용 및 전용 화면으로 연결

### 2. 앱 종료 후에도 알림 수신 유지
- **문제**: 어르신이 앱을 종료한 상태에서도 보호자의 알림을 실시간으로 받아야 함
- **해결**: `ForegroundService` + `START_STICKY`로 서비스가 시스템에 의해 종료되어도 자동 재시작, Firestore `addSnapshotListener`로 메시지 실시간 감지

### 3. 걸음 수 센서 오감지 문제
- **문제**: `ACCELEROMETER` 센서가 걷지 않아도 진동·흔들림 등에 반응해 걸음 수가 부정확하게 증가
- **해결**: 가속도 벡터의 magnitude(`√(x²+y²+z²)`) 계산 후 임계값(15.0f) 초과 시에만 걸음으로 인정, 0.2초 쿨다운 타임으로 연속 오감지 방지

### 4. GPS 기반 안전 구역 이탈 감지
- **문제**: 위도·경도 좌표만으로는 실제 거리를 정확히 계산하기 어려움
- **해결**: 지구 곡률을 반영하는 **Haversine 공식** 자체 구현으로 두 GPS 좌표 간 실제 거리(m) 계산, 설정된 반경과 비교하여 이탈 판단

---

## 📁 프로젝트 구조

```
app/src/main/java/com/oldcare/capstonedesign/
├── start/             # 앱 시작 (역할 선택)
├── login/             # 로그인, 회원가입
├── Fragment/          # 어르신 앱 메인 Fragment (커뮤니티, 활동, 설정)
├── old_man/           # 어르신 전용 화면 (메인, 경고, 설정, 만보기)
├── location/          # 지도, 위치, 안전 구역, Kakao API
├── service/
│   ├── FirestoreNotificationService.java   # 실시간 알림 수신 서비스
│   ├── FirestoreNotificationService2.java  # 보호자용 알림 서비스
│   └── StepCounterService.java             # 걸음 수 측정 백그라운드 서비스
├── StepCounterActivity.java   # 걸음 수 통계 & 랭킹
├── MainActivity.java
└── NotificationActivity.java
```

---

## 👥 팀 구성 & 역할

| 이름 | 학과 | 역할 |
|------|------|------|
| **이유빈** (팀장) | 식품생명공학과 | 로그인·회원가입, 알림 시스템, 걸음 수 활동 기능 |
| 김재홍 | 경제학과 | 위치 확인 기능, 복지 정보 연결 |
| 김상화 | 산업시스템공학과 | 기획, 디자인, 만보기 UI |

---

## 💻 설치 및 실행

```bash
git clone https://github.com/a0100019/Elderly-Care-System-Android.git
```

1. **Android Studio**에서 프로젝트 열기
2. Firebase 프로젝트 생성 후 `google-services.json`을 `app/` 폴더에 추가
3. `local.properties`에 Kakao Maps API 키 설정
4. **Run ▶️**
