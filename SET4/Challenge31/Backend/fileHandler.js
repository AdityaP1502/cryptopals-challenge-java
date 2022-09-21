const fs = require('fs').promises;
const path = require('path');
const read = async (filename) => {
  // file is assumed to be in the same directory
  let err = 0;
  let data = null;
  const filepath = path.resolve("SET4", "Challenge31", "Backend", filename);
  try {
    data = await fs.readFile(filepath, encoding = 'utf-8');
  } catch (e) {
    console.log(e.name);
    console.log(e.message);
    console.log(e.stack);
    err = 1; 
  } finally {
    return {data, err};
  }
}

module.exports = {
  read,
}