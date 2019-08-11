package sample.ca.cmpt213.asn4.tictactoe.game;

public class GridState {
    public String playerTurn = "x" ;
    private String[][] ticTacArr;
    public boolean isGameOver = false;
    int numOfMoves = 0; //when moves is 16 and nobody won then it is a draw
    public boolean isTie = false;
    public GridState(){
        ticTacArr = new String[4][4];
        for(int i =0 ; i < 4 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                ticTacArr[i][j] = "blank";
            }
        }
    }
    public boolean isValidMove(int x, int y){

        if(ticTacArr[x][y] == "blank"){
            return true;
        }
        else{
            return false;
        }
    }

    public void setMove(int x , int y){
        numOfMoves++;
        System.out.println(numOfMoves);
        ticTacArr[x][y] = playerTurn;


        if(checkWin()){
            System.out.println(playerTurn + " wins");
        }
        else {
            if(playerTurn.equals("x")){
                playerTurn = "o";
            }
            else{
                playerTurn = "x";
            }

        }

    }

    public void clearGrid(){
        for(int i =0 ; i < 4 ; i++){
            for(int j = 0 ; j < 4 ; j++){
                ticTacArr[i][j] = "blank";
            }
        }
        numOfMoves = 0;
        isGameOver = false;
        isTie = false;
        numOfMoves = 0;
    }

    public boolean checkWin(){
        int needFour =0;


        //check rows
        for( int i = 0 ; i < 4 ; i++ ){
            for( int j = 0; j < 4 ; j++ ){
                if(ticTacArr[i][j] == playerTurn){
                    needFour++;
                }

                if(needFour == 4){
                    isGameOver = true;
                    return true;
                }
            }
            needFour = 0;
        }
        //check columns

        for( int i = 0 ; i < 4 ; i++ ){
            for( int j = 0; j < 4 ; j++ ){
                if(ticTacArr[j][i] == playerTurn){
                    needFour++;
                }

                if(needFour == 4){
                    isGameOver = true;
                    return true;
                }
            }
            needFour =0;
        }


        //check diagonal
        needFour = 0;
        for( int j = 0; j < 4 ; j++ ){
            //System.out.println("row and col " + j + " " + ticTacArr[j][j]);
            if(ticTacArr[j][j] == playerTurn){
                needFour++;
            }

            if(needFour == 4){
                isGameOver = true;
                return true;
            }
        }


        needFour = 0;
        int k = 3;
        for( int j = 0; j < 4 ; j++ ){


            if (ticTacArr[j][k] == playerTurn) {
                needFour++;
            }

            if (needFour == 4) {
                isGameOver = true;
                return true;
            }
            k--;

        }
        //System.out.println(numOfMoves);
        if(numOfMoves == 16){
            isGameOver = true;
            isTie = true;
        }

        return false;
    }

}
