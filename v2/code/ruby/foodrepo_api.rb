# frozen_string_literal: true

# Sample Ruby code for a call against the OpenFood API products listing, with paging
# Replace [API_KEY] with your API Key
# curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET  "https://foodrepo.org/api/v2/products?page[number]=2&page[size]=3" -H 'Authorization: Token token="[API_KEY]"'

# USAGE:
# $ ruby products.rb

require 'httparty'
require 'json'

BASE_URL = 'https://www.foodrepo.org/api/v2'
API_KEY = '91d904ca64a4872c232843c895b7d20c'

url = "#{BASE_URL}/products"

query = {
  'page[number]' => '2',
  'page[size]' => '5'
}

headers = {
  Authorization: "Token token=#{API_KEY}",
  Accept: 'application/vnd.api+json',
  'Content-Type': 'application/vnd.api+json'
}

response = HTTParty.get(url, query: query, headers: headers, debug_output: $stdout)
puts response.body
