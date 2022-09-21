const { getFileHandler, createHash } = require('./handler');

const routes = [
  {
    method: 'GET', 
    path: '/test', 
    handler: getFileHandler,
  },
  {
    method: 'GET', 
    path: '/test/hash', 
    handler: createHash, 
  }
]

module.exports = { routes };