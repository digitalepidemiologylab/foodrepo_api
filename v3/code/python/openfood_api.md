# OpenFood API version 3

## Python Example Code

### Product Endpoints

#### Get Product by ID ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/findProductById))

* Get a product whose ID you know (e.g. `2663`)
```python
"""
Sample Python 2.7 code for a call against OpenFood API v3 product by ID.

Replace 'secret' with your API Key

USAGE:
$ python openfood_api.py
"""
import requests

BASE_URL='https://www.openfood.ch/api/v3'
API_KEY='secret'
PRODUCT_ID=2663
ENDPOINT='/products/{}'.format(PRODUCT_ID)

url = BASE_URL + ENDPOINT

headers = {
  'Authorization': 'Token token=' + API_KEY,
  'Accept': 'application/vnd.api+json',
  'Content-Type': 'application/vnd.api+json',
  'Accept-Encoding': 'gzip,deflate'
}

r = requests.get(url, headers=headers)
print 'Status: ' + str(r.status_code)
if r.status_code == 200:
    print r.json()
```

#### List of Products ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/listProducts))

Supports query parameters for paging, filtering by barcode, and excluding fields that you may not interested in (such as `nutrients` or `ingredients_translations`).

* Get page 2 of all products, with 5 products per page
```python
"""
Sample Python 2.7 code for a call against OpenFood API v3 products listing, with paging.

Replace 'secret' with your API Key

USAGE:
$ python openfood_api.py
"""
import requests

BASE_URL='https://www.openfood.ch/api/v3'
API_KEY='secret'
ENDPOINT='/products'

url = BASE_URL + ENDPOINT

query = {
  "page[number]": "2",
  "page[size]": "5"
}

headers = {
  'Authorization': 'Token token=' + API_KEY,
  'Accept': 'application/vnd.api+json',
  'Content-Type': 'application/vnd.api+json',
  'Accept-Encoding': 'gzip,deflate'
}

r = requests.get(url, params=query, headers=headers)
print 'Status: ' + str(r.status_code)
if r.status_code == 200:
    print 'Barcodes on this page:'
    for product in r.json()['data']:
      print '  ' + product['barcode']
```

#### Search for Products ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/searchProducts))

Advanced search using ElasticSearch Query DSL in the request data. See [the 'Search' section of the main API v3 README](/v3/README.md#search) for in depth explanations and example queries.

```python
"""
Sample Python 2.7 code for a call against the OpenFood API v3 product _search

Replace 'secret' with your API Key

USAGE:
$ python openfood_api.py

MORE INFO:
https://github.com/salathegroup/openfood_api/blob/master/v3/README.md#search
"""
import requests

BASE_URL='https://www.openfood.ch/api/v3'
API_KEY='secret'
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
  'Authorization': "Token token=" + API_KEY,
  'Accept': 'application/vnd.api+json',
  'Content-Type': 'application/vnd.api+json'
}

r = requests.post(url, json=query, headers=headers)
print 'Status: ' + str(r.status_code)
if r.status_code == 200:
    results = r.json()
    print 'Number of products found: ' + str(results['hits']['total'])
    print 'First few products...'
    for hit in results['hits']['hits']:
        print '  ' + hit['_source']['display_name_translations']['en']

```
