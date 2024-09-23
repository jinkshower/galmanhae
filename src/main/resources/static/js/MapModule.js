export class MapModule {
  constructor(mapContainerId, options) {
    this.mapContainer = document.getElementById(mapContainerId);
    this.mapOptions = options;
    this.map = this.initializeMap();
    this.loadAllPlaces();
    this.customOverlay = null; // 오버레이 저장용 변수
  }

  initializeMap() {
    return new kakao.maps.Map(this.mapContainer, this.mapOptions);
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

  setCenter(position) {
    // 지도 이동
    this.map.setCenter(position);
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

  // 마커 클릭 시 오버레이로 정보 표시
  showCustomOverlay(placeDetails, position) {
    // 이전 오버레이가 있다면 제거
    if (this.customOverlay) {
      this.customOverlay.setMap(null);
    }

    // 오버레이 내용 생성
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

    // 오버레이 생성
    this.customOverlay = new kakao.maps.CustomOverlay({
      position: position,
      content: overlayContent,
      yAnchor: 1,
      map: this.map,
    });

    // 닫기 버튼 이벤트 처리
    document.querySelector('.close-overlay').addEventListener('click', () => {
      this.customOverlay.setMap(null);
    });
  }
}
