
var socket = io("http://localhost:8080");

var avail1=true;
var liveArray= [[[0,0,0],[0,0,0],[0,0,0]],[[0,0,0],[0,0,0],[0,0,0]],[[0,0,0],[0,0,0],[0,0,0]]];
var count;
var points = 0;
var myMove = true;
var playertwo;
var moveCounter = 0;
var tf = true;
var date;


document.forms[0].onsubmit = function () {
    var input = document.getElementById("message");
		var msg = playertwo + ": " + input.value
    printMessage(msg);
    socket.emit('chat',msg);
    input.value = '';
};

function printMessage(message) {
    var p = document.createElement("p");
    p.innerText = message;
    document.querySelector("div.messages").appendChild(p);
	}

  socket.on('message', function(msg){
    printMessage(msg);
  });


function play2(data){
  console.log("wtf"+data.name);

  playertwo= data.name;
  console.log("wtf"+playertwo);

}
$.ajax({
  method: 'post',
  url: '/play2',
  //data: 'check_in_id='+$('#check_in_id').val(), 'users='+$('#users').val()
  success: play2

});

console.log("gametwo.js loaded");



socket.emit('join2', "two");

var html=
'<table id="levelone"><tr><td onclick="change(event)" id="1.1.1"></td><td onclick="change(event)" id="1.1.2"></td><td onclick="change(event)" id="1.1.3"></td></tr><tr><td onclick="change(event)" id="1.2.1"></td><td onclick="change(event)" id="1.2.2"></td><td onclick="change(event)" id="1.2.3"></td></tr><tr><td onclick="change(event)" id="1.3.1"></td><td onclick="change(event)" id="1.3.2"></td><td onclick="change(event)" id="1.3.3"></td></tr></table>'+

'<table id="leveltwo"><tr><td onclick="change(event)" id="2.1.1"></td><td onclick="change(event)" id="2.1.2"></td><td onclick="change(event)" id="2.1.3"></td></tr><tr><td onclick="change(event)" id="2.2.1"></td><td onclick="change(event)" id="2.2.2"></td><td onclick="change(event)" id="2.2.3"></td></tr><tr><td onclick="change(event)" id="2.3.1"></td><td onclick="change(event)" id="2.3.2"></td><td onclick="change(event)" id="2.3.3"></td></tr></table>'+

'<table id="levelthree"><tr><td onclick="change(event)" id="3.1.1"></td><td onclick="change(event)" id="3.1.2"></td><td onclick="change(event)" id="3.1.3"></td></tr><tr><td onclick="change(event)" id="3.2.1"></td><td onclick="change(event)" id="3.2.2"></td><td onclick="change(event)" id="3.2.3"></td></tr><tr><td onclick="change(event)" id="3.3.1"></td><td onclick="change(event)" id="3.3.2"></td><td onclick="change(event)" id="3.3.3"></td></tr></table>';

socket.on('displayGame', function(two){
  console.log("please display");

  document.getElementById('thegame').innerHTML = html;

});
// console.log("currentPlayer is"+currentPlayer);
// console.log("color is"+theColor);
var level;
var row;
var col;
currentPlayer=2;
theColor="red";

function change(jack){

  if(tf == true){
    tf = false;
    date = new Date();
  }

  moveCounter++;

  if(myMove == true){
    //console.log("player two made a move and two is false");
    myMove = false;
    var x= jack.target;
    var a=0;
    //console.log(x.id);
    var level= x.id[0]-1;
    var row = x.id[2]-1;
    var col= x.id[4]-1;
    //console.log(x.id);
    //console.log(level+row+col);
    liveArray[level][row][col]=currentPlayer;
    //console.log(liveArray);

    x.style.backgroundColor = "#b3f0ff";
    x.innerHTML="x";
    masterCheck();


    socket.emit('twoMove', x.id);
  }

}

  socket.on('oneMove', function(data){

    //moveCounter++;
    myMove=true;
    var level= data[0]-1;
    var row = data[2]-1;
    var col= data[4]-1;
    var theId = document.getElementById(data);

    theId.style.backgroundColor = "#ffc2b3";
    theId.innerHTML="x";
    liveArray[level][row][col] = 1;

    //console.log("player one made a move");
    //console.log(liveArray);
  });
  function quit(){
    if(avail1==true){

      socket.emit('p2quit', playertwo);

      socket.on('player2quit', function(data){
        console.log('player two data has arrived' + data);

        //window.alert("total wins:" + (data.wins) + ". total losses:" + (data.losses + 1) + ". number of moves made:" + moveCounter + ". start time:" + date);
        window.alert("\nYour Info:\nTotal wins: " + (data.wins) + "\nTotal losses: " + (data.losses + 1) + "\nNumber of moves made: " + moveCounter + "\nStart time: " + date);
      });

      socket.emit("quit2",playertwo);
      socket.emit('gamesplayed', playertwo);

      //socket.emit("twolost",playertwo);
    }

  }

  socket.on("quit",function(quit){
    avail1=false;
    socket.emit('gamesplayed', playertwo);

    window.alert("The other player has quit you win");

    socket.on('theplayerdata', function(data){

      //window.alert("The other player has quit you win");
      //window.alert("total wins:" + (data.wins + 1) + ". total losses:" + (data.losses) + ". number of moves made:" + moveCounter + ". start time:" + date);
      window.alert("Your Info:\nTotal wins: " + (data.wins + 1) + "\nTotal losses: " + data.losses + "\nNumber of moves made: " + moveCounter + "\nStart time: " + date);
    });

  });


  ////////////////
var pnt=0;
socket.on('pointsupdate', function(points){
  pnt = document.getElementById('point1');
  pnt.innerHTML="Player One Points: "+ points;

});







  function masterCheck(){

    checkRow();
    checkCol();
    LevelRow();
    levelCol();

    levelDiag();
    diag();
    levelVert();

    pnt = document.getElementById('point2');
    pnt.innerHTML="Player Two Points: "+ points;
    //
    socket.emit('pointsupdate', points);


    console.log("points are" + points);
    if(points >= 3){
      //socket.emit();
      console.log("player 1 wins sending to server"+playertwo);
      socket.emit("twowon",playertwo);
      socket.emit('gamesplayed', playertwo);
      window.alert("congrats you won");

      socket.emit('p2data', playertwo);

    }
    points = 0;

  }

  socket.on('player2', function(data){
    console.log('player two data has arrived' + data);

    //window.alert("total wins:" + (data.wins + 1) + ". total losses:" + data.losses + ". number of moves made:" + moveCounter + ". start time:" + date);
    window.alert("Your Info:\nTotal wins: " + (data.wins + 1) + "\nTotal losses: " + data.losses + "\nNumber of moves made: " + moveCounter + "\nStart time: " + date);
  });

socket.on("1won",function(data){
  socket.emit('gamesplayed', playertwo);
  socket.emit("twolost",playertwo);


  socket.on('theplayerdata', function(data){

    //window.alert("player one won");
    window.alert("Player one won\n" + "\nYour Info:\nTotal wins: " + (data.wins) + "\nTotal losses: " + (data.losses + 1) + "\nNumber of moves made: " + moveCounter + "\nStart time: " + date);
  });
  //window.alert("player one won");
});








  function LevelRow(){
    //onsole.log("check level row");
    var count = 0;
    for(var i=0; i<3; i++){
      if(liveArray[0][i][0] == currentPlayer){
        count++;
        if(liveArray[1][i][1] == currentPlayer){
          count++
        }
        if(liveArray[2][i][2] == currentPlayer){
          count++
        }
        if(count == 3){
          //socket.emit();
          console.log("level row win");
          points++;
        }
      }
      count = 0;
    }

    count = 0;
    for(var i=0; i<3; i++){
      if(liveArray[0][i][2] == currentPlayer){
        count++;
        if(liveArray[1][i][1] == currentPlayer){
          count++
        }
        if(liveArray[2][i][0] == currentPlayer){
          count++
        }
        if(count == 3){
          //socket.emit();
          console.log(" level row win");
          points++;
        }
      }
      count = 0;
    }

  }

  function checkRow(){
    //console.log("check row");
    var count = 0;

    for(var i=0; i<3; i++){
      for(var j=0; j<3; j++){
        for(var k=0; k<3; k++){
          if(liveArray[i][j][k] == currentPlayer){
            count++;
          }
          if(count == 3){
            console.log("row win");
            points++;
          }
        }
        count = 0;
      }
    }

  }

  function levelCol(){
    //console.log("level col");
    var count = 0;
    for(var i=0; i<3; i++){
      if(liveArray[0][0][i] == currentPlayer){
        count++;
        if(liveArray[1][1][i] == currentPlayer){
          count++
        }
        if(liveArray[2][2][i] == currentPlayer){
          count++
        }
        if(count == 3){
          //socket.emit();
          console.log("level col win");
          points++;
        }
      }
      count = 0;
    }

    count = 0;
    for(var i=0; i<3; i++){
      if(liveArray[0][2][i] == currentPlayer){
        count++;
        if(liveArray[1][1][i] == currentPlayer){
          count++
        }
        if(liveArray[2][0][i] == currentPlayer){
          count++
        }
        if(count == 3){
          //socket.emit();
          console.log("level col win");
          points++;
        }
      }
      count = 0;
    }

  }


  function checkCol(){
    //console.log("check col");
    var count = 0;

    for(var i=0; i<3; i++){
      for(var j=0; j<3; j++){
        for(var k=0; k<3; k++){
          if(liveArray[i][k][j] == currentPlayer){
            count++;
          }
          if(count == 3){
            console.log("col win");
            points++;
          }

        }
        count = 0;
      }
    }

  }


  function diag(){
    //console.log("diag");
    count=0;
    for(i=0;i<3;i++){
      if(liveArray[i][0][0]== currentPlayer){
        count++
        if(liveArray[i][1][1]== currentPlayer){
          count++;
        }
        if(liveArray[i][2][2]== currentPlayer){
          count++;
        }
      }
      if (count==3){
        console.log("diag win");
        count=0;
        points++;
      }
      count=0;

      if(liveArray[i][0][2]== currentPlayer){
        count++
        if(liveArray[i][1][1]== currentPlayer){
          count++;
        }
        if(liveArray[i][2][0]== currentPlayer){
          count++;
        }
      }
      if (count==3){
        console.log("diag win");
        count=0;
        points++;
      }
      count=0;
    }
  }
  function levelDiag(){
    //console.log("levelDiag");
  count=0;
    if(liveArray[0][0][0]== currentPlayer){
      //console.log("from left");
      count++
      if(liveArray[1][1][1]== currentPlayer){
        count++;
      }
      if(liveArray[2][2][2]== currentPlayer){
        count++;
      }
    }
    if (count==3){
      console.log("levelDiag win");
      count=0;
      points++;
    }
    count=0;

    if(liveArray[0][0][2]== currentPlayer){
      //console.log("from right"+count);

      count++
      if(liveArray[1][1][1]== currentPlayer){
        count++;
      }
      if(liveArray[2][2][0]== currentPlayer){
        count++;
      }
    }
    if (count==3){
      console.log("levelDiag win");
      count=0;
      points++;
    }
    count=0;

  }

  function levelVert(){
    count=0;
    //console.log("levelVert");
    for(i=0; i<3;i++){
      for(j=0;j<3;j++){
        if(liveArray[0][i][j]== currentPlayer){
          count++
          if(liveArray[1][i][j]== currentPlayer){
            count++
          }
          if(liveArray[2][i][j]== currentPlayer){
            count++
          }
          if(count==3){
            console.log("levelVert win");
            points++;
          }
          count=0;
        }
      }
    }
  }
