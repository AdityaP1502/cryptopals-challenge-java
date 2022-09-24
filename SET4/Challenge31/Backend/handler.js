const { read } = require('./fileHandler');
const { sign } = require('./HMAC');
const sleep = (ms) => {
  return new Promise(resolve => setTimeout(resolve, ms));
}

const checkSignature_insecure = async (signature, message) => {
		// Generate valid signature
		let startTime, endTime;
		let a, b;
		let validSign = await sign(message);
		console.log(`User signature : ${signature}`);
		console.log(`Valid signature: ${validSign}`)

		const bufValidSign = [...new Buffer.from(validSign, 'hex')];
		const bufSign = [...new Buffer.from(signature, 'hex')];
		const zipSign = bufValidSign.map((e, i) => [e, bufSign[i]]);
		let falseSign = false;
		a = Date.now();
		for (const element of zipSign) {
			let [x, y] = element;
			if (x != y) {
				falseSign = true;
				break;
			}

			// sleep for 50ms if x == y
		startTime = Date.now();
		await sleep(20);
		endTime = Date.now();
		//console.log(`I sleep for ${endTime - startTime} ms`);
		}
		b = Date.now();
		console.log(`Checking takes ${b - a} ms`);
		//console.log(`average sleep is ${(b - a) / 20}`);
		return falseSign;
}

const getFileHandler = async (request, h) => {
		// take file and signature query parameters
		const {file, signature} = request.query;

		if (!file && !signature) {
			const response = h.response({
				status: 'fail', 
				message: 'Invalid file or signature'
			})
			response.code(500);
			response.type('application/json');
			return response;
		}

		const date = new Date().toString().slice(0, 24);
		console.log(`${date}: Received File request ${file} with signature ${signature}`);

		isFalse = await checkSignature_insecure(signature, file);
		// isFalse = true;
		console.log(`Valid Signature: ${!isFalse}`);

		if (isFalse) {
			const response = h.response({
				status: 'fail', 
				message: 'Invalid Signature',
			});

			response.code(500);
			response.type('application/json');
			return response;
		}

		// TODO: Read file
		const {data, err} = await read(file);
		console.log(err);

		if (err == 1) {
			const response = h.response({
				'status': 'fail', 
				'message': 'File not found or file is broken'
			}).code(500)
				.type('application/json');

			return response;
		}

		console.log(data);
		const response = h.response({
			stataus: 'success', 
			message: 'Authentication successfull', 
			data , 
		})
		response.code(200);
		response.type('application/json');
		return response;
}

const createHash = (request, h) => {
		const {message} = request.query;
		if (!message) {
			const response = h.response({
				status: 'fail', 
				message: 'Invalid message'
			}).code(500)
				.type('application/json');
			return response;
		} 

		hashGenerator = new SHA1(message);
		hash = hashGenerator.digest();

		const response = h.response({
			status: 'success',
			hash,
		}).code(200)
			.type('application/json');

		return response;
}

module.exports = {
  getFileHandler, createHash, 
}