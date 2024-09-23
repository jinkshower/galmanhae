export class SearchModule {
  constructor(mapModule) {
    this.mapModule = mapModule;
    this.initSearch();
    this.initOutsideClickListener();
  }

  initSearch() {
    const searchButton = document.querySelector('#search-container button');
    const keyWordInput = document.querySelector('#keyword');

    // '갈만해?' 버튼 클릭 시 검색 수행
    searchButton.addEventListener('click', () => {
      const keyWord = keyWordInput.value.trim();
      if (keyWord) {
        this.searchPlaces(keyWord);
      }
    });

    // 엔터 키 입력 시 검색 수행
    keyWordInput.addEventListener('keydown', (event) => {
      if (event.key === 'Enter') {
        const keyWord = keyWordInput.value.trim();
        if (keyWord) {
          this.searchPlaces(keyWord);
        }
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
        this.fetchPlaceDetails(
            placeSearchResponse.placeId,
            placeSearchResponse.latitude,
            placeSearchResponse.longitude
        );
        resultsContainer.style.display = 'none'; // 검색 결과를 클릭 시 결과 창을 숨깁니다.
      });

      resultsContainer.appendChild(placeElement);
    });

    resultsContainer.style.display = 'block'; // 새로운 검색 시 결과 창을 다시 표시
  }

  fetchPlaceDetails(placeId, latitude, longitude) {
    axios.get(`/api/places/${placeId}`)
    .then(response => {
      const placeDetails = response.data;
      const position = new kakao.maps.LatLng(latitude, longitude);

      // 지도 중심 이동 및 오버레이 표시
      this.mapModule.setCenter(position);
      this.mapModule.showCustomOverlay(placeDetails, position);
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
