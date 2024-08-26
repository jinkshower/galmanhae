import {PlaceInfoModule} from './PlaceInfoModule.js';

export class MapModule {
  constructor(mapContainerId, options) {
    this.mapContainer = document.getElementById(mapContainerId);
    this.mapOptions = options;
    this.map = this.initializeMap();
    this.loadAllPlaces();
  }

  initializeMap() {
    return new kakao.maps.Map(this.mapContainer, this.mapOptions);
  }

  addMarker(place) {
    const markerPosition = new kakao.maps.LatLng(place.latitude,
        place.longitude);
    const imageSrc = this.getMarkerImageSrc(place.goOutLevel);
    const imageSize = new kakao.maps.Size(64, 69);
    const imageOption = {offset: new kakao.maps.Point(27, 69)};
    const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
        imageOption);
    const marker = new kakao.maps.Marker({
      position: markerPosition,
      image: markerImage
    });
    marker.setMap(this.map);

    const infowindow = this.createInfoWindow(place.name);

    // 마우스 오버 시 인포윈도우 열기
    kakao.maps.event.addListener(marker, 'mouseover', () => {
      infowindow.open(this.map, marker);
    });

    // 마우스 아웃 시 인포윈도우 닫기
    kakao.maps.event.addListener(marker, 'mouseout', () => {
      infowindow.close();
    });

    // 마커 클릭 시 장소 정보 가져오기 및 지도 중심 이동
    kakao.maps.event.addListener(marker, 'click', () => {
      this.setCenter(markerPosition);
      this.fetchPlaceDetails(place.name);
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
        return '/marker.png';
    }
  }

  createInfoWindow(content) {
    return new kakao.maps.InfoWindow({
      content: '<div style="padding:5px;">' + content + '</div>'
    });
  }

  loadAllPlaces() {
    axios.get('/api/place-infos')
    .then(response => {
      response.data.forEach(placeInfo => {
        this.addMarker(placeInfo);
      });
    })
    .catch(error => {
      console.error('Error fetching places:', error);
    });
  }

  adjustMapSize() {
    this.mapContainer.style.width = "80%";
    const placeInfoContainer = document.getElementById('placeInfoContainer');
    placeInfoContainer.style.width = "20%";
    placeInfoContainer.style.display = "block";
  }

  setCenter(position) {
    this.map.setCenter(position);
  }

  fetchPlaceDetails(placeName) {
    axios.get(`/api/places/${placeName}`)
    .then(response => {
      const placeDetails = response.data;
      PlaceInfoModule.displayPlaceInfo(placeDetails);
      this.adjustMapSize();
    })
    .catch(error => {
      console.error('Error fetching place details:', error);
    });
  }
}
