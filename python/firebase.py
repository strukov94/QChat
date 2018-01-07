from oauth2client.service_account import ServiceAccountCredentials
import sys
import os

def _get_access_token():
	path = os.path.join(sys.path[0], 'your secret json')
	credentials = ServiceAccountCredentials.from_json_keyfile_name(path, 'https://www.googleapis.com/auth/firebase.messaging')
	access_token_info = credentials.get_access_token()
	return access_token_info.access_token