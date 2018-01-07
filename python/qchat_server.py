import os
from flask import Flask, request, jsonify, abort, make_response, Response
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.orm.exc import NoResultFound
from sqlalchemy import or_
from sqlalchemy import update
from database_setup import Base, User, Message, Friend
from firebase import _get_access_token
import send_to_firebase

app = Flask(__name__)

engine = create_engine('db uri')
Base.metadata.bind = engine

DBSession = sessionmaker(bind = engine)
session = DBSession()

token = 'secret token'

@app.route(token + '/users/all')
def getUsers():
	users = session.query(User).all()
	return jsonify(users = [user.serialize for user in users])


@app.route(token + '/users/<int:user_id>')
def getUser(user_id):
	try:
		user = session.query(User).filter_by(id = user_id).one()
		return jsonify(user.serialize)
	except NoResultFound:
		abort(make_response(jsonify({"user_id" : user_id, "error_code" : 404, "error_message" : "NOT_FOUND"}), 404))



@app.route(token + '/user/new', methods = ['POST'])
def addUser():
	userJson = request.get_json()
	existUser = session.query(User).filter_by(token_auth = userJson.get('token_auth')).first()
	if existUser is None:
		user = User(token_fcm = userJson.get('token_fcm'), eMail = userJson.get('eMail'), 
			name = userJson.get('name'), token_auth = userJson.get('token_auth'), image = userJson.get('image'))
		session.add(user)
		session.commit()		
		return jsonify(user.serialize)

	existUser.token_fcm = userJson.get('token_fcm')
	session.add(existUser)
	session.commit
	
	return jsonify(existUser.serialize)

@app.route(token + '/user/update', methods = ['PUT'])
def updateUser():
	userJson = request.get_json()
	user = session.query(User).filter_by(token_auth = userJson.get('token_auth')).one()
	user.token_fcm = userJson.get('token_fcm')
	session.add(user)
	session.commit()
	
	return jsonify(user.serialize)


@app.route(token + '/users/all/delete', methods = ['DELETE'])
def deleteUsers():
	rows = session.query(User).delete()
	session.commit()
	return jsonify({'delete users' : rows})



@app.route(token + '/users/<int:user_id>/message/new', methods = ['POST'])
def addMessage(user_id):

	messageJson = request.get_json()

	user = session.query(User).filter_by(id = user_id).one()


	message = Message(client_id = messageJson.get('client_id'), message = messageJson.get('message'),
		date = messageJson.get('date'), writer_id = messageJson.get('writer_id'), 
		receiver_id = messageJson.get('receiver_id'), is_sent = messageJson.get('is_sent'), 
		is_read = messageJson.get('is_read'), out = messageJson.get('out'), owner = messageJson.get('owner'))
	session.add(message)
	session.commit()

	dataMessage = {"message" :
	{"data" : {
	"client_id" : message.client_id,
	"message_id" : str(message.message_id),
	"message" : message.message,
	"date" : str(message.date),
	"writer_id" : str(message.writer_id),
	"receiver_id" : str(message.receiver_id),
	"is_sent" : str(message.is_sent),
	"is_read" : str(message.is_read),
	"out" : str(message.out),
	"owner" : message.owner
	},
	"android" : {
	"priority" : "high"
	},
	"token" : user.token_fcm}
	}

	status = send_to_firebase.send_request_fcm(dataMessage)

	is_sent = False

	if (status == 200):
		message.is_sent = True
		session.add(message)
		session.commit()
		is_sent = True
	
	return jsonify(message.serialize)


@app.route(token + '/messages/all')
def getMessages():
	messages = session.query(Message).all()
	return jsonify(messages = [message.serialize for message in messages])


@app.route(token + '/users/<int:user_id>/messages/all')
def getUserMessages(user_id):
	query = request.args.get('is_sent')

	if query is not None:
		messages = session.query(Message).filter(Message.receiver_id == user_id).filter_by(is_sent = query).distinct('message_id').all()
		for message in messages:
			message.is_sent = True			
			session.add(message)
			session.commit		
		return jsonify(messages = [message.serialize for message in messages])

	messages = session.query(Message).filter(or_(Message.writer_id == user_id, Message.receiver_id == user_id)).all()
	return jsonify(messages = [message.serialize for message in messages])


@app.route(token + '/users/<int:user_id>/friends/new', methods = ['POST'])
def addFriend(user_id):
	friendJson = request.get_json()

	user = session.query(User).filter_by(id = user_id).first()	

	existFriend = user.friends.filter_by(friend_id = friendJson.get('friend_id')).first()

	if existFriend is None:
		userFriend = session.query(User).filter_by(id = friendJson.get('friend_id')).first()
		friend = Friend(user_id = friendJson.get('user_id'), friend_id = friendJson.get('friend_id'),
			name = userFriend.name, image = userFriend.image, user_name = userFriend.user_name, dialog = friendJson.get('dialog'))
		session.add(friend)
		user.friends.append(friend)
		session.commit
		return jsonify(friend.serialize)

	return jsonify(existFriend.serialize)

@app.route(token + '/users/<int:user_id>/friends/all')
def getUserFriends(user_id):
	user = session.query(User).filter_by(id = user_id).first()
	friends = user.friends

	return jsonify(friends = [friend.serialize for friend in friends])


if __name__ == '__main__':
	port = int(os.environ.get('PORT', 5000))
	app.run(host='0.0.0.0', port=port)