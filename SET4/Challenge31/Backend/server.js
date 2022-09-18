const {Hapi} = require('@hapi/hapi');
const {readFile} = require('./fileHandler');

const KEY_FILENAME = 'KEY.txt';
const KEY = readFile(KEY_FILENAME);

const init = async () => {
  const server = Hapi.Server({
    port: 5000, 
    host: 'localhost',
    routes: {
      cors: 'true'
    }
  })

  server.route(routes);
  await server.start();
  console.log(`Server berjalan pada ${server.info.uri}`);
}

init()

module.exports = {KEY};