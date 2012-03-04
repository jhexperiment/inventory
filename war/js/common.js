function querySite(url, data, success_function)
{
	if (isEmpty(data['type'])) {
		data['type'] = 'GET'
	}
	if (isEmpty(data['returnType'])) {
		/*
		 * "xml": Returns a XML document that can be processed via jQuery.
		 * "html": Returns HTML as plain text; included script tags are evaluated when inserted in the DOM.
		 * "script": Evaluates the response as JavaScript and returns it as plain text. Disables caching unless option "cache" is used. Note: This will turn POSTs into GETs for remote-domain requests.
		 * "json": Evaluates the response as JSON and returns a JavaScript object. In jQuery 1.4 the JSON data is parsed in a strict manner; any malformed JSON is rejected and a parse error is thrown. (See json.org for more information on proper JSON formatting.)
		 * "jsonp": Loads in a JSON block using JSONP. Will add an extra "?callback=?" to the end of your URL to specify the callback.
		 * "text": A plain text string.
		 */
		data['returnType'] = 'json'
	}
	$.ajax({
		'type': data['type'],
		'url': url,
		'data': data,
		'dataType': data['returnType']
	}).success(success_function);
}

function isEmpty(obj) {return (obj == null || obj == '' || $.isEmptyObject(obj));}

function isValidDate(dateString) {
	var date = Date.parse(dateString);
	
	return ! isNaN(date);
}