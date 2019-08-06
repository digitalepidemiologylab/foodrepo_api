# frozen_string_literal: true

# Sample Ruby code for a call against the Food Repo API v3 product _search

# USAGE:
# $ ruby product_search.rb

require 'httparty'
require 'json'

BASE_URL = 'https://www.foodrepo.org/api/v3'
KEY = '91d904ca64a4872c232843c895b7d20c'
ENDPOINT = '/products/_search'

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
  Authorization: "Token token=#{KEY}",
  Accept: 'application/json',
  'Content-Type': 'application/vnd.api+json'
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
