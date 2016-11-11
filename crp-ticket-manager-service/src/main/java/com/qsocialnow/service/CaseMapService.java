package com.qsocialnow.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qsocialnow.common.model.cases.CaseLocationView;
import com.qsocialnow.common.model.event.GeoSocialEventLocationMethod;
import com.qsocialnow.common.model.pagination.PageRequest;
import com.qsocialnow.persistence.CaseRepository;

@Service
public class CaseMapService {

	private static final Logger log = LoggerFactory
			.getLogger(CaseMapService.class);

	@Autowired
	private CaseRepository repository;

	public String getGeoJson(Integer pageNumber, Integer pageSize) {
		try {
			List<CaseLocationView> locations = repository
					.findCasesLocations(new PageRequest(pageNumber, pageSize, "openDate"));
			if (!CollectionUtils.isEmpty(locations)) {
				JsonObject geoJson = new JsonObject();
				geoJson.addProperty("type", "FeatureCollection");
				JsonArray points = new JsonArray();
				for (CaseLocationView location : locations) {
					if (GeoSocialEventLocationMethod.GEO_LOCATION
							.equals(location.getLocationMethod())) {
						JsonObject point = new JsonObject();
						point.addProperty("type", "Feature");
						JsonObject geometry = new JsonObject();
						geometry.addProperty("type", "Point");
						JsonArray coordinates = new JsonArray();
						coordinates.add(location.getLocation().getLongitud());
						coordinates.add(location.getLocation().getLatitud());
						geometry.add("coordinates", coordinates);
						point.add("geometry", geometry);
						JsonObject properties = new JsonObject();
						properties.addProperty("caseId", location.getId());
						point.add("properties", properties);
						points.add(point);
					}
				}
				geoJson.add("features", points);
				return geoJson.toString();
			}
			
		} catch (Exception e) {
			log.error("error", e);
		}
		return null;
	}

}
