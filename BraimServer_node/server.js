var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
var mongojs = require('mongojs');
var ObjectId = mongojs.ObjectId
var db = mongojs('braimdb',['epoc_entries', 'player_entries']);


io.on('connection', function(client){
	//console.log('Client connected..');
	client.on('register_braim_session',function(braim_session){
		braim_session_json = JSON.parse(braim_session)
		client.braim_session_id = ObjectId(braim_session_json["id"])
		client.user_id = ObjectId(braim_session_json["user_id"])
		client.emit('init_capture')
		console.log("NEW braim sesion:"+client.braim_session_id+"user:"+client.user_id)
	});

	client.on('disconnect',function(name){
	});

	client.on('store_epoc_info',function(epoc_values){
		if(epoc_values!=null){
			epoc_values["braim_session_id"] = client.braim_session_id
			epoc_values["user_id"] = client.user_id
			db.epoc_entries.save(epoc_values, function(){
				//console.log('epoc entry saved');
			});
		}
	});

	client.on('store_player_info',function(player_values){
		if(player_values!=null){
			player_values["braim_session_id"] = client.braim_session_id
			player_values["user_id"] = client.user_id
			db.player_entries.save(player_values, function(){
				//console.log('player entry saved');
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