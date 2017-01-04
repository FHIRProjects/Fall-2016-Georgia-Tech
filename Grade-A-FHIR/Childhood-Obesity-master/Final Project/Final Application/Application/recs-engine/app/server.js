// server.js

var express = require('express');
var app = express();
var os = require('os');
var pjson = require('../package.json');
var config = require('./config.json');
var utils = require('./lib/utils');
var FHIRService = require('./services/fhir');
var GooglePlacesService = require('./services/google-places');
var RecommendationsService = require('./services/recommendations');

var startTime = new Date();

app.get('/recs', function (req, res) {
  var patient = req.query.patient;
  var limit = req.query.limit;
  return RecommendationsService.get(patient, limit).then(function (body) {
    res.set('content-type', 'application/json');
    res.send(body);
  });
});

app.get('/', function (req, res) {
	var params = req.query;
	return FHIRService.getReferralRequest(params).then(function (body) {
		res.set('content-type', 'application/json');
		res.send(body);
	});
});

app.get('/patient', function (req, res) {
  var params = req.query;
  return FHIRService.getPatient(params).then(function (body) {
    res.set('content-type', 'application/json');
    res.send(body);
  });
});

app.get('/search', function (req, res) {
  var params = req.query;
  return GooglePlacesService.textSearch(params).then(function (body) {
    res.set('content-type', 'application/json');
    res.send(body);
  });
});

app.get('/geocode', function (req, res) {
  var params = req.query;
  return GooglePlacesService.geocode(params).then(function (body) {
    res.set('content-type', 'application/json');
    res.send(body);
  });
});

app.get('/details', function (req, res) {
  var params = req.query;
  return GooglePlacesService.details(params).then(function (body) {
    res.set('content-type', 'application/json');
    res.send(body);
  });
});

app.get('/healthcheck', function (req, res) {
  res.send({
    'name': pjson.name,
    'version': pjson.version,
    'host': os.hostname(),
    'upTime': new Date() - startTime,
    'startTime': startTime
  });
})

app.listen(3000, function () {
	console.log('Example app listening on port 3000!');
});
