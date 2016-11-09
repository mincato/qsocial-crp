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

router.get('/cases/report', function (req, res) {
	  function asyncResponse(err,response) {
	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	    	  res.writeHead(200, {
	    	        'Content-Type': 'application/vnd.ms-excel',
	    	        'Content-Length': response.length
	    	    });
	    	    res.end(new Buffer(response, 'binary'))
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  var domainId = req.query.domainId?req.query.domainId :null;
	  
	  var triggerId = req.query.triggerId?req.query.triggerId :null;
	  var segmentId = req.query.segmentId?req.query.segmentId :null;
	  
	  var userSelected = req.query.userSelected?req.query.userSelected :null;
	  var userName = req.query.userName?req.query.userName :null;
	  var subject = req.query.subject?req.query.subject :null;
	  var title = req.query.title?req.query.title :null;
	  var description = req.query.description?req.query.description :null;
	  var pendingResponse = req.query.pendingResponse?req.query.pendingResponse :null;
	  var status = req.query.status?req.query.status :null;
	  
	  var fromOpenDate = req.query.fromOpenDate?req.query.fromOpenDate :null;
	  var toOpenDate = req.query.toOpenDate?req.query.toOpenDate :null;
	  
	  var language = req.query.language?req.query.language :null;

	  var caseService = javaContext.getBeanSync("caseReportService");
	  caseService.getReport(domainId,triggerId,segmentId,subject,title,description,pendingResponse,status,fromOpenDate,toOpenDate,userName, userSelected, language, asyncResponse);
	  
});

router.get('/cases/results/report', function (req, res) {
	  function asyncResponse(err,response) {
	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	    	  res.writeHead(200, {
	    	        'Content-Type': 'application/vnd.ms-excel',
	    	        'Content-Length': response.length
	    	    });
	    	    res.end(new Buffer(response, 'binary'))
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  var domainId = req.query.domainId?req.query.domainId :null;
	  var language = req.query.language?req.query.language :null;
	  var caseService = javaContext.getBeanSync("caseReportService");
	  caseService.getCasesByResolutionReport(domainId,language,asyncResponse);
	  
});

router.get('/cases/results', function (req, res) {
	function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	    	res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  var domainId = req.query.domainId?req.query.domainId :null;
	  var caseService = javaContext.getBeanSync("caseResultsService");
	  caseService.getResults(domainId,asyncResponse);
});

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
  var sortField = req.query.sortField ? req.query.sortField : null;
  var sortOrder = req.query.sortOrder?req.query.sortOrder :null;
  var domainId = req.query.domainId?req.query.domainId :null;
  
  var triggerId = req.query.triggerId?req.query.triggerId :null;
  var segmentId = req.query.segmentId?req.query.segmentId :null;
  
  var userName = req.query.userName?req.query.userName :null;
  var userSelected = req.query.userSelected?req.query.userSelected :null;
  var subject = req.query.subject?req.query.subject :null;
  var title = req.query.title?req.query.title :null;
  var pendingResponse = req.query.pendingResponse?req.query.pendingResponse :null;
  var status = req.query.status?req.query.status :null;
  var priority = req.query.priority?req.query.priority :null;
  var fromOpenDate = req.query.fromOpenDate?req.query.fromOpenDate :null;
  var toOpenDate = req.query.toOpenDate?req.query.toOpenDate :null;
  
  var caseCategoryId = req.query.caseCategory?req.query.caseCategory :null;
  var subjectCategoryId = req.query.subjectCategory?req.query.subjectCategory :null;

  caseService.findAll(pageNumber, pageSize,sortField,sortOrder,domainId,triggerId,segmentId,subject,title,pendingResponse,priority,status,fromOpenDate,toOpenDate,userName,userSelected,caseCategoryId,subjectCategoryId,asyncResponse);

});

router.post('/cases', function (req, res) {

	  function asyncResponse(err,responseCases) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(responseCases !== null) {
	      try {
	        res.set('Content-Type','application/json');
	        res.send(gson.toJsonSync(responseCases));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  prettyJSON(req.body);

	  var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	  var clazz = java.findClassSync('com.qsocialnow.common.model.cases.Case');
	  var newCase = gson.fromJsonSync(JSON.stringify(req.body), clazz);

	  var caseService = javaContext.getBeanSync("caseService");
	  caseService.save(newCase, asyncResponse);

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
	  caseService.findOne(caseId,asyncResponse);
	  
	});

router.post('/cases/:id/action', function (req, res) {

	function asyncResponse(err,responseCase) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

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

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

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
	  var text = req.query.text ? req.query.text : null;
	  var action = req.query.action ? req.query.action : null;
	  var user = req.query.user ? req.query.user : null;
	  var fromDate = req.query.fromDate ? req.query.fromDate : null;
	  var toDate = req.query.toDate ? req.query.toDate : null;
	  
	  if(text === null && action === null && user === null && fromDate === null && toDate === null) {
		  actionRegistryService.findAll(caseId,pageNumber, pageSize,asyncResponse);
	  }else{
		  actionRegistryService.findAllBy(caseId,text,action,user,fromDate,toDate,pageNumber, pageSize,asyncResponse);
	  }
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
	  var userName = req.query.userName ? req.query.userName : null;
	  
	  if(name === null) {
		  if(userName === null) {
			  domainService.findAll(pageNumber, pageSize, asyncResponse);
		  }
		  else{
			  domainService.findAllByUserName(userName,asyncResponse);
		  }
	  }else{
		  domainService.findAllByName(pageNumber, pageSize,name,asyncResponse);
	  }
});

router.get('/domainsActive', function (req, res) {

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
	  domainService.findAllActive(asyncResponse);
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

router.get('/domainsWithActiveResolutions/:id', function (req, res) {

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
  domainService.findOneWithActiveResolutions(domainId, asyncResponse);

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

  prettyJSON(req.body);
  
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
	  var status = req.query.status ? req.query.status : null;
	  var fromDate = req.query.fromDate ? req.query.fromDate : null;
	  var toDate = req.query.toDate ? req.query.toDate : null;
	  
	  triggerService.findAll(domainId, pageNumber, pageSize, name, status, fromDate, toDate, asyncResponse);
});

router.get('/domains/:id/triggerActive', function (req, res) {

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
	  
	  triggerService.findAllActive(domainId, asyncResponse);
});

router.get('/domains/:id/trigger/:triggerId', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;
	  
	  triggerService.findOne(domainId, triggerId, asyncResponse);
});

router.get('/domains/:id/triggerWithActiveSegments/:triggerId', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;
	  
	  triggerService.findOneWithActiveSegments(domainId, triggerId, asyncResponse);
});

router.get('/domains/:id/trigger/:triggerId/caseCategories', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;
	  
	  triggerService.findCaseCategories(domainId, triggerId, asyncResponse);
});

router.get('/domains/:id/trigger/:triggerId/caseCategoriesActive', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;
	  
	  triggerService.findCaseCategoriesActive(domainId, triggerId, asyncResponse);
});

router.get('/domains/:id/trigger/:triggerId/subjectCategories', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;
	  
	  triggerService.findSubjectCategories(domainId, triggerId, asyncResponse);
});

router.get('/domains/:id/trigger/:triggerId/segments', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;
	  
	  triggerService.findSegments(domainId, triggerId, asyncResponse);
});

router.get('/domains/:id/trigger/:triggerId/segmentsActive', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;
	  
	  triggerService.findActiveSegments(domainId, triggerId, asyncResponse);
});

router.get('/domains/:id/trigger/:triggerId/segment/:segmentId', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var triggerService = javaContext.getBeanSync("triggerService");
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;
	  var segmentId = req.params.segmentId;
	  
	  triggerService.findSegment(domainId, triggerId, segmentId, asyncResponse);
});

router.put('/domains/:id/trigger/:triggerId', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type','application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  
	  prettyJSON(req.body);

	  var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	  var clazz = java.findClassSync('com.qsocialnow.common.model.config.Trigger');
	  
	  var trigger = gson.fromJsonSync(JSON.stringify(req.body), clazz);  
	  
	  var domainId = req.params.id;
	  var triggerId = req.params.triggerId;

	  var triggerService = javaContext.getBeanSync("triggerService");
	  triggerService.update(domainId, triggerId, trigger, asyncResponse);

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

router.get('/domains/:id/resolutions/:resolutionId', function (req, res) {

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
	resolutionService.findOne(domainId, resolutionId, asyncResponse);
	  
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

router.get('/userResolver', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  
	  var userResolverService = javaContext.getBeanSync("userResolverService");
	  userResolverService.findAll(asyncResponse);
});

router.get('/userResolverActive', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  
	  var userResolverService = javaContext.getBeanSync("userResolverService");
	  userResolverService.findAllActive(asyncResponse);
});

router.get('/userResolver/list', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var userResolverService = javaContext.getBeanSync("userResolverService");
	  var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
	  var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
	  var identifier = req.query.identifier ? req.query.identifier : null;
	  
	  userResolverService.findAll(pageNumber, pageSize, identifier, asyncResponse);
});

router.post('/userResolver', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}
	}

	prettyJSON(req.body);

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.config.UserResolver');
	var userResolver = gson.fromJsonSync(JSON.stringify(req.body), clazz);

	var userResolverService = javaContext.getBeanSync("userResolverService");
	userResolverService.createUserResolver(userResolver, asyncResponse);

});

router.get('/userResolver/:id', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	    	try {
	    		res.set('Content-Type','application/json');
	    		res.send(gson.toJsonSync(response));
	    	} catch(ex) {
	    		res.status(500).json(ex.cause.getMessageSync());
	    	}
	    } else {
	    	res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }
	}

	var userResolverId = req.params.id;

	var userResolverService = javaContext.getBeanSync("userResolverService");
	userResolverService.findOne(userResolverId, asyncResponse);

});

router.delete('/userResolver/:id', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
	      } catch(ex) {
	    	  	res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	    	res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }
	}
	  
	var userResolverId = req.params.id;
	var userResolverService = javaContext.getBeanSync("userResolverService");
	userResolverService.delete(userResolverId, asyncResponse);
	  
});

router.put('/userResolver/:id', function (req, res) {
	  
	function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	    	try {
	    		res.set('Content-Type','application/json');
	    		res.send(gson.toJsonSync(response));
	    	} catch(ex) {
	    		res.status(500).json(ex.cause.getMessageSync());
	    	}
	    } else {
	    	res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	}
	
	prettyJSON(req.body);

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.config.UserResolver');
	var userResolver = gson.fromJsonSync(JSON.stringify(req.body), clazz);
	var userResolverId = req.params.id;

	var userResolverService = javaContext.getBeanSync("userResolverService");
	userResolverService.update(userResolverId, userResolver, asyncResponse);

});

router.post('/teams', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}
	}

	prettyJSON(req.body);

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.config.Team');
	var team = gson.fromJsonSync(JSON.stringify(req.body), clazz);

	var teamService = javaContext.getBeanSync("teamService");
	teamService.createTeam(team, asyncResponse);

});

router.get('/teams/list', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var teamService = javaContext.getBeanSync("teamService");
	  var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
	  var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
	  var name = req.query.name ? req.query.name : null;
	  
	  teamService.findAll(pageNumber, pageSize, name, asyncResponse);
	  
});

router.get('/teams', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var teamService = javaContext.getBeanSync("teamService");
	  
	  teamService.findAll(asyncResponse);
});

router.get('/teamsActive', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var teamService = javaContext.getBeanSync("teamService");
	  
	  teamService.findAllActive(asyncResponse);
});

router.get('/teams/:id', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var teamService = javaContext.getBeanSync("teamService");
	  var teamId = req.params.id;
	  
	  teamService.findOne(teamId, asyncResponse);
});

router.get('/teams/:id/userResolvers', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var teamService = javaContext.getBeanSync("teamService");
	  var teamId = req.params.id;
	  var status = req.query.status ? req.query.status === 'true' : null;
	  var source = req.query.source ? req.query.source : null;
	  
	  teamService.findUserResolvers(teamId, status, source, asyncResponse);
});

router.get('/teams/:id/users', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }

	  var teamService = javaContext.getBeanSync("teamService");
	  var teamId = req.params.id;
	  
	  teamService.findUsers(teamId, asyncResponse);
});


router.put('/teams/:id', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}
	}

	prettyJSON(req.body);

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.config.Team');
	var team = gson.fromJsonSync(JSON.stringify(req.body), clazz);
	var teamId = req.params.id;

	var teamService = javaContext.getBeanSync("teamService");
	teamService.update(teamId, team, asyncResponse);

});

router.get('/caseCategorySets', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type', 'application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}
	}
    
	var ids = req.query.ids ? req.query.ids : null;
  
	var caseCategorySetService = javaContext.getBeanSync("caseCategorySetService");
	
	if (ids === null) {
		var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
		var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
		var name = req.query.name ? req.query.name : null;
		
		caseCategorySetService.findAll(pageNumber, pageSize, name,asyncResponse);
		
	} else {
		caseCategorySetService.findByIds(ids, asyncResponse);
	}  
});

router.get('/caseCategorySets/all', function (req, res) {

    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  
  var caseCategorySetService = javaContext.getBeanSync("caseCategorySetService");	  
  caseCategorySetService.findAll(asyncResponse);
});

router.get('/caseCategorySetsActive/all', function (req, res) {

    function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();
	
	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }
	
	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }
    }
  
	var caseCategorySetService = javaContext.getBeanSync("caseCategorySetService");	  
	caseCategorySetService.findAllActive(asyncResponse);
});

router.get('/caseCategorySets/:id', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  
	  var caseCategorySetId = req.params.id;	
	  var caseCategorySetService = javaContext.getBeanSync("caseCategorySetService");	  
	  caseCategorySetService.findOne(caseCategorySetId, asyncResponse);
});

router.get('/caseCategorySetsWithActiveCategories/:id', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  
	  var caseCategorySetId = req.params.id;	
	  var caseCategorySetService = javaContext.getBeanSync("caseCategorySetService");	  
	  caseCategorySetService.findOneWithActiveCategories(caseCategorySetId, asyncResponse);
});

router.get('/caseCategorySets/:id/categories', function (req, res) {

	  function asyncResponse(err,response) {
	    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

	    if(response !== null) {
	      try {
	        res.set('Content-Type', 'application/json');
	        res.send(gson.toJsonSync(response));
	      } catch(ex) {
	        res.status(500).json(ex.cause.getMessageSync());
	      }
	    } else {
	      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	    }

	  }
	  

});

router.put('/caseCategorySets/:id', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}
	}

	prettyJSON(req.body);

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.config.CaseCategorySet');
	var caseCategorySet = gson.fromJsonSync(JSON.stringify(req.body), clazz);
	
	var caseCategorySetId = req.params.id;	
	var caseCategorySetService = javaContext.getBeanSync("caseCategorySetService");	  
	caseCategorySetService.update(caseCategorySetId, caseCategorySet,asyncResponse);
});

router.post('/caseCategorySets', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}
	}

	prettyJSON(req.body);

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.config.CaseCategorySet');
	var caseCategorySet = gson.fromJsonSync(JSON.stringify(req.body), clazz);

	var caseCategorySetService = javaContext.getBeanSync("caseCategorySetService");
	caseCategorySetService.createCaseCategorySet(caseCategorySet, asyncResponse);

});


router.get('/caseCategories/all', function (req, res) {
    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  var caseCategorySetService = javaContext.getBeanSync("caseCategorySetService");	  
  caseCategorySetService.findAllCategories(asyncResponse);
});


router.get('/subjectCategorySets', function (req, res) {
    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
    
  var ids = req.query.ids ? req.query.ids : null;
	
  var subjectCategorySetService = javaContext.getBeanSync("subjectCategorySetService");
  
  if (ids === null) {
    var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
    var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
    var name = req.query.name ? req.query.name : null;
    
    subjectCategorySetService.findAll(pageNumber, pageSize, name,asyncResponse);
		
  } else {
    subjectCategorySetService.findByIds(ids, asyncResponse);
  }
});

router.get('/subjectCategorySets/all', function (req, res) {
    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  var subjectCategorySetService = javaContext.getBeanSync("subjectCategorySetService");
  subjectCategorySetService.findAll(asyncResponse);
});

router.get('/subjectCategorySetsActive/all', function (req, res) {
    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  var subjectCategorySetService = javaContext.getBeanSync("subjectCategorySetService");
  subjectCategorySetService.findAllActive(asyncResponse);
});


router.get('/subjectCategorySets/:id', function (req, res) {

  function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  
  var subjectCategorySetId = req.params.id;	
  var subjectCategorySetService = javaContext.getBeanSync("subjectCategorySetService");	  
  subjectCategorySetService.findOne(subjectCategorySetId, asyncResponse);
});

router.get('/subjectCategorySetsWithActiveCategories/:id', function (req, res) {

  function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  
  var subjectCategorySetId = req.params.id;	
  var subjectCategorySetService = javaContext.getBeanSync("subjectCategorySetService");	  
  subjectCategorySetService.findOneWithActiveCategories(subjectCategorySetId, asyncResponse);
});

router.put('/subjectCategorySets/:id', function (req, res) {

function asyncResponse(err,response) {
	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	if(response !== null) {
		try {
			res.set('Content-Type','application/json');
			res.send(gson.toJsonSync(response));
		} catch(ex) {
			res.status(500).json(ex.cause.getMessageSync());
		}
	} else {
		res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	}
}

prettyJSON(req.body);

var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
var clazz = java.findClassSync('com.qsocialnow.common.model.config.SubjectCategorySet');
var subjectCategorySet = gson.fromJsonSync(JSON.stringify(req.body), clazz);

var subjectCategorySetId = req.params.id;	
var subjectCategorySetService = javaContext.getBeanSync("subjectCategorySetService");	  
subjectCategorySetService.update(subjectCategorySetId, subjectCategorySet,asyncResponse);
});

router.post('/subjectCategorySets', function (req, res) {

function asyncResponse(err,response) {
	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	if(response !== null) {
		try {
			res.set('Content-Type','application/json');
			res.send(gson.toJsonSync(response));
		} catch(ex) {
			res.status(500).json(ex.cause.getMessageSync());
		}
	} else {
		res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	}
}

prettyJSON(req.body);

var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
var clazz = java.findClassSync('com.qsocialnow.common.model.config.SubjectCategorySet');
var subjectCategorySet = gson.fromJsonSync(JSON.stringify(req.body), clazz);

var subjectCategorySetService = javaContext.getBeanSync("subjectCategorySetService");
subjectCategorySetService.createSubjectCategorySet(subjectCategorySet, asyncResponse);

});

router.get('/subjectCategories/all', function (req, res) {
    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  var subjectCategorySetService = javaContext.getBeanSync("subjectCategorySetService");
  subjectCategorySetService.findAllCategories(asyncResponse);
});

router.get('/thematics', function (req, res) {

    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  
  var thematicService = javaContext.getBeanSync("thematicService");	  
  thematicService.findAll(asyncResponse);
});

router.get('/thematics/:thematicId/series/:serieId/categories', function (req, res) {

    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  var thematicId = req.params.thematicId;
  var serieId = req.params.serieId;
  var thematicService = javaContext.getBeanSync("thematicService");	  
  thematicService.findCategoryGroupsBySerieId(thematicId, serieId, asyncResponse);
});



router.get('/subjects', function (req, res) {

    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  
  var pageNumber = req.query.pageNumber ? parseInt(req.query.pageNumber) : null;
  var pageSize = req.query.pageSize ? parseInt(req.query.pageSize) : null;
  var identifier = req.query.identifier ? req.query.identifier : null;
  var source = req.query.source ? req.query.source : null;
    
  var subjectService = javaContext.getBeanSync("subjectService");	  
  subjectService.findAll(pageNumber, pageSize, identifier, source, asyncResponse);
});

router.get('/subjects/:id', function (req, res) {

  function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  
  var subjectId = req.params.id;	
  var subjectService = javaContext.getBeanSync("subjectService");	  
  subjectService.findOne(subjectId, asyncResponse);
});


router.put('/subjects/:id', function (req, res) {

function asyncResponse(err,response) {
	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	if(response !== null) {
		try {
			res.set('Content-Type','application/json');
			res.send(gson.toJsonSync(response));
		} catch(ex) {
			res.status(500).json(ex.cause.getMessageSync());
		}
	} else {
		res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	}
}

prettyJSON(req.body);

var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
var clazz = java.findClassSync('com.qsocialnow.common.model.cases.Subject');
var subject = gson.fromJsonSync(JSON.stringify(req.body), clazz);

var subjectId = req.params.id;	
var subjectService = javaContext.getBeanSync("subjectService");	  
subjectService.update(subjectId,subject,asyncResponse);

});

router.post('/subjects', function (req, res) {

function asyncResponse(err,response) {
	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

	if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

	if(response !== null) {
		try {
			res.set('Content-Type','application/json');
			res.send(gson.toJsonSync(response));
		} catch(ex) {
			res.status(500).json(ex.cause.getMessageSync());
		}
	} else {
		res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
	}
}

prettyJSON(req.body);

var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
var clazz = java.findClassSync('com.qsocialnow.common.model.cases.Subject');
var subject = gson.fromJsonSync(JSON.stringify(req.body), clazz);

var subjectService = javaContext.getBeanSync("subjectService");
subjectService.createSubject(subject, asyncResponse);

});

router.get('/users', function (req, res) {

    function asyncResponse(err,response) {
    var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

    if(err)  { console.log(err); res.status(500).json(err.cause.getMessageSync()); return; }

    if(response !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(response));
      } catch(ex) {
        res.status(500).json(ex.cause.getMessageSync());
      }
    } else {
      res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
    }

  }
  var userName = req.query.userName ? req.query.userName : null;
  var userService = javaContext.getBeanSync("userService");
  if(userName === null) {
	  userService.findAll(asyncResponse);
  }else{
	  userService.findAllByUserName(userName,asyncResponse);
  }
});

router.post('/retroactive', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}
	}

	prettyJSON(req.body);

	var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateDeserialize()).setPrettyPrintingSync().createSync();
	var clazz = java.findClassSync('com.qsocialnow.common.model.retroactive.RetroactiveProcessRequest');
	var request = gson.fromJsonSync(JSON.stringify(req.body), clazz);

	var retroactiveService = javaContext.getBeanSync("retroactiveService");
	retroactiveService.executeNewProcess(request, asyncResponse);

});

router.get('/retroactive', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}

	}

	var retroactiveService = javaContext.getBeanSync("retroactiveService");
	retroactiveService.getProcess(asyncResponse);

});

router.delete('/retroactive', function (req, res) {

	function asyncResponse(err,response) {
		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }
		res.send(null);
		return;
	}

	var retroactiveService = javaContext.getBeanSync("retroactiveService");
	retroactiveService.cancelProcess(asyncResponse);

});

router.get('/teams/:id/segmentsActive', function (req, res) {

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

	  var teamId = req.params.id;
	  var segmentService = javaContext.getBeanSync("segmentService");
	  segmentService.findAllActiveIdsByTeam(teamId, asyncResponse);
});

router.post('/teams/:oldId/reassignSegment/:newId', function (req, res) {

	function asyncResponse(err,response) {
		var gson = new GsonBuilder().registerTypeAdapterSync(DateClazz, new JSONDateSerialize()).setPrettyPrintingSync().createSync();

		if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

		if(response !== null) {
			try {
				res.set('Content-Type','application/json');
				res.send(gson.toJsonSync(response));
			} catch(ex) {
				res.status(500).json(ex.cause.getMessageSync());
			}
		} else {
			res.status(500).json("Token " + req.body['tokenId'] + " invalid.");
		}
	}
	
    var oldTeamId = req.params.oldId;
    var newTeamId = req.params.newId;
    var segmentService = javaContext.getBeanSync("segmentService");
    segmentService.reassignNewTeam(oldTeamId, newTeamId, asyncResponse);
});


module.exports = router;
