var socket = io("http://localhost:8080");

socket.on('message',function(message){
	let elem = document.getElementById('chatbox');
	elem.innerHTML=message;
	//printMessage(message);
});


socket.on('join2', function(data){
	console.log("we made it 2 player one's client side dashboard");

	var status = document.getElementById('status');
	status.innerHTML = "";
	//var text = document.createTextNode("JOIN PLAYER 2");

	var leftDiv = document.createElement("div"); //Create left div
    leftDiv.id = "left"; //Assign div id
    leftDiv.setAttribute("style", "float:left; width:66.5%; line-height: 26px; text-align:left; font-size:12pt; padding-left:8px; height:26px;"); //Set div attributes
    leftDiv.style.background =  "#FF0000";
    a = document.createElement('a');
    a.href =  '/newGame2'; // Insted of calling setAttribute
    a.innerHTML = "JOIN PLAYER 2" // <a>INNER_TEXT</a>
    status.appendChild(a); // Append the link to the div
    //document.body.appendChild(leftDiv); // And append the div to the document body
	//var newButton = document.createElement("BUTTON");

	//newButton.appendChild(text);
	//status.appendChild(newButton);

});
