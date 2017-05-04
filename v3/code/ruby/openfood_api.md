# OpenFood API version 3

## Ruby Example Code

### Product Endpoints

#### Get Product by ID ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/findProductById))

* Get a product whose ID you know (e.g. `2663`)
```ruby
# Sample Ruby code for a call against the OpenFood API v3 product by ID

# USAGE:
# $ ruby openfood_api.rb

require 'httparty'
require 'json'

BASE_URL='https://www.openfood.ch/api/v3'
KEY='API_KEY'
ID=2663
ENDPOINT="/products/#{ID}"

url = BASE_URL + ENDPOINT

headers = {
  'Authorization' => "Token token=#{KEY}",
  'Accept' => 'application/json',
  'Content-Type' => 'application/vnd.api+json',
  'Accept-Encoding' => 'gzip,deflate'
}

response = HTTParty.get(url, headers: headers)
puts "Status: #{response.code}"
if response.code == 200
  puts JSON.parse(response.body)['data']
end
```

#### List of Products ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/listProducts))

Supports query parameters for paging, filtering by barcode, and excluding fields that you may not interested in (such as `nutrients` or `ingredients_translations`).

* Get page 2 of all products, with 5 products per page
```ruby
# Sample Ruby code for a call against the OpenFood API v3 products listing, with paging

# USAGE:
# $ ruby openfood_api.rb

require 'httparty'
require 'json'

BASE_URL='https://www.openfood.ch/api/v3'
KEY='API_KEY'
ENDPOINT='/products'

url = BASE_URL + ENDPOINT

query = {
  'page[number]' => 2,
  'page[size]' => 5
}

headers = {
  'Authorization' => "Token token=#{KEY}",
  'Accept' => 'application/json',
  'Content-Type' => 'application/vnd.api+json',
  'Accept-Encoding' => 'gzip,deflate'
}

response = HTTParty.get(url, query: query, headers: headers)
puts "Status: #{response.code}"
if response.code == 200
  def page_number(link)
    link&.match(/\?.*page%5Bnumber%5D=(\d+)/)&.[](1) || 'unknown'
  end
  json = JSON.parse(response.body)
  curr_page = page_number(json['links']['self'])
  last_page = page_number(json['links']['last'])
  puts "Page: #{curr_page} / #{last_page}"
  puts 'Barcodes on this page:'
  json['data'].each do |product|
    puts "  #{product['barcode']}"
  end
end

```

#### Search for Products ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/searchProducts))

Advanced search using ElasticSearch Query DSL in the request data. See [the 'Search' section of the main API v3 README](/v3/README.md#search) for in depth explanations and example queries.

```ruby
# Sample Ruby code for a call against the OpenFood API v3 product _search

# USAGE:
# $ ruby openfood_api.rb

require 'httparty'
require 'json'

BASE_URL='https://www.openfood.ch/api/v3'
KEY='API_KEY'
ENDPOINT='/products/_search'

url = BASE_URL + ENDPOINT

query = <<~JSON
{
  "query": {
    "wildcard": {
      "_all_names" : "*toblerone*"
    }
  }
}
JSON

headers = {
  'Authorization' => "Token token=#{KEY}",
  'Accept' => 'application/json',
  'Content-Type' => 'application/vnd.api+json',
  'Accept-Encoding' => 'gzip,deflate'
}

response = HTTParty.post(url, headers: headers, body: query)
puts "Status: #{response.code}"
if response.code == 200
  results = JSON.parse(response.body)
  puts "Number of products found: #{results['hits']['total']}"
  puts 'First few products...'
  results['hits']['hits'].each do |hit|
    puts "  #{hit['_source']['display_name_translations']['en']}"
  end
end
```
