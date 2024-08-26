import {MapModule} from './MapModule.js';
import {SearchModule} from './SearchModule.js';

document.addEventListener("DOMContentLoaded", function () {
  const mapModule = new MapModule('map', {
    center: new kakao.maps.LatLng(37.5665, 126.9780),
    level: 4
  });

  new SearchModule(mapModule);
});
