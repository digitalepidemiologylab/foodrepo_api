"""
Sample Python 2.7 code for a call against the OpenFood API products listing, with paging
Replace [API_KEY] with your API Key
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET  "https://www.openfood.ch/api/v2/products?page[number]=2&page[size]=3" -H 'Authorization: Token token="[API_KEY]"'

USAGE:
$ python openfood_api.py
"""

import requests

BASE_URL='https://www.openfood.ch/api/v3'
API_KEY='secret'

url = BASE_URL + '/products'

query = {
  "page[number]": "2",
  "size[size]": "5"
}

headers = {
  'Authorization': "Token token={}".format(API_KEY)
}

r = requests.get(url, params=query, headers=headers)
print r.status_code
print r.json()
