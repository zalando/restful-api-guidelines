describe('server.env', () => {
  const env = require('../../../src/server/env');

  test('should be defined', () => {
    expect(env).toBeDefined();
  });

  test('should expose a public function', () => {
    expect(env.public).toBeInstanceOf(Function);
  });

  describe('when invoking the public function', () => {
    test('should expose object containing public keys', function (){
      expect(env.public()).toBeInstanceOf(Object);
    });
  });

});

