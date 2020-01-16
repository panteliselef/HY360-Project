function ajaxRequest(method, url, dataToServer, callback, isAsync = true) {
	let xmlReq = new XMLHttpRequest();
	// xmlReq.withCredentials = true;
	const onloadHandler = (e) => {
		if (xmlReq.status >= 400) {
			console.log('Status Code', xmlReq.status);
			console.log('Content', xmlReq.responseText);
			callback(JSON.parse(xmlReq.responseText));
		} else {
			console.log(xmlReq.responseText, xmlReq.status);
			callback(JSON.parse(xmlReq.responseText));
		}
	};
	xmlReq.open(method, url, isAsync);
	// xmlReq.setRequestHeader('Access-Control-Allow-Origin','*');
	// xmlReq.setRequestHeader('access-control-allow-origin','*');
	xmlReq.addEventListener('load', onloadHandler);
	xmlReq.addEventListener('error', onloadHandler);
	xmlReq.send(dataToServer);
}

export default ajaxRequest;