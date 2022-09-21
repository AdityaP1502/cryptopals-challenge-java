const { SHA1 } = require('./Hash');
const { read } = require('./fileHandler');

const KEY_FILENAME = 'KEY.txt';


const BLOCK_SIZE = 64; // 64 bytes for SHA-1
const Hash = new SHA1();

// Module for HMAC signature
const sign = async (message) =>  {
  // TODO: Sign message using HMAC
  console.log(message);
  message = Buffer.from(message).toString('hex');
  return await HMAC(message); // sign is in hex
}

const getBlockKey = (key) => {
  let secondKey = key;
  // key that are bigger than block size are shortened by hashing
  if (secondKey.length > 128) {
    Hash.setMessage(key);
    secondKey = Hash.digest(); // key in hex
  }

  if (secondKey.length < 128) {
    // append '0'
    secondKey += '0'.repeat(128 - secondKey.length)
  }

  return secondKey;
}

const HMAC = async (message)  => {
  // TODO: Implement HMAC Algorithm here

  /* Input :
  * Message (string) : String message in hex 
  * Output:
  * SignedMessage : Message in hex
  */

  // read KEY
  const {data: KEY} = await read(KEY_FILENAME); 

  // compute blockSizedKey
  const key = Buffer.from(KEY).toString('hex'); 
  blockKey = getBlockKey(key);
  blockKeyBytes = parseInt(blockKey, 16);

  opad = "5c".repeat(BLOCK_SIZE);
  opadBytes = parseInt(opad, 16)

  ipad = '36'.repeat(BLOCK_SIZE);
  ipadBytes = parseInt(ipad, 16);

  o_key_pad = blockKeyBytes ^ opadBytes;
  i_key_pad =  blockKeyBytes ^ ipadBytes

  Hash.setMessage(i_key_pad + message, 'HEX');
  const firstHash = Hash.digest();

  Hash.setMessage(o_key_pad + firstHash, 'HEX');
  return Hash.digest();
}

module.exports = { sign };