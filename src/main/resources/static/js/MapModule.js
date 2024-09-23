export class MapModule {
  constructor(mapContainerId, options) {
    this.mapContainer = document.getElementById(mapContainerId);
    this.mapOptions = options;
    this.map = this.initializeMap();
    this.customOverlay = null; // 오버레이 저장용 변수
    this.currentPosition = null; // 사용자 위치 저장용 변수
    this.defaultPosition = new kakao.maps.LatLng(37.5665, 126.9780); // 기본 좌표 (예: 서울 시청)
    this.loadAllPlaces();
    this.initLocationButton(); // 내 위치로 버튼 초기화
  }

  initializeMap() {
    return new kakao.maps.Map(this.mapContainer, this.mapOptions);
  }

  // 내 위치로 버튼 초기화
  initLocationButton() {
    const locationButton = document.getElementById('current-location-button');

    // 버튼 클릭 시 위치 요청
    locationButton.addEventListener('click', () => {
      if (this.currentPosition) {
        this.setCenter(this.currentPosition); // 현재 위치로 지도 중심 이동
      } else {
        this.getUserLocation(); // 위치가 없으면 새로 가져옴
      }
    });

    // 페이지 로드 시 사용자 위치를 가져와 지도 중심 설정
    this.getUserLocation();
  }

  // 사용자 위치 가져오기 (Geolocation API)
  getUserLocation() {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(
          (position) => {
            const lat = position.coords.latitude;
            const lon = position.coords.longitude;
            this.currentPosition = new kakao.maps.LatLng(lat, lon);

            // 지도 중심을 현재 위치로 설정
            this.setCenter(this.currentPosition);
          },
          () => {
            // 실패 시 아무 알림 없이 기본 좌표로 지도 중심 설정
            this.setCenter(this.defaultPosition);
          }
      );
    } else {
      // Geolocation API 지원하지 않을 경우, 기본 좌표로 지도 이동
      this.setCenter(this.defaultPosition);
    }
  }

  setCenter(position) {
    this.map.setCenter(position);
  }

  addMarker(place) {
    const markerPosition = new kakao.maps.LatLng(place.latitude,
        place.longitude);
    const imageSrc = this.getMarkerImageSrc(place.goOutLevel);
    const imageSize = new kakao.maps.Size(45, 51);
    const imageOption = {offset: new kakao.maps.Point(27, 69)};
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
        imageOption);
    const marker = new kakao.maps.Marker({
      position: markerPosition,
      image: markerImage,
    });
    marker.setMap(this.map);

    // 마커 클릭 시 장소 정보 가져오기 및 지도 중심 이동
    kakao.maps.event.addListener(marker, 'click', () => {
      this.setCenter(markerPosition);
      this.fetchPlaceDetails(place.placeId, markerPosition);
    });
  }

  getMarkerImageSrc(goOutLevel) {
    switch (goOutLevel) {
      case 'LOW':
        return '/image/marker_copper.png';
      case 'MEDIUM':
        return '/image/marker_silver.png';
      case 'HIGH':
        return '/image/marker_gold.png';
      default:
        return '/image/marker.png';
    }
  }

  loadAllPlaces() {
    axios.get('/api/places')
    .then((response) => {
      response.data.forEach((place) => {
        this.addMarker(place);
      });
    })
    .catch((error) => {
      console.error('Error fetching places:', error);
    });
  }

  fetchPlaceDetails(placeId, position) {
    axios.get(`/api/places/${placeId}`)
    .then((response) => {
      const placeDetails = response.data;

      // 지도 이동 후 오버레이 표시
      this.setCenter(position); // 지도 이동
      setTimeout(() => {
        this.showCustomOverlay(placeDetails, position); // 오버레이 표시
      }, 200); // 약간의 지연 시간 적용
    })
    .catch((error) => {
      console.error('Error fetching place details:', error);
    });
  }

  showCustomOverlay(placeDetails, position) {
    if (this.customOverlay) {
      this.customOverlay.setMap(null);
    }

    const overlayContent = `
      <div class="overlay-container">
        <div class="overlay-content">
          <h2>${placeDetails.name}</h2>
          <p>${placeDetails.goOutLevelDescription}</p>
          <p><strong>날씨:</strong> ${placeDetails.weatherDescription}</p>
          <p><strong>온도:</strong> ${placeDetails.weatherTemp}도</p>
          <p><strong>강수 확률:</strong> ${placeDetails.weatherRaining}%</p>
          <p><strong>혼잡도:</strong> ${placeDetails.congestionDescription}</p>
          <p><strong>실시간 인구 수:</strong> ${placeDetails.congestionPeople}명</p>
          <button class="close-overlay">닫기</button>
        </div>
      </div>
    `;

    this.customOverlay = new kakao.maps.CustomOverlay({
      position: position,
      content: overlayContent,
      yAnchor: 1,
      map: this.map,
    });

    document.querySelector('.close-overlay').addEventListener('click', () => {
      this.customOverlay.setMap(null);
    });
  }
}
