// services/google-places.js

/**
 * Google Places Service: functions for interacting with the Google Places API.
 */
var config = require('../config.json');
var _ = require('lodash');
var RSVP = require('rsvp');
var googleMapsClient = require('@google/maps').createClient({
  key: config.google_places.api_key
});

function GooglePlacesService () {
}

GooglePlacesService.prototype.textSearch = function (params) {
  return RSVP.denodeify(googleMapsClient.places)(params)
  .then(function (resp) {
    return resp.json.results;
  });
}

GooglePlacesService.prototype.details = function (params) {
  return RSVP.denodeify(googleMapsClient.place)(params)
  .then(function (resp) {
    return resp.json.result;
  });
}

GooglePlacesService.prototype.geocode = function (params) {
  return RSVP.denodeify(googleMapsClient.geocode)(params)
  .then(function (resp) {
    return resp.json.results;
  });
}

module.exports = new GooglePlacesService();
