// Module for SHA1 Hash

// SHA1 Magic Number
const INITIALIZE_VAR0 = 0x67452301;
const INITIALIZE_VAR1 = 0xEFCDAB89;
const INITIALIZE_VAR2 = 0x98BADCFE;
const INITIALIZE_VAR3 = 0x10325476;
const INITIALIZE_VAR4 = 0xC3D2E1F0;
class SHA1 {
  constructor(message="", encoding='ASCII') {
    // message in ASCII
    this.BITMASK = 0xFFFFFFFF;
    this.state = new Array(5);
    this.length = null;
    this.chunks = null;

    if (message != "") {
      if (encoding == 'ASCII') {
        // Convert text into hex string
        message = Buffer.from(message).toString('hex');
      }
      this.length = message.length * 4;
      this.chunks = this.createChunks(this.preprocess(message));
    }
  }

  setMessage(message, encoding='ASCII') {
    if (encoding == 'ASCII') message = Buffer.from(message).toString('hex'); // Convert text into hex string
    this.length = message.length * 4;
    this.chunks = this.createChunks(this.preprocess(message));
  }

  preprocess(message) {
    let length = message.length * 4;
    message += '80';
    length += 8;

    let lenMod = length % 512;
    let n = lenMod < 448 ? (448 - lenMod) : 448 + (512 - lenMod);
    n = n / 4; // each hex consist of 4 bit
    
    // push 0 until message % 512 = 448
    for (let i = 0; i < n; i++) {
      message += '0';
    }

    // add message length as 64 bit big endian 
    let lengthHex = (this.length).toString(16);
    // prepend 0 so lengthHex is 64 bit
    lengthHex = '0'.repeat(16 - lengthHex.length) + lengthHex
    message+= lengthHex;

    return message;
  }

  createChunks(hex) {
    let i = 0;
    const n = hex.length / 128;
    const bytes = Buffer.from(hex, 'hex');
    const chunks = new Array(n).fill(0).map(() => new Array(64).fill(0))

    bytes.forEach(element => {
      chunks[Math.floor(i / 64)][i % 64] = element;
      i++;
    });

    return chunks;
  }

  breakChunks(chunk) {
    let n = 80; 
    let bitmask = 0xFF;
    let block = [];
    let temp;
    // break chunks into 32 bit word
    for (let i = 0; i < chunk.length; i += 4) {
      // concate 4 bytes into one word
      temp = ((chunk[i] & bitmask) << 24) + ((chunk[i + 1] & bitmask) << 16) + ((chunk[i + 2] & bitmask) << 8)  + (chunk[i + 3] & bitmask);
      block.push(temp & this.BITMASK);
    }

    return block;
  }

  fill(blocks) {
    let f;
    for (let i = 16; i < 80; i++) {
      f = blocks[i - 3] ^ blocks[i - 8] ^ blocks[i - 14] ^ blocks[i - 16];
      // do left rotate by 1
      f = this.lr(f, 1);
      blocks.push(f & this.BITMASK);
    }
  }

  mainLoopOp(a, b, c, d, i) {
    let f, g;

    if (i >= 0 && i <= 19) {
      f = (b & c) | ((~b) & d);
      g = 0x5A827999;
    } else if (i >= 20 && i <= 39) {
      f = b ^ c ^ d;
      g = 0x6ED9EBA1;
    } else if (i >= 40 && i <= 59) {
      f = (b & c) | (b & d) | (c & d);
      g = 0x8F1BBCDC;
    } else if (i >= 60 && i <= 79) {
      f = b ^ c ^ d;
      g = 0xCA62C1D6;
    }

    return [f, g];
  }

  initState() {
    this.state[0] = INITIALIZE_VAR0;
    this.state[1] = INITIALIZE_VAR1;
    this.state[2] = INITIALIZE_VAR2;
    this.state[3] = INITIALIZE_VAR3;
    this.state[4] = INITIALIZE_VAR4;
  }

  doProcess() {
    let a, b, c, d, e, f, g;
    let temp;
    
    for (let i = 0; i < this.chunks.length; i++) {
      /* preprocess block */
      // break chunks into 16, 32 bit word
      let words = this.breakChunks(this.chunks[i]);

      // fill empty slot in words
      this.fill(words);

      /* Init hash value */
      a = this.state[0];
      b = this.state[1];
      c = this.state[2];
      d = this.state[3];
      e = this.state[4];

      /* Main Loop */
      for (let j = 0; j < 80; j++) {
        [f, g] = this.mainLoopOp(a, b, c, d, j);
        
        temp = (this.lr(a, 5) + f + e + g + words[j]) & this.BITMASK;
        // update hash value
        e = d & this.BITMASK;
        d = c & this.BITMASK;
        c = this.lr(b, 30) & this.BITMASK;
        b = a & this.BITMASK;
        a = temp & this.BITMASK;
      }

      /* Add chunk hash into result */
      this.state[0] = (this.state[0] + a) & this.BITMASK;
      this.state[1] = (this.state[1] + b) & this.BITMASK;
      this.state[2] = (this.state[2] + c) & this.BITMASK;
      this.state[3] = (this.state[3] + d) & this.BITMASK;
      this.state[4] = (this.state[4] + e) & this.BITMASK;
    }
  }

  toString(x) {
    // javascript toString for number is a bit odd
    let temp;
    const y = 255; // 1111111
    let res = '';

    // x is a 32 bit number


    for (let i = 0; i < 4; i++) {
      temp = x & y;
      res = (temp > 15 ? temp.toString(16) : '0' + temp.toString(16)) + res;
      x = (x >> 8) ; // remove the first 8 bits    }
    }

    return res;
  }

  digest() {
    if (this.chunks === null) {
      console.log("Message isn't set");
      return;
    }

    let digest = "";
    this.initState();
    this.doProcess();
    this.state.forEach(element => {
      digest += this.toString(element);
    });

    return digest;
  }

  lr(word, count) {
    let bitmask = (1 << count) - 1;
    let temp = (word << count) + ((word >> (32 - count)) & bitmask);
    return temp & this.BITMASK;
  }
}

// let a = '6f737a7a7961166563747b77647f7873363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636363636';
// let b = '7061737377642e747874'
// let message = a + b;
// sha = new SHA1(message, 'hex');
// hash = sha.digest();
// console.log(hash);

module.exports = { SHA1 };