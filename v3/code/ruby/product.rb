# frozen_string_literal: true

# Sample Ruby code for a call against the Food Repo API v3 product by ID

# USAGE:
# $ ruby product.rb

require 'httparty'
require 'json'

BASE_URL = 'https://www.foodrepo.org/api/v3'
KEY = 'API_KEY'
ID = 2663
ENDPOINT = "/products/#{ID}"

url = BASE_URL + ENDPOINT

headers = {
  Authorization: "Token token=#{KEY}",
  Accept: 'application/json',
  'Content-Type': 'application/vnd.api+json'
}

response = HTTParty.get(url, headers: headers)
puts "Status: #{response.code}"
puts puts JSON.parse(response.body)['data'] if response.code == 200
