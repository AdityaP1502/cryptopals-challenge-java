const { KEY } = require('./server');

const sign = (message) => {
  // implement HMAC in here
  // return valid signature in hex
}

const sleep = (ms) => {
  return new Promise(resolve => setTimeout(resolve, ms));
}

const checkSignature_insecure = async (signature, message) => {
  // Generate valid signature
  const validSign = sign(message);
  const bufValidSign = new Buffer.from(validSign, 'hex');
  const bufSign = new Buffer.from(signature, 'hex');
  const zipSign = [bufSign, bufValidSign];
  const falseSign = false;

  for ([x, y] in zipSign) {
    if (x != y) {
      falseSign = true;
      break;
    }

    // sleep for 50ms if x == y
    await sleep(50);
  }

  return falseSign;
}

const getFileHandler = (request, h) => {
  // take file and signature query parameters
  const {file, signature} = request.query();
  isValid = checkSignature_insecure(signature, file);

  if (!isValid) {
    const response = h.response({
      status: 'fail', 
      message: 'Invalid Signature',
    });

    response.code(500);
    response.type('application/json');
    return response;
  }

  // TODO: Read file
  // TODO: Return the content of the file
}

module.exports = {
  getFileHandler,
}