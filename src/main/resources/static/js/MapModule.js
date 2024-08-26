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
    kakao.maps.event.addListener(marker, 'mouseover', function () {
      infowindow.open(this.map, marker);
    });

    kakao.maps.event.addListener(marker, 'mouseout', function () {
      infowindow.close();
    });

    kakao.maps.event.addListener(marker, 'click', function () {
      fetchPlaceDetails(place.name);
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
}
