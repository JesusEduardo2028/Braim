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
		
	});
	
}

var storePlayerData = function(data,session_date){
	//Add the message in the array
	db.emo_sessions.find({start_at: session_date},function(err,docs){
		session = docs[0];
		data["emo_session_id"] = session["_id"]
		
	});
	
}


io.on('connection', function(client){
	//console.log('Client connected..');
	client.on('register_session',function(session){
		session_json = JSON.parse(session)
		client.session_id = session_json["id"]
		client.user_id = session_json["user_id"]
		console.log("NEW sesion:"+client.session_id+"user:"+client.user_id)
	});

	client.on('disconnect',function(name){
	});

	client.on('store_emo_info',function(emo_values){
		if(emo_values!=null){
			emo_values["session_id"] = client.session_id
			emo_values["user_id"] = client.user_id
			db.emo_entries.save(emo_values, function(){
				//console.log('entry saved');
			});
		}
	});

	client.on('store_player_info',function(player_values){
		if(player_values!=null){
			player_values["session_id"] = client.session_id
			player_values["user_id"] = client.user_id
			db.player_entries.save(data, function(){
				//console.log('entry saved');
			});
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