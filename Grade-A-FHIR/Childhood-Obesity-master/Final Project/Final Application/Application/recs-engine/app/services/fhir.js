// services/fhir.js

/**
 * FHIR Service: functions for interacting with FHIR resources.
 */
var config = require('../config.json');
var _ = require('lodash');
var RSVP = require('rsvp');
var request = RSVP.denodeify(require('request'));

function FHIRService () {
}

/**
 * @param {string} params.patient: who the referral is about
 * @param {string} params.status: the status of the referral
 */
FHIRService.prototype.getReferralRequest = function (params) {
  return request({
    url: config.fhir_base_url + '/ReferralRequest',
    qs: _.extend({
      //'_sort:desc': '_lastUpdated',
      '_format': 'json'
    }, params)
  })
  .then(function (resp) {
    return JSON.parse(resp.body);
  });
}

/**
 * @param {string} params._id: the resource identity
 */
FHIRService.prototype.getPatient = function (params) {
  return request({
    url: config.fhir_base_url + '/Patient',
    qs: _.extend({
      //'_sort:desc': '_lastUpdated',
      '_format': 'json'
    }, params)
  })
  .then(function (resp) {
    return JSON.parse(resp.body);
  });
}

module.exports = new FHIRService();
