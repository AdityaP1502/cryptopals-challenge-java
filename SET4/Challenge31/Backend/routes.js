const { getFileHandler } = require('./handler');

const route = [
  {
    method: 'GET', 
    path: '/test', 
    handler: getFileHandler,
  }
]