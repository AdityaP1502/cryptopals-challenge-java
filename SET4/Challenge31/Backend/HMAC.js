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

const XOR = (x, y) => {
  g = []

  for (let i = 0; i < x.length; i++) {
    g.push(x[i] ^ y[i])
  }

  return new Buffer.from(g);
}

const HMAC = async (message)  => {
  console.log(message);
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
  //console.log(`block key:${blockKey}`);
  blockKeyBytes = Buffer.from(blockKey, 'hex');

  opad = "5c".repeat(BLOCK_SIZE);
  opadBytes = Buffer.from(opad, 'hex');

  ipad = '36'.repeat(BLOCK_SIZE);
  ipadBytes = Buffer.from(ipad, 'hex')

  o_key_pad = XOR(blockKeyBytes, opadBytes);
  o_key_pad = o_key_pad.toString('hex')
  i_key_pad = XOR(blockKeyBytes, ipadBytes);
  i_key_pad = i_key_pad.toString('hex')

  // console.log(`o_key_pad: ${o_key_pad},\ni_key_pad: ${i_key_pad}`);

  Hash.setMessage(i_key_pad + message, 'HEX');
  const firstHash = Hash.digest();
  // console.log(`first round hash=`+ firstHash);
  // console.log(`Second round message: ${o_key_pad + firstHash}`);
  Hash.setMessage(o_key_pad + firstHash, 'HEX');
  const signature = Hash.digest();
  // console.log(`Signature is: ${signature}`);
  return signature;
}

module.exports = { sign };