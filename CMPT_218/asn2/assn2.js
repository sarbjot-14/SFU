console.log("hellllloo");

var i = 1;
var original = document.getElementById('duplicate0');

var arr = [];
var mario;
function newForm() {
    arr[i]= i;
    console.log("hey");


   console.log("double please");
    var clone = original.cloneNode(true);
    clone.id = "duplicate"+i;


    original.parentNode.appendChild(clone);
    document.getElementById("duplicate"+i).reset();
    i++;

}
function removeForm() {
  if(i!=1){
    i--;
    // Removes an element from the document
    var element = document.getElementById("duplicate"+i);
    element.parentNode.removeChild(element);
  }
}
