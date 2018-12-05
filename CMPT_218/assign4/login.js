/*CHANGES
-I added allllll the code for app.get('/newGame', function(req, res){
My idea is to basically make 3 main divisions; 1: playerone,2:playertwo,3:thegame
If you look at the css you will see that i try to separate the divisions

I also added a button saying hi in /newGame also. This button triggers
socket in client.js and that sends hi to server. server sends hi back. and then client writes it into the chatbox. THIS IS FOR TESTING. I just wanted to get socket working and now I can sleep.

*/
var express = require("express");
var app = express();
var flash = require('express-flash');
var session = require('express-session');
var MongoClient = require('mongodb').MongoClient;
var url = "mongodb://myUsers:pass0987@ds117489.mlab.com:17489/mydb";
//var url = "mongodb://sarbjot:sarbjot@ds121599.mlab.com:21599/mydb";
var port =8080;
var http = require('http');

var server = http.createServer(app).listen(port);

var io = require('socket.io')(server);


MongoClient.connect(url, function(err, client){                   //create database
  if (err) console.log(err);
  database = client.db('mydb'); // use

  gamers = database.collection('gamers');
  activeGamers = database.collection('active');


  activeGamers.deleteMany({});
});

var client = 0;
var database;
var gamers;
var gamersOnline = [];
var currentUser;
var playerone;
var playertwo;
var oneWins;
var twoWins;
var twoLosses;
var oneLosses;



var head = `<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="minimum-scale=1.0, width=device-width, maximum-scale=1.0, user-scalable=no"/>
    <meta charset="utf-8">
    <title>Users</title>
    <link rel="stylesheet" href="/css/style.css"/>
</head>
<body>`;
var headGame = `<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="minimum-scale=1.0, width=device-width, maximum-scale=1.0, user-scalable=no"/>
    <meta charset="utf-8">
    <title>Users</title>
    <link rel="stylesheet" href="/css/game.css"/>
</head>
<body>`;

var _head = `
<script src="js/socket.io-client.min.js"></script>
<script src="js/client.js"></script>
</body>
</html>`;

var _headgameone = `
<script src="js/jquery.min.js"></script>
<script src="js/socket.io-client.min.js"></script>
<script src="js/gameone.js"></script>

</body>
</html>`;
var _headgametwo = `
<script src="js/jquery.min.js"></script>
<script src="js/socket.io-client.min.js"></script>
<script src="js/gametwo.js"></script>
</body>
</html>`;


app.use(flash());


function logReq(req,res,next){
  console.log(`${req.method} request for ${req.url} - ${JSON.stringify(req.body)}`);
  next();
}


function isLoggedIn(req,res,next){
  if (req.session.user){
    var sid = req.sessionID;
    console.log('SID:',sid);
    next();
  } else {
    req.flash('error','login first!!!');
    res.redirect('/login');
  }
}
function onewon(){



}


app.use('/',logReq);

app.use(session({
  name:"session",
  secret: "zordon",
  maxAge: 1000 * 60 * 30,
  saveUninitialized: true,
  resave:true

}));

app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(express.static('./pub'));

io.on('connection', function(socket){
  console.log('new connection');

  socket.on('chat', function(message){
    socket.broadcast.emit('message',message);
  });

  socket.on('join2', function(data){
    socket.broadcast.emit('join2',data);

  });
  socket.on('displayGame', function(data){
    socket.broadcast.emit('displayGame',data);
  });

  socket.on('oneMove', function(data){
    socket.broadcast.emit('oneMove',data);
  });

  socket.on('twoMove', function(data){
    socket.broadcast.emit('twoMove',data);
  });
  socket.on('pointsupdate', function(data){
    socket.broadcast.emit('pointsupdate',data);
  });

  ///
  socket.on('quit1', function(data){
    socket.broadcast.emit('quit',data);
    var query = { user: data };
    gamers.findOne(query, function(err, result) {
      if (err) throw err;
      var myVar = setInterval(myTimer, 100);
      function myTimer() {

          if(result!=undefined){
            console.log("one has loses: " +result.losses);
            clearInterval(myVar);
            oneLosses= JSON.parse(result.losses)+1;
            //
            console.log("the data is and wins "+ data+" "+oneLosses);

            var myquery = { user: data };
            var newvalues = { $set: {losses: oneLosses} };
            gamers.updateOne(myquery, newvalues, function(err, res) {
              if (err) throw err;

            });
        }
    }
  });
  });

  socket.on('quit2', function(data){
    socket.broadcast.emit('quit',data);
    var query = { user: data };
    gamers.findOne(query, function(err, result) {
      if (err) throw err;
      var myVar = setInterval(myTimer, 100);
      function myTimer() {

          if(result!=undefined){
            console.log("two has loses: " +result.losses);
            clearInterval(myVar);
            twoLosses= JSON.parse(result.losses)+1;
            //
            console.log("the data is and wins "+ data+" "+twoLosses);

            var myquery = { user: data };
            var newvalues = { $set: {losses: twoLosses} };
            gamers.updateOne(myquery, newvalues, function(err, res) {
              if (err) throw err;

            });
        }
    }
  });
  });


  socket.on('onewon', function(data){
      //notify other person who won
      socket.broadcast.emit('1won',data);

      var query = { user: data };
      gamers.findOne(query, function(err, result) {
        if (err) throw err;
        var myVar = setInterval(myTimer, 100);
        function myTimer() {

            if(result!=undefined){
              console.log("one has wins " +result.wins);
              clearInterval(myVar);
              oneWins= JSON.parse(result.wins)+1;
              //
              console.log("the data is and wins "+ data+" "+oneWins);

              var myquery = { user: data };
              var newvalues = { $set: {wins: oneWins} };
              gamers.updateOne(myquery, newvalues, function(err, res) {
                if (err) throw err;

              });

          }

      }
    });
  });
    socket.on('twowon', function(data){
      //notify other person who won
      socket.broadcast.emit('2won',data);

      var query = { user: data };
      gamers.findOne(query, function(err, result) {
        if (err) throw err;
        var myVar = setInterval(myTimer, 100);
        function myTimer() {

            if(result!=undefined){
              console.log("one has wins " +result.wins);
              clearInterval(myVar);
              twoWins= JSON.parse(result.wins)+1;
              //
              console.log("the data is and wins "+ data+" "+twoWins);

              var myquery = { user: data };
              var newvalues = { $set: {wins: twoWins} };
              gamers.updateOne(myquery, newvalues, function(err, res) {
                if (err) throw err;

              });
          }
      }
    });
  });
  socket.on('onelost', function(data){


    var query = { user: data };
    gamers.findOne(query, function(err, result) {
      if (err) throw err;
      var myVar = setInterval(myTimer, 100);
      function myTimer() {

          if(result!=undefined){
            console.log("one has loses: " +result.losses);
            clearInterval(myVar);
            oneLosses= JSON.parse(result.losses)+1;
            //
            console.log("the data is and wins "+ data+" "+oneLosses);

            var myquery = { user: data };
            var newvalues = { $set: {losses: oneLosses} };
            gamers.updateOne(myquery, newvalues, function(err, res) {
              if (err) throw err;

            });
        }
    }
  });
});
socket.on('twolost', function(data){

  var query = { user: data };
  gamers.findOne(query, function(err, result) {
    if (err) throw err;
    var myVar = setInterval(myTimer, 100);
    function myTimer() {

        if(result!=undefined){
          console.log("two has losses: " +result.losses);
          clearInterval(myVar);
          twoLosses= JSON.parse(result.losses)+1;
          //
          console.log("the data is and wins "+ data+" "+twoLosses);

          var myquery = { user: data };
          var newvalues = { $set: {losses: twoLosses} };
          gamers.updateOne(myquery, newvalues, function(err, res) {
            if (err) throw err;

          });
      }
  }
});
});
var gamesPlayed;
socket.on('gamesplayed', function(data){

  var query = { user: data };
  gamers.findOne(query, function(err, result) {

    socket.emit('theplayerdata', result);

    if (err) throw err;
    var myVar = setInterval(myTimer, 100);
    function myTimer() {

        if(result!=undefined){
          console.log("two games played updated: " +result.gamesplayed);
          clearInterval(myVar);
          gamesPlayed= JSON.parse(result.gamesplayed)+1;
          //
          //console.log("the data is and wins "+ data+" "+twoLosses);

          var myquery = { user: data };
          var newvalues = { $set: {gamesplayed: gamesPlayed} };
          gamers.updateOne(myquery, newvalues, function(err, res) {
            if (err) throw err;

          });
      }
  }
});
});

socket.on('p1data', function(data){

  var myquery = { user: data };
  gamers.findOne(myquery, function(err, result) {
    console.log("player 1 data is about to send");
    socket.emit('player1', result);
  });
});

socket.on('p2data', function(data){

  var myquery = { user: data };
  gamers.findOne(myquery, function(err, result) {
    console.log("player 2 data is about to send");
    socket.emit('player2', result);
  });
});

socket.on('p2quit', function(data){

  var myquery = { user: data };
  gamers.findOne(myquery, function(err, result) {
    console.log("player 2 data is about to send");
    socket.emit('player2quit', result);
  });
});

socket.on('p1quit', function(data){

  var myquery = { user: data };
  gamers.findOne(myquery, function(err, result) {
    console.log("player 2 data is about to send");
    socket.emit('player1quit', result);
  });
});



});

app.get('/login', function(req,res){                                    //request server for the root page

  var error = req.flash('error');

  var form = `<h1>LOGIN</h1>
  <section>
    <form method="post">
        <input type="text" id="uname" name="uname" placeholder="username" required /> <br />
        <input type="password" id="password" name="password" placeholder="password" required /> <br />
        <button formaction="/userLogin">login</button>
    </form>

  </section>
  <a href="/registerPage">create new user</a>
  <h2>Features</h2>
  <ul style="list-style-type:disc">
    <li>Chatbox to communicate with your opponent</li>
    <li>Different color schema to distinguish the player on the tictactoe board</li>
    <li>Immediate on-page feedback when player wins/loses or points are updated</li>
    <li>Tables designed to provide 3D component of the game</li>
  </ul>
    <h2>Instructions</h2>
    <ul style="list-style-type:disc">
      <li>Register if not already</li>
      <li>First person registers and waits</li>
      <li>Second person registers and clicks "play with" button</li>
      <li>First player joins player two</li>
      <li>If a player gets 3 in a row in any direction that is 1 point</li>
      <li>First person to reach 3 points wins!!</li>
    </ul>`;

  form = head + '<p id="error">' + error + '</p>'+ form + _head;

  res.writeHead(200, {
      "Content-Type": "text/html",
      'Content-Length': form.length
  });
  res.end(form);
});



app.post('/userLogin', function(req, res, next) {                            //requests server to log user into their account
  //var error = req.flash('error');
  gamers.findOne({user: req.body.uname}, function(err, result){
    if(err) throw err;
    //console.log(result);
    if(result == null)
    {
      req.flash('error','user does not exist');
      res.redirect("/login");

    }
    else if(req.body.password != result.pass){

      req.flash('error','sorry wrong password');
      res.redirect("/login");
    }
    else
    {
      console.log("where are you");
      req.session.user = req.body.uname;
      currentUser = req.session.user;
      res.redirect('/dashboard');
    }

  });

});
app.post('/play1', function(req, res, next) {                            //requests server to log user into their account
  activeGamers.findOne({player: "one"}, function(err, player){
  if(err) throw err;
  var myVar = setInterval(myTimer, 700);
  function myTimer() {
      if(player!=undefined){
        //console.log("here it is"+JSON.stringify(result));
        clearInterval(myVar);
        res.json(player);
      }
    }

});
});

app.post('/play2', function(req, res, next) {                            //requests server to log user into their account
  activeGamers.findOne({player: "two"}, function(err, player){
  if(err) throw err;
  var myVar = setInterval(myTimer, 700);
  function myTimer() {
      if(player!=undefined){
        //console.log("here it is"+JSON.stringify(result));
        clearInterval(myVar);
        res.json(player);
      }
    }

});
});
app.get('/dashboard', isLoggedIn, function(req,res){                  //takes user to dashboard upon login
  var error = req.flash('error');
  //console.log("kiddaaaaaa");
  //var count = activeGamers.count();
  //console.log(count);

  activeGamers.findOne({player: "one"}, function(err, player){
    if(err) throw err;

    if(player == null){

      activeGamers.insert({player: "one", name: req.session.user}, function(err){
          if(err) throw err;
          console.log("player 1 added to array!!!!!!");
        });

        gamers.findOne({user: req.session.user}, function(err, person){


          dash = '<h1>'+ req.session.user +'</h1>'+ '<br><p id="error">'+error+'</p>' +
                '<div id="status"><h2>Waiting for player 2</h2></div>' +
                '<div id="mytable"><table id="stats"> <tr> <th>Played</th><th>Wins</th><th>Loses</th></tr>'+
                `<tr><td>${person.gamesplayed}</td><td>${person.wins}</td><td>${person.losses}</td></tr></table></div>` +
                '<br><a href="/logout">LOGOUT</a>';

                res.end(head+dash+_head);

        });
    }

    else{
      activeGamers.insert({player: "two" , name: req.session.user}, function(err){
          if(err) throw err;
          console.log("player 2 added to array!!!!!!");
        });

        gamers.findOne({user: req.session.user}, function(err, person){


          dash = '<h1>'+ req.session.user +'</h1>'+ '<br><p id="error">'+error+'</p>' +
                '<div class="messages"></div>'+
                '<div id="mytable"><table id="stats"> <tr> <th>Game Played</th><th>Wins</th><th>Loses</th>'+
                `<tr><td>${person.gamesplayed}</td><td>${person.wins}</td><td>${person.losses}</td></tr></table></div>` +
                '<br><a href="/logout">LOGOUT</a>' +
                `<br><form method="GET"><button formaction="/newGame"> Play with ${player.name}</button></form>`;

                res.end(head+dash+_head);

        });
    }

  });

});


app.get('/newGame', function(req, res){                             //request server to start a new game
  var cursor = activeGamers.find({});
  var allUsersArray = [];

  cursor.each(function(err, doc){
    if(err) throw err;

    if(doc != null){
      allUsersArray.push(doc);
    }

    else{
      var game =
`<div id="playerone"><h1>Player 1: ${allUsersArray[0].name}</h1><h2 id="point1"></h2></div>` +
`<div id="playertwo"><h1>Player 2: ${allUsersArray[1].name}</h1><h2 id="point2"></h2></div>` +
//making table now
'<div id="thegame"><h1>WAITING FOR PLAYER 1...'+

// '<table id="levelone"><tr><td onclick="change(event)" id="1.1.1"></td><td onclick="change(event)" id="1.1.2"></td><td onclick="change(event)" id="1.1.3"></td></tr><tr><td onclick="change(event)" id="1.2.1"></td><td onclick="change(event)" id="1.2.2"></td><td onclick="change(event)" id="1.2.3"></td></tr><tr><td onclick="change(event)" id="1.3.1"></td><td onclick="change(event)" id="1.3.2"></td><td onclick="change(event)" id="1.3.3"></td></tr></table>'+
//
// '<table id="leveltwo"><tr><td onclick="change(event)" id="2.1.1"></td><td onclick="change(event)" id="2.1.2"></td><td onclick="change(event)" id="2.1.3"></td></tr><tr><td onclick="change(event)" id="2.2.1"></td><td onclick="change(event)" id="2.2.2"></td><td onclick="change(event)" id="2.2.3"></td></tr><tr><td onclick="change(event)" id="2.3.1"></td><td onclick="change(event)" id="2.3.2"></td><td onclick="change(event)" id="2.3.3"></td></tr></table>'+
//
// '<table id="levelthree"><tr><td onclick="change(event)" id="3.1.1"></td><td onclick="change(event)" id="3.1.2"></td><td onclick="change(event)" id="3.1.3"></td></tr><tr><td onclick="change(event)" id="3.2.1"></td><td onclick="change(event)" id="3.2.2"></td><td onclick="change(event)" id="3.2.3"></td></tr><tr><td onclick="change(event)" id="3.3.1"></td><td onclick="change(event)" id="3.3.2"></td><td onclick="change(event)" id="3.3.3"></td></tr></table>'+

'</div>'+
//table ends here
'<div class="messages"></div>' +
'<form action="javascript:void(0)"><input type="text" id="message" required autofocus /></form>' +
'<br> <form method="GET" action="/quitGame"><button onclick="quit()" type="submit">Quit Game</button></form>';
      res.end(headGame+game+_headgametwo);

    }
  });

});

app.get('/newGame2', function(req, res){                             //request server to redirect player 1 to same page player 2 is on
  var cursor = activeGamers.find({});
  var allUsersArray = [];

  cursor.each(function(err, doc){
    if(err) throw err;

    if(doc != null){
      allUsersArray.push(doc);
    }

    else{
      var game =
`<div id="playerone"><h1>Player 1: ${allUsersArray[0].name}</h1><h2 id="point1"></h2></div>` +
`<div id="playertwo"><h1>Player 2: ${allUsersArray[1].name}</h1><h2 id="point2"></h2></div>` +
//making table now
'<div id="thegame">'+

'<table id="levelone"><tr><td onclick="change(event)" id="1.1.1"></td><td onclick="change(event)" id="1.1.2"></td><td onclick="change(event)" id="1.1.3"></td></tr><tr><td onclick="change(event)" id="1.2.1"></td><td onclick="change(event)" id="1.2.2"></td><td onclick="change(event)" id="1.2.3"></td></tr><tr><td onclick="change(event)" id="1.3.1"></td><td onclick="change(event)" id="1.3.2"></td><td onclick="change(event)" id="1.3.3"></td></tr></table>'+

'<table id="leveltwo"><tr><td onclick="change(event)" id="2.1.1"></td><td onclick="change(event)" id="2.1.2"></td><td onclick="change(event)" id="2.1.3"></td></tr><tr><td onclick="change(event)" id="2.2.1"></td><td onclick="change(event)" id="2.2.2"></td><td onclick="change(event)" id="2.2.3"></td></tr><tr><td onclick="change(event)" id="2.3.1"></td><td onclick="change(event)" id="2.3.2"></td><td onclick="change(event)" id="2.3.3"></td></tr></table>'+

'<table id="levelthree"><tr><td onclick="change(event)" id="3.1.1"></td><td onclick="change(event)" id="3.1.2"></td><td onclick="change(event)" id="3.1.3"></td></tr><tr><td onclick="change(event)" id="3.2.1"></td><td onclick="change(event)" id="3.2.2"></td><td onclick="change(event)" id="3.2.3"></td></tr><tr><td onclick="change(event)" id="3.3.1"></td><td onclick="change(event)" id="3.3.2"></td><td onclick="change(event)" id="3.3.3"></td></tr></table>'+

'</div>'+
//table ends here
'<div class="messages"></div>' +
'<form action="javascript:void(0)"><input type="text" id="message" required autofocus /></form>' +
'<br> <form method="GET" action="/quitGame"><button button onclick="quit()" type="submit">Quit Game</button></form>';
      res.end(headGame+game+_headgameone);

    }
  });

});

app.get('/quitGame', function(req, res){



  res.redirect('/dashboard');
});


app.get('/logout', function(req,res){                               //logs out user from current session ID

  activeGamers.deleteMany({});

  req.session.regenerate(function(err){
    req.flash('error','logged out');
    res.redirect('/login');
  });
});


app.get('/registerPage', function(req, res, next) {         //requests server for registration page
  var error = req.flash('error');
  var form = `<h1>Register</h1>
  <section>
    <form method="post" action="/register">
        <input type="text" id="uname" name="uname" placeholder="username" required /> <br />
        <input type="password" id="password" name="password" placeholder="password" required /> <br />
        <button >register</button>
    </form>
  </section>`;
  form = head + '<p id="error">'+error+'</p>'+ form + _head;

  res.writeHead(200, {
      "Content-Type": "text/html",
      'Content-Length': form.length
  });
  res.end(form);
});


app.post('/register', function(req, res, next) {                                  //creates new user in the database
  gamers.insert({user: req.body.uname, pass: req.body.password,wins:0,losses:0,gamesplayed:0}, function(err){
    if(err) throw err;
    console.log("new user was created!!!!!!");
  });
  req.session.user = req.body.uname;

  res.redirect("/dashboard");
});


//}



//app.listen(port);
console.log("app running on", port);
