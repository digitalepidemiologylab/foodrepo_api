![OpenFood Logo](../images/logo-foodrepo.svg?raw=true "FoodRepo")

# Food Repo API v3

## Overview

At Food Repo we believe in open data delivered using open source technologies and open web standards. The Food Repo API allows full access to all of the content available in the Food Repo database, including images.

For general information about our data license, how to contribute, and more, see [here](/README.md).

## Access

Our API is available to **anyone** using an [API key generated using their Food Repo account](https://www.foodrepo.org/users/me/api_keys). All API access is over HTTPS, and endpoints are subpaths of `https://www.foodrepo.org/api/v3`. All data is sent and received as JSON.

#### How to use your API key

Each request made against the Food Repo API must include your API key. The key must be passed in a Token header, like so:

```
Authorization: Token token="API_KEY"
```

## Endpoints

Once you have an API key, check out our <b>[Swagger Documentation page](https://www.foodrepo.org/api-docs/swaggers/v3)</b> which describes all our endpoints and their possible responses. You can perform live requests against our database, see exactly how to format your requests, and get a feel for what our API has to offer.

## Object Schemas

All endpoints returning a product (or products) will follow the [Product Object Schema](schema/product.md).

The `/products` endpoint provides a `links` object that follows the [Links Object Schema](schema/links.md).

## Search

The **_search** endpoint's functionality is provided through an ElasticSearch cluster. To perform searches, you'll need to familiarize yourself with a few things.

 * [ElasticSearch Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html)
 * [Product mapping](schema/es_mapping/product.md)
 * [Example queries](code/meta/es_sample_queries_product.md)

## Example Code

Example code for various languages may be found in this repo. Replace `API_KEY` with your own key where necessary.

* [Curl](code/curl/foodrepo_api.md)
* [JavaScript](code/javascript/)
* [Python](code/python/foodrepo_api.md)
* [Ruby](code/ruby/foodrepo_api.md)

## Misc

#### Compressed responses

If you are using our API a lot, that's great! But telling our endpoints that you can accept a compressed response could reduce the response payload size by a factor of 10! That translates to snappier apps for your users and cheaper bandwidth costs for everyone. To enable compression, simply include the following header in your request:

```
Accept-Encoding: gzip
```

<small>Note: Depending on the coding language or framework you are using to consume our API, you may need to do extra work to decompress the response.</small>

#### Contact and Contribution

We welcome any and all suggestions for improving and adding to the above documentation via Pull Request or [Issue log](https://github.com/salathegroup/foodrepo_api/issues).
