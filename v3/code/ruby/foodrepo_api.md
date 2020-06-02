![The Open Food Repo Logo](../../../images/logo-foodrepo.svg?sanitize=true "Food Repo")

# OpenFood API version 3

## Ruby Example Code

Click any example herein to expand the code.

### Product Endpoints

#### Get Product by ID ([Try it!](https://www.foodrepo.org/api-docs/swaggers/v3#!/default/findProductById))

<details><summary>Get a product whose ID you know (e.g. `2663`)</summary>
  
```ruby
# Sample Ruby code for a call against the Food Repo API v3 product by ID

# USAGE:
# $ ruby product.rb

require 'httparty'
require 'json'

BASE_URL='https://www.foodrepo.org/api/v3'
KEY='API_KEY'
ID=2663
ENDPOINT="/products/#{ID}"

url = BASE_URL + ENDPOINT

headers = {
  'Authorization' => "Token token=#{KEY}",
  'Accept' => 'application/json',
  'Content-Type' => 'application/vnd.api+json'
}

response = HTTParty.get(url, headers: headers)
puts "Status: #{response.code}"
if response.code == 200
  puts JSON.parse(response.body)['data']
end
```
</details>

#### List of Products ([Try it!](https://www.foodrepo.org/api-docs/swaggers/v3#!/default/listProducts))

Supports query parameters for paging, filtering by barcode, and excluding fields that you may not interested in (such as `nutrients` or `ingredients_translations`).

<details><summary>Get page 2 of all products, with 5 products per page</summary>
  
```ruby
# Sample Ruby code for a call against the Food Repo API v3 products listing

# USAGE:
# $ ruby product_list.rb

require 'httparty'
require 'json'

BASE_URL='https://www.foodrepo.org/api/v3'
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
  'Content-Type' => 'application/vnd.api+json'
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
</details>

<details><summary>Scrape all products from our database</summary>

```ruby
# Sample Ruby code for multiple calls against the Food Repo API v3 products listing, with paging

require 'httparty'
require 'json'

BASE_URL = 'https://www.foodrepo.org/api/v3'
KEY = YOUR_API_KEY_HERE
ENDPOINT = '/products'
DESTINATION = 'foodrepo_products_snapshot.json'
START_TIME = Time.now

url = BASE_URL + ENDPOINT

query = {
  'page[number]' => 1,
  'page[size]' => 200
}

headers = {
  'Authorization' => "Token token=#{KEY}",
  'Accept' => 'application/json',
  'Content-Type' => 'application/vnd.api+json'
}

products = []
num_queries = 0

puts 'Fetching products from FoodRepo API...'

loop do
  response = HTTParty.get(url, query: query, headers: headers)
  num_queries += 1
  raise unless response.code == 200 # HTTP OK
  json = JSON.parse(response.body)
  products += json['data']
  print "\rRetrieved products so far: #{products.length}..."
  url = json['links']['next']
  query = nil
  break if url.nil?
end

puts "\n#{products.length} products fetched from #{num_queries} queries."

File.open(DESTINATION, 'w') do |file|
  file << JSON.unparse({
    source: 'foodrepo',
    date: Time.now.utc,
    products: products
  })
end

puts "Data successfully written to: #{DESTINATION}"
puts "Time taken: #{(Time.now - START_TIME).round(1)} seconds"
```

The above essentially pages through our DB, 100 products at a time, until all products are retrieved, and stores the results into a single json file. Running this code on 2020-06-02 yielded the following output:

> Fetching products from FoodRepo API...
<br>Retrieved products so far: 43027...
<br>43027 products fetched from 216 queries.
<br>Data successfully written to: foodrepo_products_snapshot.json
<br>Time taken: 181.4 seconds

</details>


#### Search for Products ([Try it!](https://www.foodrepo.org/api-docs/swaggers/v3#!/default/searchProducts))

Advanced search using ElasticSearch Query DSL in the request data. See [the 'Search' section of the main API v3 README](/v3/README.md#search) for in depth explanations and example queries.

<details><summary>Find some Toblerones</summary>

```ruby
# Sample Ruby code for a call against the Food Repo API v3 product _search

# USAGE:
# $ ruby product_search.rb

require 'httparty'
require 'json'

BASE_URL='https://www.foodrepo.org/api/v3'
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
  'Content-Type' => 'application/vnd.api+json'
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
</details>
