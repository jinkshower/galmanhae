document.addEventListener("DOMContentLoaded", function () {
  var mapContainer = document.getElementById('map');
  var placeInfoContainer = document.getElementById('placeInfoContainer');
  var map = initializeMap(mapContainer);

  axios.get('/api/place-infos')
  .then(function (response) {
    response.data.forEach(function (place) {
      addMarkerToMap(map, place);
    });
  })
  .catch(function (error) {
    console.error('Error fetching places:', error);
  });
});

function initializeMap(container) {
  var mapOption = {
    center: new kakao.maps.LatLng(37.5665, 126.9780), // 초기 지도 중심좌표 설정 (서울 시청 기준)
    level: 3 // 확대 레벨
  };
  return new kakao.maps.Map(container, mapOption); // 지도 생성 및 객체 리턴
}

function addMarkerToMap(map, place) {
  var markerPosition = new kakao.maps.LatLng(place.latitude, place.longitude);

  var marker = new kakao.maps.Marker({
    position: markerPosition,
    clickable: true
  });
  marker.setMap(map);

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
    content: '<div style="padding:5px;">' + content + '</div>'
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
