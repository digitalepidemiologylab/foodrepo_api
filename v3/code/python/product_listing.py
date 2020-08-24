"""
Sample Python 3.7 code for a call against Food Repo API v3 products listing, with paging.

USAGE:
$ python3 product_listing.py
"""
import requests

BASE_URL = 'https://www.foodrepo.org/api/v3'
KEY = 'API_KEY'
ENDPOINT = '/products'

url = BASE_URL + ENDPOINT

query = {
  'page[number]': 2,
  'page[size]': 5
}

headers = {
  'Authorization': 'Token token=' + KEY,
  'Accept': 'application/json',
  'Content-Type': 'application/vnd.api+json',
  'Accept-Encoding': 'gzip,deflate'
}

r = requests.get(url, params=query, headers=headers)
print('Status: ' + str(r.status_code))
if r.status_code == 200:
    print('Page loaded successfully.')
    print('Generated in ' + str(r.json().get('meta', {}).get('generated_in', -1)) + ' milliseconds.')
    print('Next page\'s URL is: ' + r.json()['links'].get('next', 'UNKNOWN'))
    print('Barcodes on this page:')
    for product in r.json()['data']:
        print('  ' + product['barcode'])
