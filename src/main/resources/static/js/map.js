document.addEventListener("DOMContentLoaded", function () {
  var mapContainer = document.getElementById('map'), // 지도를 표시할 div
      mapOption = {
        center: new kakao.maps.LatLng(37.5665, 126.9780), // 지도의 중심좌표 (서울 시청 기준)
        level: 3 // 지도의 확대 레벨
      };

  var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

  // 서버에서 장소 정보를 가져와서 마커를 생성합니다
  axios.get('/api/place-infos')
  .then(function (response) {
    response.data.forEach(function (placeInfo) {
      addMarkerToMap(map, placeInfo);
    });
  })
  .catch(function (error) {
    console.error('Error fetching places:', error);
  });
});

function addMarkerToMap(map, place) {
  var markerPosition = new kakao.maps.LatLng(place.latitude, place.longitude); // 마커가 표시될 위치입니다

  // goOutLevel에 따른 마커 이미지 설정
  var imageSrc;
  switch (place.goOutLevel) {
    case 'LOW':
      imageSrc = '/image/marker_copper.png'; // 동색 마커 이미지 경로
      break;
    case 'MEDIUM':
      imageSrc = '/image/marker_silver.png'; // 은색 마커 이미지 경로
      break;
    case 'HIGH':
      imageSrc = '/image/marker_gold.png'; // 황금색 마커 이미지 경로
      break;
    default:
      imageSrc = '/marker.png'; // 기본 마커 이미지 경로
  }

  var imageSize = new kakao.maps.Size(64, 69); // 마커이미지의 크기입니다
  var imageOption = {offset: new kakao.maps.Point(27, 69)}; // 마커이미지의 옵션입니다

  var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize,
      imageOption); // 마커의 이미지정보를 가지고 있는 마커이미지 생성
  var marker = new kakao.maps.Marker({
    position: markerPosition,
    image: markerImage // 마커이미지 설정
  });

  marker.setMap(map); // 마커가 지도 위에 표시되도록 설정합니다

  // 마커에 마우스 오버 이벤트와 클릭 이벤트 추가
  var infowindow = createInfoWindow(place.name);
  kakao.maps.event.addListener(marker, 'mouseover', function () {
    infowindow.open(map, marker);
  });

  kakao.maps.event.addListener(marker, 'mouseout', function () {
    infowindow.close();
  });

  kakao.maps.event.addListener(marker, 'click', function () {
    fetchPlaceDetails(place.name, map, marker);
  });
}

function createInfoWindow(content) {
  return new kakao.maps.InfoWindow({
    content: '<div style="padding:5px;">' + content + '</div>' // 인포윈도우에 표시할 내용
  });
}

function fetchPlaceDetails(placeName, map, marker) {
  axios.get(`/api/places/${placeName}`)
  .then(function (response) {
    var placeDetails = response.data;
    displayPlaceInfo(placeDetails);
    adjustMapSize();
  })
  .catch(function (error) {
    console.error('Error fetching place details:', error);
  });
}

function loadTemplate(callback) {
  axios.get('/placeInfoTemplate.html')
  .then(function (response) {
    var placeInfoContainer = document.getElementById('placeInfoContainer');
    placeInfoContainer.innerHTML = response.data;
    callback();
  })
  .catch(function (error) {
    console.error('Error loading template:', error);
  });
}

function displayPlaceInfo(details) {
  loadTemplate(function () {
    document.getElementById('place-name').innerText = details.name;
    document.getElementById(
        'place-goOutLevelDescription').innerText = details.goOutLevelDescription;
    document.getElementById(
        'place-weatherDescription').innerText = details.weatherDescription;
    document.getElementById(
        'place-weatherTemp').innerText = details.weatherTemp;
    document.getElementById(
        'place-weatherRaining').innerText = details.weatherRaining;
    document.getElementById(
        'place-congestionDescription').innerText = details.congestionDescription;
    document.getElementById(
        'place-congestionPeople').innerText = details.congestionPeople;
  });
}

function adjustMapSize() {
  var mapContainer = document.getElementById('map');
  var placeInfoContainer = document.getElementById('placeInfoContainer');
  mapContainer.style.width = "80%";
  placeInfoContainer.style.width = "20%";
  placeInfoContainer.style.display = "block";
}
