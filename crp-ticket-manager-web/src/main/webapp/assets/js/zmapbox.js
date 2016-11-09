function (wgt, dataValue) {
	var uuid = wgt.uuid;
	//Init Map
	mapboxgl.accessToken = 'pk.eyJ1IjoiZWxmcmFzY28iLCJhIjoiY2l1eWlieWFrMDRxdzJ0bW8wNGtsbnlreSJ9.q-IZB3b_RDxmr8FfIw9fdQ';
	var self = this;
	if (self.after) {
		self.after('$clientUpdate', function (evt) {
			console.log('clien update');
			var map = new mapboxgl.Map({
				container: uuid, // container id
				style: 'mapbox://styles/mapbox/dark-v9', //stylesheet location
				center: [-103.59179687498357, 40.66995747013945], // starting position
				zoom: 3 // starting zoom
			});
			if (evt) {
				map.on('load', function() {
					var data = JSON.parse(evt);
					map.addSource("points", {
	        	        type: "geojson",
	        	        data: data,
	        	        cluster: true,
	        	        clusterMaxZoom: 14, // Max zoom to cluster points on
	        	        clusterRadius: 50 // Radius of each cluster when clustering points (defaults to 50)
	        	    });
	        
		        map.addLayer({
		            "id": "l-points",
		            "type": "symbol",
		            "source": "points",
		            "filter": ["!has", "point_count"],
		            "layout": {
		                "icon-image": "marker-15"
		            }
		        });

		        // Display the earthquake data in three layers, each filtered to a range of
		        // count values. Each range gets a different fill color.
		        var layers = [
		            [150, '#f28cb1'],
		            [20, '#f1f075'],
		            [0, '#51bbd6']
		        ];

		        layers.forEach(function (layer, i) {
		            map.addLayer({
		                "id": "cluster-" + i,
		                "type": "circle",
		                "source": "points",
		                "paint": {
		                    "circle-color": layer[1],
		                    "circle-radius": 18
		                },
		                "filter": i === 0 ?
		                    [">=", "point_count", layer[0]] :
		                    ["all",
		                        [">=", "point_count", layer[0]],
		                        ["<", "point_count", layers[i - 1][0]]]
		            });
		        });

		        // Add a layer for the clusters' count labels
		        map.addLayer({
		            "id": "cluster-count",
		            "type": "symbol",
		            "source": "points",
		            "layout": {
		                "text-field": "{point_count}",
		                "text-font": [
		                    "DIN Offc Pro Medium",
		                    "Arial Unicode MS Bold"
		                ],
		                "text-size": 12
		            }
		        });
        
				});	
				
			}
			
			
			
		});
	}
}