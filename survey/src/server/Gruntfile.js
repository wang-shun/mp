module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        
        requirejs:{     
	      compile : {
	        options : {
	          //appDir : "content-dev/js/",
	          //baseUrl : ".",
	          //dir : 'target/',
	          mainConfigFile : 'src/main/resources/static/js/main.js',
	          optimize: "uglify2",
	          uglify2: {
	            compress: {
	              drop_console: true
	            }
	          },                   
	          keepLines: true,
	          preserveLicenseComments: false,
	          name: "main",
	          out: "target/classes/static/js/main.js",
	          logLevel : 0,
	          findNestedDependencies : true,
	          //fileExclusionRegExp: /^\./,
	          //inlineText: true
	        }
	      }
	    }
    });

    grunt.loadNpmTasks('grunt-contrib-requirejs');
    //grunt.loadNpmTasks('grunt-contrib-uglify');

    grunt.registerTask('default', ['requirejs']);

};
