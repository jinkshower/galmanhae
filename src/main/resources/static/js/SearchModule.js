import {PlaceInfoModule} from './PlaceInfoModule.js';

export class SearchModule {
  constructor(mapModule) {
    this.mapModule = mapModule;
    this.initSearch();
    this.initOutsideClickListener();
  }

  initSearch() {
    const searchButton = document.querySelector('#search-container button');
    const keyWordInput = document.querySelector('#keyword');

    searchButton.addEventListener('click', () => {
      const keyWord = keyWordInput.value.trim();
      if (keyWord) {
        this.searchPlaces(keyWord);
      }
    });
  }

  searchPlaces(keyWord) {
    axios.get(`/api/search/${keyWord}`)
    .then(response => {
      this.displaySearchResults(response.data);
    })
    .catch(error => {
      console.error('Error fetching search results:', error);
    });
  }

  displaySearchResults(placeSearchResponses) {
    const resultsContainer = document.getElementById('search-results');
    resultsContainer.innerHTML = ''; // 기존 결과 제거

    if (placeSearchResponses.length === 0) {
      resultsContainer.innerHTML = '<p>검색 결과가 없습니다.</p>';
      return;
    }

    placeSearchResponses.forEach(placeSearchResponse => {
      const placeElement = document.createElement('div');
      placeElement.className = 'search-result-item';
      placeElement.innerText = placeSearchResponse.placeName;

      placeElement.addEventListener('click', () => {
        this.fetchPlaceDetails(placeSearchResponse.placeName,
            placeSearchResponse.latitude, placeSearchResponse.longitude);
        resultsContainer.style.display = 'none'; // 검색 결과를 클릭 시 결과 창을 숨깁니다.
      });

      resultsContainer.appendChild(placeElement);
    });

    resultsContainer.style.display = 'block'; // 새로운 검색 시 결과 창을 다시 표시
  }

  fetchPlaceDetails(placeName, latitude, longitude) {
    axios.get(`/api/places/${placeName}`)
    .then(response => {
      const placeDetails = response.data;
      PlaceInfoModule.displayPlaceInfo(placeDetails);
      this.mapModule.adjustMapSize();

      // 검색 결과 클릭 시 해당 위치로 지도 중심 이동
      this.mapModule.setCenter(new kakao.maps.LatLng(latitude, longitude));
    })
    .catch(error => {
      console.error('Error fetching place details:', error);
    });
  }

  initOutsideClickListener() {
    document.addEventListener('click', (event) => {
      const searchContainer = document.getElementById('search-container');
      const resultsContainer = document.getElementById('search-results');

      // 검색창 외부를 클릭했을 때 검색 결과를 숨깁니다.
      if (!searchContainer.contains(event.target)) {
        resultsContainer.style.display = 'none';
      }
    });
  }
}
