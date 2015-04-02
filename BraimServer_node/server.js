var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var mongojs = require('mongojs');
var ObjectId = mongojs.ObjectId
var db = mongojs('songbook_development',['emo_entries','users', 'emo_sessions', 'player_entries']);


io.on('connection', function(client){
	//console.log('Client connected..');
	client.on('register_session',function(session){
		session_json = JSON.parse(session)
		client.session_id = ObjectId(session_json["id"])
		client.user_id = ObjectId(session_json["user_id"])
		client.emit('init_capture')
		console.log("NEW sesion:"+client.session_id+"user:"+client.user_id)
	});

	client.on('disconnect',function(name){
	});

	client.on('store_emo_info',function(emo_values){
		if(emo_values!=null){
			emo_values["emo_session_id"] = client.session_id
			emo_values["user_id"] = client.user_id
			db.emo_entries.save(emo_values, function(){
				//console.log('entry saved');
			});
		}
	});

	client.on('store_player_info',function(player_values){
		if(player_values!=null){
			player_values["emo_session_id"] = client.session_id
			player_values["user_id"] = client.user_id
			db.player_entries.save(player_values, function(){
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