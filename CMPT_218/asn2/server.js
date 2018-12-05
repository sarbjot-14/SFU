var http = require('http');
var fs = require('fs');
var path = require('path');
var url = require('url');
var qs = require('querystring');

var server = http.createServer();
server.on('request', function(req,res){
  console.log('request:', req.url);
  var urlObj = url.parse(req.url, true); // true => query turned into an obj

  //GET REQUEST TO ROOT TO GET THE form.html
  var request = url.parse(req.url, true);
  var action = request.pathname;

	if (action === '/' & req.method== 'GET'){  //display form.html on get request
     var page = fs.readFileSync('./form.html');
     res.writeHead(200, {"Content-Type": "text/html"});
     res.write(page);
     res.end();
  }
  else if(req.url === '/ass2.css') {
                fs.readFile('ass2.css',function(err,data){
                  res.writeHead(200,{"Content-Type": "text/css"});
                  res.write(data);
                  res.end();
                });
  }
  else if(req.url === '/assn2.js') {
                fs.readFile('assn2.js',function(err,data){
                  res.writeHead(200,{"Content-Type": "text/js"});
                  res.write(data);
                  res.end();
                });
  }
  else if (req.method === 'POST'& action === '/'){  //add users when submmit a form
    console.log("post request to root");
    var body ='';
    req.on('data', function(data){
      body += data
    });
    req.on('end', function(){
      var postObj = qs.parse(body);
      //console.log("postObj"+postObj);
      var newUser = JSON.stringify(postObj);


    if(fs.existsSync(__dirname+"/data/users.json")){  //add user to prexisiting user array in jason file
      //var users = require('fs');
      if(fs.existsSync(__dirname+"/data/users.json")) {
        fs.readFile(__dirname+"/data/users.json", function(err, data){
          if (err){
              console.log(err);
          }
          else {
            var obj = JSON.parse(data);

          }
          obj.table.push(newUser);
          var newArray = JSON.stringify(obj);
          fs.writeFile(__dirname+"/data/users.json", newArray);
          });;
      }
      else{
        res.writeHead(200,{"Content-Type": "text/plain"});
        res.write("sorry no users to display");
        res.end();
      }
    }

    else{
      console.log("do this when creating file");  //create new array if one does not exist
      var obj = {
        table: []

      };
      obj.table.push(newUser);
      var newArray = JSON.stringify(obj);
      fs.writeFile(__dirname+"/data/users.json", newArray);
    }

    });
  }
else if(req.url == '/data/users.json' & req.method== 'GET'){ //display users

    if(fs.existsSync(__dirname+"/data/users.json")) {
      fs.readFile(__dirname+"/data/users.json",function(err,data){
        if (err){
            console.log(err);
        }
        res.writeHead(200,{"Content-Type": "text/json"});
        res.write(data);
        res.end();
      });
    }
    else{
      var obj = {
        table: []

      }
      var newArray = JSON.stringify(obj);
      fs.writeFile(__dirname+"/data/users.json", newArray);
      fs.readFile(__dirname+"/data/users.json",function(err,data){
        if (err){
            console.log(err);
        }
        res.writeHead(200,{"Content-Type": "text/json"});
        res.write(data);
        res.end();
      });

    }

}
else if(req.url == '/users.html' & req.method== 'GET'){ //display users in a table
  var attributes= 3;
    if(fs.existsSync(__dirname+"/data/users.json")) {
      fs.readFile(__dirname+"/data/users.json", function(err, data){
        if (err){
            console.log(err);
        }
        else {
          var obj = JSON.parse(data);

        }
      var usersNum= obj.table.length;

      //var theUser =JSON.parse(obj.table[0]);

      var attributes= 6;
      var html = buildHtml(obj, req);

      res.writeHead(200, {
        'Content-Type': 'text/html',
        'Content-Length': html.length

      });
      res.end(html);

      function buildHtml(obj,req) {


        var header = '<title>users.html</title><link rel="stylesheet" href="./users.css">';
      //  var body= '<p>' +theUser.fname+'</p>';
        var body = '<table ><tr><th>First Name</th><th>Last Name</th><th>Age</th><th>Email</th><th>Birthday</th><th>Fitness Level out of 10</th>';
        for(var r=0; r<usersNum; r++){
          body=body+'<tr>'
          var theUser =JSON.parse(obj.table[r]);
          for(var j=0; j<attributes; j++){
            //var theUser =JSON.parse(obj.table[0]);
            if(j==0){
              body=body+'<td>'+theUser.fname +'</td>';
            }
            if(j==1){
              body=body+'<td>'+theUser.lname +'</td>';
            }
            if(j==2){
              body=body+'<td>'+theUser.age +'</td>';
            }
            if(j==3){
              body=body+'<td>'+theUser.email +'</td>';
            }
            if(j==4){
              body=body+'<td>'+theUser.bdaymonth +'</td>';
            }
            if(j==5){
              body=body+'<td>'+theUser.points +'</td>';
            }


          }
          body=body+'</tr>';
        }
        // body=body+'<tr><td>'+ aName +'</td><td>'+'joe'+'</td><td>'+'joe'+'</td></tr>';
        body = body +'</table>'
        //body=body + '<p>'+'bobby'+'</p>';
        return '<!DOCTYPE html>'
             + '<html><header>' + header + '</header><body>' + body + '</body></html>';
      };


    })}
    else{
      console.log("hola");
    var html = buildHtml(req);
    res.writeHead(200, {
      'Content-Type': 'text/html',
      'Content-Length': html.length

    });
    res.end(html);

    function buildHtml(req) {
      var header = '<title>users.html</title><link rel="stylesheet" href="./users.css">';
    var body = '<table ><tr><th>First Name</th><th>Last Name</th><th>Age</th><th>Email</th><th>Birthday</th><th>Fitness Level out of 10</th>';
    return '<!DOCTYPE html>'
         + '<html><header>' + header + '</header><body>' + body + '</body></html>';
    }
  }

}
else if(req.url === '/users.css') {

    fs.readFile('users.css',function(err,data){
      res.writeHead(200,{"Content-Type": "text/css"});
      res.write(data);
      res.end();
    });
}
else if(req.url === '/deleteAll') { //delete users in jason file
  if(fs.existsSync(__dirname+"/data/users.json")) {
    console.log("found it");
    fs.unlink(__dirname+"/data/users.json", function(error) {
    if (error) {
        throw error;
    }

    });

  }

}
else if(req.url === '/update') { //update user informaation
  if(fs.existsSync(__dirname+"/data/users.json")) {
    //get the new user information
    var body ='';
    req.on('data', function(data){
      body += data
    });
    req.on('end', function(){
      var newUser = qs.parse(body);




    // get oldInfo to update
    fs.readFile(__dirname+"/data/users.json", function(err, data){
      if (err){
          console.log(err);
      }
      else {
        var oldInfo = JSON.parse(data);



      }
    var usersNum= oldInfo.table.length;
    var attributes= 6;

  //for loop to find target users
  for(var r=0; r< usersNum; r++){

    var oldUser = JSON.parse(oldInfo.table[r]);
    //console.log(oldUser);

    if(newUser.fname==oldUser.fname){
      console.log("omg");
      if(newUser.lname!=''){

        oldUser.lname = newUser.lname;
      }
      if(newUser.age!=''){
        oldUser.age = newUser.age;
      }
      if(newUser.email!=''){
        console.log("new email")
        oldUser.email = newUser.email;
      }
      if(newUser.bdaymonth!=''){
        oldUser.bdaymonth = newUser.bdaymonth;
      }
      if(newUser.points!=''){
        oldUser.points = newUser.points;
      }

      oldInfo.table[r]=JSON.stringify(oldUser);
      //oldInfo.table[r]=oldUser;
      var newArray = JSON.stringify(oldInfo);
      fs.writeFile(__dirname+"/data/users.json", newArray);
     break;
    }
  }
  })
  })

}
}
else if (req.method === 'POST'& req.url === '/deleteUser'){  //add users when submmit a form
  console.log("here we go");
  if(fs.existsSync(__dirname+"/data/users.json")) {
    //get the new user information
    var body ='';
    req.on('data', function(data){
      body += data
    });
    req.on('end', function(){
      var newUser = qs.parse(body);

      console.log("newUser.fname "+newUser.fname);



    // get oldInfo to update
    fs.readFile(__dirname+"/data/users.json", function(err, data){
      if (err){
          console.log(err);
      }
      else {
        var oldInfo = JSON.parse(data);
        var one = JSON.parse(oldInfo.table[0]);
        console.log("look for fname"+one.fname);


      }
    var usersNum= oldInfo.table.length;
    var attributes= 6;

  //for loop to find target users
  for(var r=0; r< usersNum; r++){

    var oldUser = JSON.parse(oldInfo.table[0]);
    console.log(oldUser);
    if(newUser.fname==oldUser.fname){
      console.log("omg");
      oldInfo.table.splice(r,1);
      var newArray = JSON.stringify(oldInfo);
      fs.writeFile(__dirname+"/data/users.json", newArray);
     break;
    }
  }
})
})
}
}

else{
  //console.log("error?");
  res.writeHead(404);
  res.write("error page not found");
  res.end();
}
}

);


server.listen(12398);
console.log('Magic is happening on port 12398');
