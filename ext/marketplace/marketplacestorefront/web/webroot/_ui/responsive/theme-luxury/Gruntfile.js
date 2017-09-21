'use strict';
var path = require('path');
function absolutePath(file) {
  return path.join(__dirname, file);
}

module.exports = function(grunt) {
grunt.initConfig({
	includes: {
		build: {
			cwd: 'html',
			src: ['*.html'],
			dest: 'build/',
			options: {
				flatten: true,
				includePath: 'html-include',
			}
		}
	},
	sass: {
		options: {
			sourcemap: 'none',
			noCache: true
		},
		compile: {
			files: {
				'css/uistyles.css': 'sass/main.scss'
			}
		}, 
		updateTrue: {
			options: {
				update: true
			},

		}		
	},
	cssmin: {
        options: {
            style: 'compressed',
        },
        absolute: {
            files: [{
                src: [
					'css/jquery.selectBoxIt.css',
					'css/uistyles.css',										
                ].map(absolutePath),
                dest: 'css/style.css'
            }]
        },
    },
	
	uglify: {
      options: {
        beautify:true,
        mangle: false
      },
      lib: {
		files: [{
			src: [	
				    'js/lib/jquery-2.1.1.min.js',
					'js/lib/jquery-ui-1.11.2.min.js',
					'js/lib/jquery.selectBoxIt.min.js',
					'js/lib/slick.min.js',
                    'js/lib/jquery.elevatezoom.js',
					'js/lib/bootstrap.min.js',
				    'js/lib/jquery.validate.min.js',
				    'js/main.js',
                    'js/acc.accountaddress.js',                
                    'js/acc.productDetail.js',
                    'js/marketplacecheckoutaddon.js'
				    /*'js/login.js',
				    'js/tulvalidation.js',*/
				   
				
				].map(absolutePath), 
					dest: 'combined/luxury-main.js'
			}]
		}
    }, 
	
	watch: {
		html: {
			files: ['html-include/*.html'],
			tasks: ['includes'],
			options: {livereload: true},
			event: ['added', 'deleted']
		},
        css: {
            files:['css/*.css','sass/*.scss', '**/*.scss'],
            tasks: ['sass', 'cssmin']
        },
        js: {
            files: ['js/*.js','js/lib/*.js', 'Gruntfile.js'], 
            tasks: ['uglify']
        },
    }, 
	
});


require('matchdep').filterDev('grunt-*').forEach(grunt.loadNpmTasks);
grunt.registerTask('default', ['includes','sass', 'cssmin','uglify','watch']);

}
