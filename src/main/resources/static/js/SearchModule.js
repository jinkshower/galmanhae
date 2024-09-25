export class SearchModule {
  constructor(mapModule) {
    this.mapModule = mapModule;
    this.initSearch();
    this.initOutsideClickListener();
    this.isComposing = false; // 한글 입력 중 여부
    this.isResultClicked = false; // 자동완성 결과 클릭 여부
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
      if (event.key === 'Enter' && !this.isComposing) {
        const keyWord = keyWordInput.value.trim();
        if (keyWord) {
          this.searchPlaces(keyWord);
        }
      }
    });

    // 한글 입력 중일 때는 자동완성 API 요청을 막음
    keyWordInput.addEventListener('compositionstart', () => {
      this.isComposing = true;
      this.isResultClicked = false; // 입력이 시작되면 결과 클릭 상태를 초기화
    });

    // 한글 입력이 끝나면 자동완성 API 요청
    keyWordInput.addEventListener('compositionend', () => {
      if (!this.isResultClicked) { // 결과를 클릭하지 않았을 때만 동작
        this.isComposing = false;
        const keyWord = keyWordInput.value.trim();
        if (keyWord) {
          this.fetchAutoComplete(keyWord);
        }
      }
      this.isResultClicked = false; // 결과 클릭 후 상태 초기화
    });
  }

  searchPlaces(keyWord) {
    axios.get(`/api/search/${keyWord}`)
    .then(response => {
      this.displayResults(response.data, 'search');
    })
    .catch(error => {
      console.error('Error fetching search results:', error);
    });
  }

  fetchAutoComplete(keyWord) {
    axios.get(`/api/autocomplete/${keyWord}`)
    .then(response => {
      this.displayResults(response.data, 'autocomplete');
    })
    .catch(error => {
      console.error('Error fetching autocomplete results:', error);
    });
  }

  displayResults(results, type) {
    const resultsContainer = document.getElementById('search-results');
    resultsContainer.innerHTML = ''; // 기존 결과 제거

    if (results.length === 0) {
      resultsContainer.innerHTML = '<p>검색 결과가 없습니다.</p>';
      return;
    }

    results.forEach(result => {
      const resultElement = document.createElement('div');
      resultElement.className = 'search-result-item';
      resultElement.innerText = result.placeName;

      // pointerdown 이벤트 사용하여 클릭 처리
      resultElement.addEventListener('pointerdown', (event) => {
        event.preventDefault(); // compositionend 발생을 방지
        this.isResultClicked = true; // 결과를 클릭했을 때 설정
        this.fetchPlaceDetails(result.placeId, result.latitude,
            result.longitude);
        resultsContainer.style.display = 'none'; // 결과 클릭 시 창 숨김
      });

      resultsContainer.appendChild(resultElement);
    });

    resultsContainer.style.display = 'block'; // 결과창 표시
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

      // 검색창 외부 클릭 시 검색 결과 숨김
      if (!searchContainer.contains(event.target)) {
        resultsContainer.style.display = 'none';
      }
    });
  }
}
