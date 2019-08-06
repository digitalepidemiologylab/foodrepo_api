# frozen_string_literal: true

# Sample Ruby code for a call against the OpenFood API v3 products listing, with paging

# USAGE:
# $ ruby product_list.rb

require 'httparty'
require 'json'

BASE_URL = 'https://www.foodrepo.org/api/v3'
KEY = 'API_KEY'
ENDPOINT = '/products'

url = BASE_URL + ENDPOINT

query = {
  'page[number]' => 2,
  'page[size]' => 5
}

headers = {
  Authorization: "Token token=#{KEY}",
  Accept: 'application/json',
  'Content-Type': 'application/vnd.api+json'
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
