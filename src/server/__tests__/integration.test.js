'use strict';

const request = require('supertest');

describe('zally-web-ui standalone', () => {
  const app = require('../index')();

  describe('GET /', () => {
    test('endpoint should respond as expected', () => {
      return request(app).get('/')
        .expect(200)
        .expect('Content-Type', /html/)
        .then((res) => {
          expect(res.text).toMatch(/<title>Zally API Linter WEB UI/);
          expect(res.text).toMatch(/<script src="\/env.js" type="text\/javascript"><\/script>/);
          expect(res.text).toMatch(/<div id="app"><\/div>/);
          expect(res.text).toMatch(/<script src="\/assets\/bundle.js" type="text\/javascript"><\/script>/);
        });
    });
  });

  describe('GET /env.js', () => {
    test('GET /env.js endpoint should respond as expected', () => {
      return request(app).get('/env.js')
        .expect(200)
        .expect('Content-Type', 'text/javascript')
        .then((res) => {
          expect(res.text).toMatch(/window\.env = \{.*\}/);
        });
    });
  });
});

describe('zally-web-ui mount', () => {
  const app = require('express')();
  const zally = require('../index')();

  app.use('/linter', zally);

  describe('GET /linter', () => {
    test('endpoint should respond as expected', () => {
      return request(app).get('/linter')
        .expect(200)
        .expect('Content-Type', /html/)
        .then((res) => {
          expect(res.text).toMatch(/<script src="\/linter\/env.js" type="text\/javascript"><\/script>/);
          expect(res.text).toMatch(/<script src="\/linter\/assets\/bundle.js" type="text\/javascript"><\/script>/);
        });
    });
  });
});
