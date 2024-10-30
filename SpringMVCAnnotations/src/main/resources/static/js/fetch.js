const requestUrl = userForm.action;
const submitBtn = document.querySelector("input[type=submit]");
let jsonData = {};
let resultData;

submitBtn.addEventListener("click", event => {
	event.preventDefault();
	for (let i = 0; i < userForm.elements.length; i++) {
		if (userForm.elements[i].name !== "") {
			jsonData[userForm.elements[i].name] 
				= userForm.elements[i].value;
		} 
	}
	
	fetch(requestUrl, {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json; charset=utf-8'
		},
		body: JSON.stringify(jsonData)
	}).then(response => response.json())
	.then(data => {
		constructResult(data);
	});
	
});

/**
 * 서버로부터 가져온 데이터를 화면에 구성.
 */
function constructResult(jsonData) {
	let responseRootElement = document.createElement("div");
	let ulElement = document.createElement("ul");
	
	for(let d in jsonData) {
		//console.log(d);
		//console.log(jsonData[d]);
		
		let liElement = document.createElement("li");
		let textNode = document.createTextNode(`${d} : ${jsonData[d]}`);
		liElement.appendChild(textNode);
		ulElement.appendChild(liElement);
	}
	responseRootElement.append(ulElement);
	
	document.querySelector("body")
		.appendChild(responseRootElement);
}
