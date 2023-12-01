var turn = 0;
var gameOn = false;
var board = new Array(parseInt(3)).fill("").map(() => new Array(parseInt(3)).fill(""));
var dimension = 0;


function makeAMove(type, xCoordinate, yCoordinate) {
    $.ajax({
        url: url + "/match/play",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "type": type,
            "coordinateX": xCoordinate,
            "coordinateY": yCoordinate,
            "matchID": gameId
        }),
        success: function (data) {
            gameOn = false;
        },
        error: function (error) {
            console.log(error);
        }
    })
}


function displayResponse(data) {
    let board = data.board;
    let mark='';
    let totalDimension=0;

    for (let i = 0; i < board.length; i++) {
        for (let j = 0; j < board[i].length; j++) {
            if (board[i][j] === 1) {
                mark = 'X'
            } else if (board[i][j] === 2) {
                mark = 'O';
            } else {
                mark = '';
            }
            let id = i + "_" + j;
            document.getElementById(id).innerHTML = mark;
        }
    }
    if (data.winner != null) {
        alert("Winner is " + data.winner);
    }
    turn++;

    totalDimension = data.dimension * data.dimension;
    if (totalDimension === turn){
        alert ("GAME IS DRAW !");
        gameOn = false;
        return;
    }

    gameOn = true;


}

function handleClick(div, x, y){
    makeAMove(playerType, x, y);

}

function reset(){
    turn = 0;
}




