function (wgt, dataValue) {
	var uuid = wgt.uuid;
	// Init Map
	mapboxgl.accessToken = 'pk.eyJ1IjoiZWxmcmFzY28iLCJhIjoiY2l1eWlieWFrMDRxdzJ0bW8wNGtsbnlreSJ9.q-IZB3b_RDxmr8FfIw9fdQ';
	var map = new mapboxgl.Map({
		container: uuid, // container id
		style: 'mapbox://styles/mapbox/light-v9', // stylesheet location
		center: [-60, -20], // starting position
		zoom: 3, // starting zoom,
	});
	var self = this;
	
	map.on('click', function (e) {
		self.command("$chooseCoordinatesEvent", e.lngLat);
	});
	
}