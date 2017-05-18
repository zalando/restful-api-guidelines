const app = require('express')();
const zally = require('./src/server')();

app.get('/', (req, res) => {
  res.status(200).send('Home');
});

app.use('/linter', zally);

app.listen(3000, () => {
  console.log('app running at http://localhost:3000');
});
