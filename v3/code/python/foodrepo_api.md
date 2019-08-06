# OpenFood API version 3

## Python Example Code

### Product Endpoints

#### Get Product by ID ([Try it!](https://www.foodrepo.org/api-docs/swaggers/v3#!/default/findProductById))

* Get a product whose ID you know (e.g. `2663`)
```python
"""
Sample Python 2.7 code for a call against Food Repo API v3 product by ID.

USAGE:
$ python product.py
"""
import requests

BASE_URL='https://www.foodrepo.org/api/v3'
KEY='API_KEY'
PRODUCT_ID=2663
ENDPOINT='/products/{}'.format(PRODUCT_ID)

url = BASE_URL + ENDPOINT

headers = {
  'Authorization': 'Token token=' + KEY,
  'Accept': 'application/json',
  'Content-Type': 'application/vnd.api+json',
  'Accept-Encoding': 'gzip,deflate'
}

r = requests.get(url, headers=headers)
print 'Status: ' + str(r.status_code)
if r.status_code == 200:
    print r.json()
```

#### List of Products ([Try it!](https://www.foodrepo.org/api-docs/swaggers/v3#!/default/listProducts))

Supports query parameters for paging, filtering by barcode, and excluding fields that you may not interested in (such as `nutrients` or `ingredients_translations`).

* Get page 2 of all products, with 5 products per page
```python
"""
Sample Python 2.7 code for a call against Food Repo API v3 products listing, with paging.

USAGE:
$ python product_listing.py
"""
import requests

BASE_URL='https://www.foodrepo.org/api/v3'
KEY='API_KEY'
ENDPOINT='/products'

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
print 'Status: ' + str(r.status_code)
if r.status_code == 200:
  print 'Page loaded successfully.'
  print 'Generated in ' + str(r.json().get('meta', {}).get('generated_in', -1)) + ' milliseconds.'
  print 'Next page\'s URL is: ' + r.json()['links'].get('next', 'UNKNOWN')
  print 'Barcodes on this page:'
  for product in r.json()['data']:
    print '  ' + product['barcode']
```

#### Search for Products ([Try it!](https://www.foodrepo.org/api-docs/swaggers/v3#!/default/searchProducts))

Advanced search using ElasticSearch Query DSL in the request data. See [the 'Search' section of the main API v3 README](/v3/README.md#search) for in depth explanations and example queries.

```python
"""
Sample Python 2.7 code for a call against the OpenFood API v3 product _search

USAGE:
$ python product_search.py

MORE INFO:
https://github.com/salathegroup/foodrepo_api/blob/master/v3/README.md#search
"""
import requests

BASE_URL='https://www.foodrepo.org /api/v3'
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
```
