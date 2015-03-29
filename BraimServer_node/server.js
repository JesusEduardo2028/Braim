var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var mongojs = require('mongojs');
var db = mongojs('songbook_development',['emo_entries','users', 'emo_sessions', 'player_entries']);

var storeEmoData = function(data,session_date){
	//Add the message in the array
	db.emo_sessions.find({start_at: session_date},function(err,docs){
		session = docs[0];
		data["emo_session_id"] = session["_id"]
		db.emo_entries.save(data, function(){
			//console.log('entry saved');
		});
	});
	
}

var storePlayerData = function(data,session_date){
	//Add the message in the array
	db.emo_sessions.find({start_at: session_date},function(err,docs){
		session = docs[0];
		data["emo_session_id"] = session["_id"]
		db.player_entries.save(data, function(){
			//console.log('entry saved');
		});
	});
	
}


io.on('connection', function(client){
	//console.log('Client connected..');
	client.on('register_session',function(session){
		session_json = JSON.parse(session)
		client.session_id = session_json["id"];
		client.user_id = session_json["user_id"]
		console.log("NEW sesion:"+client.session_id+"user:"+client.user_id);
	});

	client.on('disconnect',function(name){
	});

	client.on('add_emo_info',function(emo_values){
		if(emo_values!=null){
			emo_values["email"] = client.email;
			emo_values
			storeEmoData(emo_values,client.session_date);
			//console.log(emo_values);
		}
	});

	client.on('add_player_info',function(player_values){
		if(player_values!=null){
			storePlayerData(player_values,client.session_date);
			//console.log(player_values);
		}
	});

});

server.listen(8080);

app.get('/',function(req,res){
	res.sendStatus(200)
});
app.get('/console',function(requ,res){
	res.sendFile(__dirname+'/index.html');
});
console.log('Listening on port 8080...');