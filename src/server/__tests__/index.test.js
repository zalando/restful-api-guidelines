'use strict';

const request = require('supertest');
const app = require('../index')();

describe('server.index', () => {
  test('GET / endpoint should respond as expected', () => {
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

  test('GET /env.js endpoint should respond as expected', () => {
    return request(app).get('/env.js')
      .expect(200)
      .expect('Content-Type', 'text/javascript')
      .then((res) => {
        expect(res.text).toMatch(/window\.env = \{.*\}/);
      });
  });
});
