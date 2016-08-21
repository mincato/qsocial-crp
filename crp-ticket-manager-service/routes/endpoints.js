var express = require('express');
var app = require('../app');
var router = express.Router();

var javaInit = require('../javaInit');
var java = javaInit.getJavaInstance();

var GsonBuilder = java.import('com.google.gson.GsonBuilder');

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


module.exports = router;
