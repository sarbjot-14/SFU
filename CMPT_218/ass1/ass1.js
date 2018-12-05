console.log("hello");

//Array with the solutions
var answerArray = new Array(0,0,1,0,0,1,0,0,0,0,0,0,0
                             ,1,1,0,1,1,0,1,0,0,1,1,1,1
                             ,1,1,0,1,1,1,0,1,0,1,1,1,0
                             ,0,0,1,1,0,1,0,1,1,0,1,0,0
                             ,0,1,1,1,1,1,0,1,1,0,0,0,0
                             ,0,0,0,1,0,1,0,1,1,0,0,0,1
                             ,0,0,0,1,0,0,0,1,1,0,0,0,1
                             ,0,0,1,1,0,1,0,0,1,0,0,0,0
                             ,1,0,1,1,0,1,0,0,1,0,0,1,1
                             ,0,0,0,1,0,1,1,0,0,0,1,1,0
                             ,0,1,1,0,0,1,1,0,0,0,1,1,1
                             ,1,1,1,1,1,1,1,1,1,0,0,0,0
                             ,0,0,1,0,1,1,0,0,0,0,0,0,0);


var cells = document.querySelectorAll('td'); // make array with all data cells
var len = cells.length; //find length of array of cells

//BUTTONS
function instructionsButton() { // button for instructions
    document.getElementById("instructions").innerHTML = "Solve the puzzle by clicking the cells so that the marked cells in the columns and rows match the number show on the top and left side of the table";
}

function resetButton(){ //to reset all cells
  //array.forEach(reset(x));// I DONT KNOW HOW TO USE THIS
  for (var x=0; x<len; x++){
    cells[x].style.backgroundColor= "white";
  }
  //document.querySelector('td').backgroundColor="white"; //THIS DOESNT WORK EITHER
}
function answer(){ //shows solution
  for(x in answerArray ){
    console.log(answerArray[x]);
    if(answerArray[x]==1){
      cells[x].style.backgroundColor= "black";
    }else if(answerArray[x]==0){
      cells[x].style.backgroundColor= "white";
    }
  }
}



 for (var i=0; i<len; i++){
   cells[i].style.backgroundColor= "white";
}

function check(){ // checks if the soluiton is correct after every click
  for (var x=0; x<len; x++){

    console.log(x);
    if((cells[x].style.backgroundColor=="black")&&(answerArray[x]==1)){
      console.log("correct");
    }
    else if((cells[x].style.backgroundColor=="white")&&(answerArray[x]==0)){
      console.log("correct");
    }
    else{
      console.log("///return 0//////");
      return 0;
    }
  }
  window.alert("Congratulations You Solved The Puzzle!!" );
  stop();
}



function change(jack){
  var x= jack.target;
  var a=0;

  if(x.style.backgroundColor=="black"){
    x.style.backgroundColor= "white"; //white
    check();
  }
  else{
    x.style.backgroundColor="black";
    check();
  }
}
//timer
var secs = 0;
var int;
var mins = 0;

function count(){
  console.log(secs);
  secs++;
  if(secs==59){
    secs=00;
    mins++;
    document.getElementById("mins").innerHTML = mins;
  }
  document.getElementById("secs").innerHTML = secs;
}
function stop(){
  clearInterval(int);
}
function reset(){
  document.getElementById("secs").innerHTML =00;
  document.getElementById("mins").innerHTML =00;
  secs=0;
  mins=0;
}
//var buttonly = document.getElementById("START");
function start(){
  int = setInterval(count,1000);
  //buttonly.style.visibility="none";
}
