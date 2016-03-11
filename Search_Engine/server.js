var express = require('express');

var app = express();

app.get('/', function (request, response) {
	response.send('Hello World');
});

app.listen(9000, function () {
	console.log('Server is running.');
});