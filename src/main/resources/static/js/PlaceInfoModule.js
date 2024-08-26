export class PlaceInfoModule {
  static displayPlaceInfo(details) {
    this.loadTemplate(() => {
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

  static loadTemplate(callback) {
    axios.get('/placeInfoTemplate.html')
    .then(response => {
      const placeInfoContainer = document.getElementById('placeInfoContainer');
      placeInfoContainer.innerHTML = response.data;
      callback();
    })
    .catch(error => {
      console.error('Error loading template:', error);
    });
  }
}
