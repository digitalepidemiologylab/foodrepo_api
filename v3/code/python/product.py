"""
Sample Python 3.7 code for a call against Food Repo API v3 product by ID.

USAGE:
$ python3 product.py
"""

from pprint import pprint
import requests


BASE_URL = 'https://www.foodrepo.org/api/v3'
KEY = 'API_KEY'
PRODUCT_ID = 2663
ENDPOINT = '/products/{}'.format(PRODUCT_ID)

url = BASE_URL + ENDPOINT

headers = {
  'Authorization': 'Token token=' + KEY,
  'Accept': 'application/json',
  'Content-Type': 'application/vnd.api+json',
  'Accept-Encoding': 'gzip,deflate'
}

r = requests.get(url, headers=headers)
print('Status: {}'.format(r.status_code))
if r.status_code == 200:
    pprint(r.json())
