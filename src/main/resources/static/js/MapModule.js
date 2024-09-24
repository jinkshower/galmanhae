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

    // 창 크기 변경 시 지도 리사이즈 처리
    window.addEventListener("resize", () => {
      this.map.relayout(); // 지도를 리사이즈함
      if (this.currentPosition) {
        this.setCenter(this.currentPosition); // 현재 위치로 다시 설정
      } else {
        this.setCenter(this.defaultPosition); // 기본 위치로 설정
      }
    });
  }

  initializeMap() {
    const map = new kakao.maps.Map(this.mapContainer, this.mapOptions);

    // 모바일에서도 줌과 드래그가 가능하게 설정
    map.setDraggable(true);
    map.setZoomable(true);

    return map;
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
    const markerImage = this.getMarkerImageSrc(place.goOutLevel);

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
    // 화면 너비의 5%로 설정하되, 최소 30px, 최대 50px로 제한
    const baseSize = Math.min(Math.max(window.innerWidth * 0.05, 30), 50);
    const imageSize = new kakao.maps.Size(baseSize, baseSize * 1.1); // 가로세로 비율 적용

    switch (goOutLevel) {
      case 'LOW':
        return new kakao.maps.MarkerImage('/image/marker_copper.png',
            imageSize);
      case 'MEDIUM':
        return new kakao.maps.MarkerImage('/image/marker_silver.png',
            imageSize);
      case 'HIGH':
        return new kakao.maps.MarkerImage('/image/marker_gold.png', imageSize);
      default:
        return new kakao.maps.MarkerImage('/image/marker.png', imageSize);
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

      // 오버레이를 먼저 표시하고 나서 지도 중심 이동을 처리
      this.showCustomOverlay(placeDetails, position);
      this.setCenter(position); // 오버레이 표시 후 지도 이동
    })
    .catch((error) => {
      console.error('Error fetching place details:', error);
    });
  }

  showCustomOverlay(placeDetails, position) {
    if (this.customOverlay) {
      this.customOverlay.setMap(null);
    }

    const overlayContent = document.createElement('div');
    overlayContent.classList.add('overlay-container');
    overlayContent.innerHTML = `
      <div class="overlay-content">
        <h2>${placeDetails.name}</h2>
        <p>${placeDetails.goOutLevelDescription}</p>
        <p><strong>날씨:</strong> ${placeDetails.weatherDescription}</p>
        <p><strong>온도:</strong> ${placeDetails.weatherTemp}도</p>
        <p><strong>강수 확률:</strong> ${placeDetails.weatherRaining}%</p>
        <p><strong>혼잡도:</strong> ${placeDetails.congestionDescription}</p>
        <p><strong>실시간 인구 수:</strong> ${placeDetails.congestionPeople}명</p>
        <button class="close-overlay">X</button>
      </div>
    `;

    this.customOverlay = new kakao.maps.CustomOverlay({
      position: position,
      content: overlayContent,
      yAnchor: 1,
      map: this.map,
    });

    // 오버레이 닫기 이벤트 설정
    overlayContent.querySelector('.close-overlay').addEventListener('click',
        () => {
          this.customOverlay.setMap(null);
        });
  }
}
