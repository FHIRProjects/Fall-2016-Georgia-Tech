// services/recommendations.js

/**
 * Recommendations Service: functions for generating community resource recommendations based on patient referral requests.
 */

var config = require('../config.json');
var _ = require('lodash');
var RSVP = require('rsvp');
var consts = require('../lib/consts');
var utils = require ('../lib/utils');
var FHIRService = require('./fhir');
var GooglePlacesService = require('./google-places');

function RecommendationsService () {
}

function parseDescription (description) {
  description = (description || '').toLowerCase();
  if (description.indexOf('wic') >= 0) {
    return consts.REFERRAL_TYPES.WIC;
  }
  if (description.indexOf('endocrinologist') >= 0) {
    return consts.REFERRAL_TYPES.ENDOCRINOLOGIST;
  }
  if (description.indexOf('community nutrition resources') >= 0) {
    return consts.REFERRAL_TYPES.COMMUNITY_NUTRITION_RESOURCES;
  }
  if (description.indexOf('community physical activity resources') >= 0) {
    return consts.REFERRAL_TYPES.COMMUNITY_PHYSICAL_ACTIVITY_RESOURCES;
  }
  return consts.REFERRAL_TYPES.UNKNOWN;
}

function getAddressCoordinates (address) {
  return GooglePlacesService.geocode({
    address: _.concat(address.line, [address.city, address.state]).join(',')
  })
  .then(function (results) {
    var result = _.first(results);
    return _.values(result.geometry.location);
  });
}

function getSearchQuery (referralType, age) {
  switch (referralType) {
    case consts.REFERRAL_TYPES.WIC:
      return 'wic';
    case consts.REFERRAL_TYPES.ENDOCRINOLOGIST:
      return 'endocrinologist';
    case consts.REFERRAL_TYPES.COMMUNITY_NUTRITION_RESOURCES:
      if (utils.isChild(age)) {
        return 'child nutrition';
      }
      return 'nutrition';
    case consts.REFERRAL_TYPES.COMMUNITY_PHYSICAL_ACTIVITY_RESOURCES:
      if (utils.isChild(age)) {
        return 'child physical activity';
      }
      return 'physical activity';
  }
  return null;
}

function getSearchType (referralType) {
  switch (referralType) {
    case consts.REFERRAL_TYPES.WIC:
      return 'wic';
    case consts.REFERRAL_TYPES.ENDOCRINOLOGIST:
      return 'endocrinologist';
    case consts.REFERRAL_TYPES.COMMUNITY_NUTRITION_RESOURCES:
    case consts.REFERRAL_TYPES.COMMUNITY_PHYSICAL_ACTIVITY_RESOURCES:
      return 'community resource';
  }
  return null;
}

RecommendationsService.prototype.get = function (patient, limit) {
  var promises = {
    referrals: FHIRService.getReferralRequest({
      'patient': patient,
      'status': 'pending'
    }),
    patient: FHIRService.getPatient({
      '_id': patient
    })
  };

  return RSVP.hashSettled(promises)
  .then(function (results) {
    var referTo = [];
    _.each(results.referrals.value.entry, function (referral) {
      var referralType = parseDescription(referral.resource.description);
      referTo.push(referralType);
    });

    var patient = _.first(results.patient.value.entry);
    var address = _.first(patient.resource.address);
    var age = utils.calculateAge(patient.resource.birthDate);

    return getAddressCoordinates(address)
    .then(function (location) {
      var promises = referTo.map(function (referralType) {
        return GooglePlacesService.textSearch({
          query: getSearchQuery(referralType, age),
          type: getSearchType(referralType),
          location: location
        });
      });

      return RSVP.allSettled(promises)
      .then(function (results) {
        var places = [];
        _.each(results, function (result) {
          _.each(result.value, function (place) {
            places.push(place.place_id);
          });
        });

        places = _.uniq(places);

        if (limit) {
          places = places.slice(0, limit);
        }
        return places;
      })
    });
  })
  .then(function (places) {
    var promises = places.map(function (place) {
      return GooglePlacesService.details({
        placeid: place
      });
    });

    return RSVP.allSettled(promises)
    .then(function (results) {
      var places = [];

      _.each(results, function (result) {
        places.push({
          'address': result.value.formatted_address,
          'phone_number': result.value.formatted_phone_number,
          'icon': result.value.icon,
          'name': result.value.name,
          'opening_hours': result.value.opening_hours.weekday_text,
          'maps_url': result.value.url,
          'website': result.value.website
        });
      });

      return {
        total: _.size(places),
        entry: places
      }
    });
  })
}

module.exports = new RecommendationsService();
