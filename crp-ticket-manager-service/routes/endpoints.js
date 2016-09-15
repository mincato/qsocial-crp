var express = require('express');
var app = require('../app');
var router = express.Router();

var javaInit = require('../javaInit');
var java = javaInit.getJavaInstance();

var GsonBuilder = java.import('com.google.gson.GsonBuilder');
var DateClazz = java.findClassSync('java.util.Date');
var JSONDateDeserialize = java.import('com.qsocialnow.rest.json.JSONDateDeserialize');
var JSONDateSerialize = java.import('com.qsocialnow.rest.json.JSONDateSerialize');

var JavaApp = java.import('com.qsocialnow.App');
var javaContext = JavaApp.getInstanceSync();

function prettyJSON(obj) {
    console.log(JSON.stringify(obj, null, 8));
}

router.get('/cases', function (req, res) {

  function asyncResponse(err,responseCases) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

    if(responseCases !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(responseCases));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }

  var caseService = javaContext.getBeanSync("caseService");
  var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
  var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
  caseService.findAll(pageNumber, pageSize, asyncResponse);

});

router.get('/cases/:id', function (req, res) {

	  function asyncResponse(err,responseDomain) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(responseDomain !== null) {
	      try {
	        res.set('Content-Type','application/json');
	        res.send(gson.toJsonSync(responseDomain));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var caseId = req.params.id;
	  var caseService = javaContext.getBeanSync("caseService");
	  var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
	  var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
	  caseService.findOne(caseId,pageNumber, pageSize,asyncResponse);
	  
	});

router.post('/cases/:id/action', function (req, res) {

	function asyncResponse(err,responseCase) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.getMessageSync()); return; }

		if(responseCase !== null) {
			try {
				res.set('Content-Type', 'application/json');
				res.send(gson.toJsonSync(responseCase));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}

	}

	var caseService = javaContext.getBeanSync("caseService");
	var caseId = req.params.id;
	  
	prettyJSON(req.body);

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.cases.ActionRequest');
	var actionRequest = gson.fromJsonSync(JSON.stringify(req.body), clazz);
	  
	caseService.executeAction(caseId, actionRequest, asyncResponse);

});

router.get('/cases/:id/availableResolutions', function (req, res) {

	function asyncResponse(err,responseResolutions) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.getMessageSync()); return; }

		if(responseResolutions !== null) {
			try {
				res.set('Content-Type', 'application/json');
				res.send(gson.toJsonSync(responseResolutions));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}

	}

	var caseService = javaContext.getBeanSync("caseService");
	var caseId = req.params.id;
	  
	caseService.getAvailableResolutions(caseId, asyncResponse);

});

router.get('/cases/:id/registries', function (req, res) {

	  function asyncResponse(err,responseDomain) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(responseDomain !== null) {
	      try {
	        res.set('Content-Type','application/json');
	        res.send(gson.toJsonSync(responseDomain));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var caseId = req.params.id;
	  var actionRegistryService = javaContext.getBeanSync("actionRegistryService");
	  var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
	  var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
	  actionRegistryService.findAll(caseId,pageNumber, pageSize,asyncResponse);
});



router.get('/domains', function (req, res) {

	  function asyncResponse(err,responseDomains) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(responseDomains !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(responseDomains));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var domainService = javaContext.getBeanSync("domainService");
	  var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
	  var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
	  var name = req.query.name ? req.query.name : null;
	  
	  if(name === null) {
		  domainService.findAll(pageNumber, pageSize, asyncResponse);
	  }else{
		  domainService.findAllByName(pageNumber, pageSize,name,asyncResponse);
	  }

});

router.post('/domains', function (req, res) {

  function asyncResponse(err,responseDomain) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

    if(responseDomain !== null) {
      try {
        res.set('Content-Type','application/json');
        res.send(gson.toJsonSync(responseDomain));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }

  prettyJSON(req.body);

  var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
  var clazz = java.findClassSync('com.qsocialnow.common.model.config.Domain');
  var domain = gson.fromJsonSync(JSON.stringify(req.body), clazz);

  var domainService = javaContext.getBeanSync("domainService");
  domainService.save(domain, asyncResponse);

});

router.get('/domains/:id', function (req, res) {

  function asyncResponse(err,responseDomain) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

    if(responseDomain !== null) {
      try {
        res.set('Content-Type','application/json');
        res.send(gson.toJsonSync(responseDomain));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }

  var domainId = req.params.id;

  var domainService = javaContext.getBeanSync("domainService");
  domainService.findOne(domainId, asyncResponse);

});

router.put('/domains/:id/trigger', function (req, res) {

  function asyncResponse(err,responseTrigger) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

    if(responseTrigger !== null) {
      try {
        res.set('Content-Type','application/json');
        res.send(gson.toJsonSync(responseTrigger));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }

  var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
  var clazz = java.findClassSync('com.qsocialnow.common.model.config.Trigger');
  
  var trigger = gson.fromJsonSync(JSON.stringify(req.body), clazz);  
  
  var domainId = req.params.id;

  var triggerService = javaContext.getBeanSync("triggerService");
  triggerService.createTrigger(domainId, trigger, asyncResponse);

});

router.get('/domains/:id/trigger', function (req, res) {

	  function asyncResponse(err,responseTriggers) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(responseTriggers !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(responseTriggers));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
	  var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
	  var name = req.query.name ? req.query.name : null;
	  
	  triggerService.findAll(domainId, pageNumber, pageSize, name, asyncResponse);
});

router.put('/domains/:id/resolutions', function (req, res) {

	function asyncResponse(err,responseResolution) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(responseResolution !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(responseResolution));
	      } catch(ex) {
	    	  	res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	    	res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }
	}

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.config.Resolution');
	
	var resolution = gson.fromJsonSync(JSON.stringify(req.body), clazz);
	  
	var domainId = req.params.id;

	var resolutionService = javaContext.getBeanSync("resolutionService");
	resolutionService.createResolution(domainId, resolution, asyncResponse);
	  
});

router.put('/domains/:id/resolutions/:resolutionId', function (req, res) {

	function asyncResponse(err,responseResolution) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(responseResolution !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(responseResolution));
	      } catch(ex) {
	    	  	res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	    	res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }
	}

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.config.Resolution');
	
	var resolution = gson.fromJsonSync(JSON.stringify(req.body), clazz);
	  
	var domainId = req.params.id;
	var resolutionId = req.params.resolutionId;
	var resolutionService = javaContext.getBeanSync("resolutionService");
	resolutionService.update(domainId, resolutionId, resolution, asyncResponse);
	  
});

router.delete('/domains/:id/resolutions/:resolutionId', function (req, res) {

	function asyncResponse(err,responseResolution) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(responseResolution !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(responseResolution));
	      } catch(ex) {
	    	  	res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	    	res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }
	}
	  
	var domainId = req.params.id;
	var resolutionId = req.params.resolutionId;
	var resolutionService = javaContext.getBeanSync("resolutionService");
	resolutionService.delete(domainId, resolutionId, asyncResponse);
	  
});



router.put('/domains/:id', function (req, res) {
  
  function asyncResponse(err,responseDomain) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

    if(responseDomain !== null) {
      try {
        res.set('Content-Type','application/json');
        res.send(gson.toJsonSync(responseDomain));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }

  var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
  var clazz = java.findClassSync('com.qsocialnow.common.model.config.Domain');
  var domain = gson.fromJsonSync(JSON.stringify(req.body), clazz);
  var domainId = req.params.id;

  var domainService = javaContext.getBeanSync("domainService");
  domainService.update(domainId, domain, asyncResponse);

});

module.exports = router;
