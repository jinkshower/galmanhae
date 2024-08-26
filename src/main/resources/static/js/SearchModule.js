import {PlaceInfoModule} from './PlaceInfoModule.js';

export class SearchModule {
  constructor(mapModule) {
    this.mapModule = mapModule;
    this.initSearch();
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
    resultsContainer.innerHTML = '';

    if (placeSearchResponses.length === 0) {
      resultsContainer.innerHTML = '<p>검색 결과가 없습니다.</p>';
      return;
    }

    placeSearchResponses.forEach(placeSearchResponse => {
      const placeElement = document.createElement('div');
      placeElement.className = 'search-result-item';
      placeElement.innerText = placeSearchResponse.placeName;
      placeElement.addEventListener('click', () => {
        this.fetchPlaceDetails(placeSearchResponse.placeName);
      });
      resultsContainer.appendChild(placeElement);
    });
  }

  fetchPlaceDetails(placeName) {
    axios.get(`/api/places/${placeName}`)
    .then(response => {
      const placeDetails = response.data;
      PlaceInfoModule.displayPlaceInfo(placeDetails);
      this.mapModule.adjustMapSize();
    })
    .catch(error => {
      console.error('Error fetching place details:', error);
    });
  }
}
