module.exports = function(grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        
        requirejs:{     
	      compile : {
	        options : {
	          //appDir : "content-dev/js/",
	          //baseUrl : ".",
	          //dir : 'target/',
	          mainConfigFile : 'target/frontend/static/js/main-$'+'{' + 'svn.revision}'+'.js' ,
	          optimize: 'none',
	          //uglify2: {
	          //  compress: {
	          //    drop_console: true
	          //  }
	          //},                   
	          keepLines: true,
	          preserveLicenseComments: false,
	          name: 'main-$' + '{' + 'svn.revision}',
	          out: 'target/classes/static/js/main-${svn.revision}.js',
	          logLevel : 0,
	          findNestedDependencies : true
	          // fileExclusionRegExp: /^\./,
	          // inlineText: true
	        }
	      }
	    }
    });

    grunt.loadNpmTasks('grunt-contrib-requirejs');
    //grunt.loadNpmTasks('grunt-contrib-uglify');

    grunt.registerTask('default', ['requirejs']);

};
