
console.log("Starting ...");
var search_term = process.argv[2];
console.log("Search term from command line: " + search_term);


var links   = new Array;
var http    = require("http");
var request = require("request");
var sleep   = require("sleep");
var fs      = require('fs');


function debugResponse(res, body) {
	console.log("    res = |" + console.dir(res) +"|");
	console.log("    res.request = |" + Object.keys(res.request) +"|");
	console.log("    res.statusCode = " + res.statusCode);
	console.log("    res.request.uri.href = |" + res.request.uri.href +"|");
	console.log("      body = " + body);
}


function handleFileLinks(links) {
	var re = new RegExp(search_term);
	// console.log("##### handleFileLinks - links = " + links);

	for(var i=0; i < links.length; i++) {
		console.log("  checking link: |" + links[i] + "|");
		sleep.sleep(1);

		request(links[i], function(error, res, body) {
			// debugResponse(res, body);
 			var reArray = re.exec(body);

			if(reArray) {
				console.log("  Found Match: " + res.request.uri.href);
			} else {
				// console.log("  Not Found");
			}
		}).on('error', function(e) {
			console.log("Error: " + e.message);
		});
	}
}


fs.readFile('links.txt', 'utf-8', function(err, data) {
	console.log("Parsing file ...");
	if(err) {
		return console.log(err);
	}

	// console.log("data = " + data);
	var lines = data.split("\n");
	// console.log("lines = " + lines);

	for(var i=0; i < lines.length; i++) {
		var line = lines[i];
		// console.log("  Current Line: " + line);
		if(line.trim().length > 0) {
			// found content
			links[links.length] = line.trim();
		}
	}

	console.log("Done.");

        console.log("Evaluating links ...");
	handleFileLinks(links);
});



