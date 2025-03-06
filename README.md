# 👨‍👩‍👦‍👦 갈만해?

> 지금 서울, 이 장소 갈만해? 👉 ~~https://galmanhae.site/~~ (비용상 문제로 현재 사이트는 운영중지)

<img src="https://github.com/user-attachments/assets/9bf6f420-1973-4029-85c3-aeb7cdddca43" alt="image" width="400"/>

## 📖 Description

서울에서 외출을 하려는데, 그 장소의 날씨는 좋은 지, 사람은 너무 많지 않을 지 고민한 적 없으신가요?

갈만해?는 서울의 다양한 장소의 실시간 날씨, 인구데이터를 통합해 해당 장소가 외출하기 적절한 지 판단해드립니다!

가고 싶은 장소를 입력하고 해당 장소의 갈만한 지수를 확인해보세요!

## ⭐ Main Feature

### 서울의 인구밀집 지역들의 외출 적합도를 지도로 볼 수 있음

- Kakao Map API를 활용하여 서울의 인구밀집 지역을 지도로 확인 가능

### 장소 상세

- 날씨, 인구밀집도, 외출 적합도 확인 가능

### 장소 검색

- 원하는 장소를 검색하여 외출 적합도 확인 가능

## 💻 Technical Issues

[Wiki](https://github.com/jinkshower/galmanhae/wiki)에서 프로젝트를 진행하며 겪은 기술적 이슈들에 대한 고민을 확인할 수 있습니다.

## 🔧 Stack

- **Language**: Java, JavaScript
- **Library & Framework** : Spring MVC, Thymeleaf
- **Database** : MySQL
- **ORM** : Spring Data JPA

## ⚒ CI/CD

- github actions를 활용해서 지속적 통합 및 배포
- `feature` 브랜치에서 `main`으로로 Pull Request를 보내면, CI가 동작된다.
- CI가 동작되고 'main'에 Merge가 되면, 운영 리소스에 배포된다.

## 👨‍👩‍👧‍👦 Developer

* **임현태** ([jinkshower](https://github.com/jinkshower))

프로젝트
규칙은 [CODE_OF_CONDUCT](https://github.com/jinkshower/galmanhae/blob/main/.github/CODE_OF_CONDUCT.md)
에서 확인 할 수 있습니다.

## Project Structure

<img src="https://github.com/user-attachments/assets/9dcfc3c7-75ef-4e08-a529-78d7dae2d2f8" alt="Project Structure" width="600"/>

<details>
  <summary>Click</summary>

```markdown
├── GalmanhaeApplication.java
├── data
│ ├── domain
│ │ ├── Congestion.java
│ │ ├── Place.java
│ │ ├── Weather.java
│ │ └── WeatherAndCongestion.java
│ ├── entity
│ │ ├── BaseEntity.java
│ │ ├── CongestionEntity.java
│ │ ├── PlaceEntity.java
│ │ ├── TimeAndPlace.java
│ │ └── WeatherEntity.java
│ ├── exception
│ │ └── DataException.java
│ └── repository
│ ├── CongestionRepository.java
│ ├── PlaceRepository.java
│ ├── WeatherRepository.java
│ ├── congestion
│ │ ├── CongestionJpaRepository.java
│ │ └── CongestionRepositoryImpl.java
│ ├── place
│ │ ├── PlaceJpaRepository.java
│ │ └── PlaceRepositoryImpl.java
│ └── weather
│ ├── WeatherJpaRepository.java
│ └── WeatherRepositoryImpl.java
├── dataprocess
│ ├── DataProcessInitializer.java
│ ├── PlaceDataProcessor.java
│ ├── WeatherCongestionDataProcessor.java
│ ├── application
│ │ ├── CongestionFetchService.java
│ │ ├── DataQueryService.java
│ │ ├── PlaceFetchService.java
│ │ ├── WeatherBaseTime.java
│ │ └── WeatherFetchService.java
│ ├── client
│ │ ├── CongestionClient.java
│ │ ├── CustomErrorDecoder.java
│ │ ├── FeignConfig.java
│ │ ├── PlaceClient.java
│ │ ├── WeatherClient.java
│ │ └── response
│ │ ├── CongestionResponse.java
│ │ └── WeatherResponse.java
│ ├── exception
│ │ ├── DataProcessCheckedException.java
│ │ └── DataProcessUncheckedException.java
│ └── util
│ ├── DataParser.java
│ └── LambertCoordinateConverter.java
├── dataquery
│ ├── application
│ │ └── PlaceQueryService.java
│ ├── domain
│ │ ├── CongestionLevel.java
│ │ ├── GoOutLevel.java
│ │ ├── PlaceDetails.java
│ │ └── WeatherLevel.java
│ ├── presentation
│ │ ├── PlaceRestController.java
│ │ ├── PlaceSearchRestController.java
│ │ └── PlaceWebController.java
│ └── response
│ ├── PlaceDetailResponse.java
│ ├── PlaceResponse.java
│ └── PlaceSearchResponse.java
└── global
└── exception
├── ErrorResponse.java
└── GlobalExceptionHandler.java
</details>
```
