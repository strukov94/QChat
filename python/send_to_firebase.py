import requests
import json
from firebase import _get_access_token

def send_request_fcm(data):
	headers = {'Authorization': 'Bearer ' + _get_access_token(),'Content-Type': 'application/json; UTF-8',}	
	r = requests.post('https://fcm.googleapis.com/v1/projects/your firebase project name/messages:send', 
		data = json.dumps(data), headers = headers)
	return r.status_code