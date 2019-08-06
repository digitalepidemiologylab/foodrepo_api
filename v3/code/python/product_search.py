"""
Sample Python 2.7 code for a call against the OpenFood API v3 product _search

USAGE:
$ python product_search.py

MORE INFO:
https://github.com/salathegroup/foodrepo_api/blob/master/v3/README.md#search
"""
import requests

BASE_URL='https://www.foodrepo.org/api/v3'
KEY='API_KEY'
ENDPOINT='/products/_search'

url = BASE_URL + ENDPOINT

query = {
  "query": {
    "wildcard": {
      "_all_names" : "*toblerone*"
    }
  }
}

headers = {
  'Authorization': "Token token=" + KEY,
  'Accept': 'application/json',
  'Content-Type': 'application/vnd.api+json',
  'Accept-Encoding': 'gzip,deflate'
}

r = requests.post(url, json=query, headers=headers)
print 'Status: ' + str(r.status_code)
if r.status_code == 200:
    results = r.json()
    print 'Number of products found: ' + str(results['hits']['total'])
    print 'First few products...'
    for hit in results['hits']['hits']:
        print '  ' + hit['_source']['display_name_translations']['en']
