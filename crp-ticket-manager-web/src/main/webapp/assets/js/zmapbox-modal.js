function (wgt, dataValue) {
	var uuid = wgt.uuid;
	var self = this;
	if (self.after) {
		//resize
		self.after('$refresh', function (ev) {
			if (ev) {
				// Init Map
				mapboxgl.accessToken = 'pk.eyJ1IjoiZWxmcmFzY28iLCJhIjoiY2l1eWlieWFrMDRxdzJ0bW8wNGtsbnlreSJ9.q-IZB3b_RDxmr8FfIw9fdQ';
				var map = new mapboxgl.Map({
					container: uuid, // container id
					style: 'mapbox://styles/mapbox/light-v9', // stylesheet location
					center: [-25.19531249999946, 30.783550158470746], // starting position
					zoom: 1, // starting zoom,
				});
				
				map.on('click', function (e) {
					self.command("$chooseCoordinatesEvent", e.lngLat);
				});
			}		
		});
	}
	
}