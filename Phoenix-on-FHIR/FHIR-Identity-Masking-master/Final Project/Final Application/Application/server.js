'use strict';

var Hapi = require('hapi'),
    routes = require('./lib/routes');

// Get config values
var host = '0.0.0.0';
var port = 1338;

var server = new Hapi.Server({});

server.connection({
    host: host,
    port: port
});

// Add the route
server.route(routes);


// Start the server
var plugins = require('./lib/plugins');
server.register(plugins, function(err){
    if(err){
        console.log('Something bad happened while registering plugins. Exiting...');
        throw err;
    }

    server.start(function(){
        console.log('API started at: ' + server.info.uri);
    });
});
