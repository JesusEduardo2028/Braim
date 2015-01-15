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

var createEmoSession = function(email,client){
	db.users.find({email:email},function(err,docs){
		user = docs[0]
		session = {
			start_at: new Date().getTime(),
			user_id: user['_id']
		}

		db.emo_sessions.save(session,function(){
			console.log('session saved!');
			client.session_date = session["start_at"]
		});
	});
}

io.on('connection', function(client){
	//console.log('Client connected..');
	client.on('join',function(email){
		client.email = email;
		console.log(email+" join in to the app");
		client.emit('user added', email);
		createEmoSession(email,client);
	});

	client.on('disconnect',function(name){
	});

	client.on('add_emo_info',function(emo_values){
		if(emo_values!=null){
			emo_values["email"] = client.email;
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