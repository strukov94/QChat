import sys

from sqlalchemy import Column, ForeignKey, Integer, String, BigInteger, Boolean
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import create_engine

Base = declarative_base()

class User(Base):
	__tablename__ = 'user'

	id = Column(Integer, primary_key = True)
	token_fcm = Column(String, nullable = False)
	token_auth = Column(String, nullable = True)
	eMail = Column(String, nullable = False)
	name = Column(String, nullable = False)
	user_name = Column(String, nullable = True)
	image = Column(String, nullable = True)
	messages = relationship("Message", lazy='dynamic')
	friends = relationship("Friend", lazy='dynamic')

	@property
	def serialize(self):        
		return {
			'id': self.id,
			'token_fcm': self.token_fcm,
			'token_auth' : self.token_auth,
			'eMail' : self.eMail,
			'name' : self.name,
			'user_name' : self.user_name,
			'image' : self.image,
		}

class Message(Base):
	__tablename__ = 'message'

	message_id = Column(Integer, primary_key = True)
	client_id = Column(String, nullable = False)
	message = Column(String, nullable = False)
	date = Column(BigInteger, nullable = False)
	writer_id = Column(Integer, ForeignKey('user.id'))
	receiver_id = Column(Integer, nullable = False)
	is_sent = Column(Boolean, nullable = True)
	is_read = Column(Boolean, nullable = True)
	out = Column(Boolean, nullable = True)
	owner = Column(String, nullable = False)

	@property
	def serialize(self):        
		return {
			'message_id': self.message_id,
			'client_id': self.client_id,
			'message' : self.message,
			'date' : self.date,
			'writer_id' : self.writer_id,
			'receiver_id' : self.receiver_id,
			'is_sent' : self.is_sent,
			'is_read' : self.is_read,
			'out' : self.out,
			'owner' : self.owner,
		}


class Friend(Base):
	__tablename__ = 'friend'

	id = Column(Integer, primary_key = True)
	friend_id = Column(Integer, nullable = False)
	user_id = Column(Integer, ForeignKey('user.id'))
	name = Column(String, nullable = False)
	image = Column(String, nullable = True)
	user_name = Column(String, nullable = True)
	dialog = Column(String, nullable = False)

	@property
	def serialize(self):
		return {
			'id' : self.id,
			'friend_id' : self.friend_id,
			'user_id' : self.user_id,
			'name' : self.name,
			'image' : self.image,
			'user_name' : self.user_name,
			'dialog' : self.dialog,
		}

engine = create_engine('db uri')
Base.metadata.create_all(engine)