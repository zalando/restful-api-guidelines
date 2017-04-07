const request = require('supertest-as-promised');
const app = require('../app');

describe('server.app', () => {
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

  test('GET /health endpoint should respond as expected', () => {
    return request(app).get('/health')
      .expect(200)
      .then((res) => {
        expect(res.body).toEqual({alive:true});
      });
  });

  test('GET /favicon.ico endpoint should respond as expected', () => {
    return request(app).get('/favicon.ico')
      .expect(200)
      .expect('Content-Type', /image\/x-icon/);
  });
});
