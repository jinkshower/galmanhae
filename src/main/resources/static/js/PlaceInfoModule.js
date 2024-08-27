export class PlaceInfoModule {
  static displayPlaceInfo(details) {
    this.loadTemplate(() => {
      this.updateField('place-name', details.name);
      this.updateField('place-goOutLevelDescription',
          details.goOutLevelDescription);
      this.updateField('place-weatherDescription', details.weatherDescription);
      this.updateField('place-weatherTemp', details.weatherTemp, '도');
      this.updateField('place-weatherRaining', details.weatherRaining, '%');
      this.updateField('place-congestionDescription',
          details.congestionDescription);
      this.updateField('place-congestionPeople', details.congestionPeople, '명');
    });
  }

  static updateField(elementId, value, unit = '') {
    const element = document.getElementById(elementId);
    if (Number(value) !== -1) {
      element.innerText = value + unit;
    } else {
      element.innerText = ''; // 값이 -1인 경우 빈 값으로 처리
    }
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
