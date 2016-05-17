'use strict';

module.exports = function (grunt) {

	grunt.loadNpmTasks('grunt-gh-pages');

	grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
		'gh-pages': {
			options: {
				branch: 'gh-pages',
				base: '_book'
			},
			publish: {
				options: {
					repo: 'https://github.com/zalando/restful-api-guidelines.git',
					message: 'publish gh-pages (cli)'
				},
				src: ['**/*']
			},
			deploy: {
				options: {
					user: {
						name: 'ZalandoGhPages',
						email: 'no-reply@zalando.de'
					},
					repo: 'https://' + process.env.GH_TOKEN + '@github.com/zalando/restful-api-guidelines.git',
					message: 'publish gh-pages (auto)' + getDeployMessage(),
					silent: true
				},
				src: ['**/*']
			}
		}
	});

	// get a formatted commit message to review changes from the commit log
	// github will turn some of these into clickable links
	function getDeployMessage() {
		var ret = '\n\n';
		if (process.env.TRAVIS !== 'true') {
			ret += 'missing env vars for travis-ci';
			return ret;
		}
		ret += 'branch:       ' + process.env.TRAVIS_BRANCH + '\n';
		ret += 'SHA:          ' + process.env.TRAVIS_COMMIT + '\n';
		ret += 'range SHA:    ' + process.env.TRAVIS_COMMIT_RANGE + '\n';
		ret += 'build id:     ' + process.env.TRAVIS_BUILD_ID  + '\n';
		ret += 'build number: ' + process.env.TRAVIS_BUILD_NUMBER + '\n';
		return ret;
	}

	grunt.registerTask('check-deploy', function() {
		// only deploy under these conditions
		if (process.env.TRAVIS === 'true'
					&& process.env.TRAVIS_SECURE_ENV_VARS === 'true'
					&& process.env.TRAVIS_PULL_REQUEST === 'false'
					&& process.env.TRAVIS_BRANCH === 'master') {
			grunt.log.writeln('executing deployment');
			// queue deploy
			grunt.task.run('gh-pages:deploy');
		}
		else {
			grunt.log.writeln('skipped deployment');
		}
	});

	grunt.registerTask('publish', 'Publish from CLI', [
		'gh-pages:publish'
	]);

	grunt.registerTask('deploy', 'Publish from Travis', [
		'check-deploy'
	]);

	grunt.registerTask('default', ['publish']);
};
