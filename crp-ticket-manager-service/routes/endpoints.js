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
    var gson = new GsonBuilder().setPrettyPrintingSync().createSync();

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

router.get('/domains', function (req, res) {

	  function asyncResponse(err,responseDomains) {
	    var gson = new GsonBuilder().setPrettyPrintingSync().createSync();

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
	  domainService.findAll(pageNumber, pageSize, asyncResponse);

});

router.post('/domains', function (req, res) {

  function asyncResponse(err,responseDomain) {
    var gson = new GsonBuilder().setPrettyPrintingSync().createSync();

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

  var gson = new GsonBuilder().setPrettyPrintingSync().createSync();
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

  var domainService = javaContext.getBeanSync("domainService");
  domainService.createTrigger(domainId, trigger, asyncResponse);

});

router.put('/domains/:id', function (req, res) {
  console.log("yeah");
  function asyncResponse(err,responseDomain) {
    var gson = new GsonBuilder().setPrettyPrintingSync().createSync();

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

  var gson = new GsonBuilder().setPrettyPrintingSync().createSync();
  var clazz = java.findClassSync('com.qsocialnow.common.model.config.Domain');
  var domain = gson.fromJsonSync(JSON.stringify(req.body), clazz);
  var domainId = req.params.id;

  var domainService = javaContext.getBeanSync("domainService");
  domainService.update(domainId, domain, asyncResponse);

});

router.get('/thematics', function (req, res) {

  function asyncResponse(err,responseThematics) {
    var gson = new GsonBuilder().setPrettyPrintingSync().createSync();

    if(err)  { res.status(500).json(err.cause.getMessageSync()); return; }

    if(responseThematics !== null) {
      try {
        res.set('Content-Type', 'application/json');
        res.send(gson.toJsonSync(responseThematics));
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


module.exports = router;
