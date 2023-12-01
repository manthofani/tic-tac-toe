const url = 'http://localhost:8080';
let stompClient;
let gameId;
let playerType;

function connectToSocket(gameId) {

    console.log("connecting to the game");
    let socket = new SockJS(url + "/play");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/in-game/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log("display first time", data.board);
            displayResponse(data);
        })
    })
}

function startGame() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        $.ajax({
            url: url + "/match/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login,
                "dimension": document.getElementById("size").value,
            }),
            success: function (data) {
                createGrid()
                reset();
                gameId = data.matchID;
                playerType = 'X';
                connectToSocket(gameId);
                alert("Your created a game. Game id is: " + gameId);
                gameOn = true;
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}


function startRandom() {
    let login = document.getElementById("login2").value;
    if (login == null || login === '') {
        alert("Please enter player 2 name");
    } else {
        $.ajax({
            url: url + "/match/random",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                gameId = data.matchID;
                playerType = 'O';
                reset();
                createGrid(data.dimension);
                connectToSocket(gameId);
                alert("Congrats you're playing with: " + data.player1);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

function joinGame() {
    let login = document.getElementById("login2").value;
    if (login == null || login === '') {
        alert("Please enter player 2 Name");
    } else {
        let game_id = document.getElementById("game_id").value;
        if (game_id == null || game_id === '') {
            alert("Please enter game id");
        }
        $.ajax({
            url: url + "/match/connect",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "player": {
                    "login": login
                },
                "matchID": game_id
            }),
            success: function (data) {
                gameId = data.matchID;
                playerType = 'O';
                reset();
                createGrid(data.dimension);
                connectToSocket(gameId);
                alert("Congrats you're playing with: " + data.player1);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}


function createGrid(value) {


    var Container = document.getElementById("board-container");
  	Container.innerHTML = '';
    let numberOfRows = 0;
  	if (value === null || value === '' || value === undefined){
        numberOfRows = document.getElementById("size").value;
        dimension = numberOfRows * numberOfRows;
    } else {
        numberOfRows = value;
        dimension = numberOfRows * numberOfRows;
    }


    for (let i = 0; i < numberOfRows; i++) {
        let row = document.createElement("div");
        row.className = "row";
        for (let j = 0; j < numberOfRows; j++) {
          let cell = document.createElement("div");
          cell.addEventListener("click", (event) => handleClick(cell, i, j));
          cell.className = "cell";
          cell.id = i+"_"+j;
          row.appendChild(cell);
        }
        Container.appendChild(row);
      }
    
    board = new Array(parseInt(numberOfRows)).fill("").map(() => new Array(parseInt(numberOfRows)).fill(""));

}
      
