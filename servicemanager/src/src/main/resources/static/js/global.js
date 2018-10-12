/**
 * define the console.log function if the browser doesn't support.
 */
if (typeof console === "undefined" || typeof console.log === "undefined") {
	console = {};
	console.log = function() {};
}

/**
 * Add global functions.
 */
define(["jquery"], function($) {

});