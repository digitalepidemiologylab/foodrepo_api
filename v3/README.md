![OpenFood Logo](/images/OpenFood.png?raw=true "OpenFood")

# OpenFood API v3

## Overview

At OpenFood we believe in open data delivered using open source technologies and open web standards. The OpenFood API allows full access to all of the content available in the OpenFood database, including images.

For general information about our data license, how to contribute, and more, see [here](/README.md).

## Access

Our API is available to **anyone** using an [API key generated using their OpenFood account](https://www.openfood.ch/users/me/api_keys). All API access is over HTTPS, and endpoints are subpaths of `https://openfood.ch/api/v3`. All data is sent and received as JSON.

#### How to use your API key

Each request made against the OpenFood API must include your API key. The key must be passed in a Token header, like so:

```
Authorization: Token token="API_KEY"
```

## Endpoints

Once you have an API key, check out our <b>[Swagger Documentation page](https://www.openfood.ch/api-docs/swaggers/v3)</b> which describes all our endpoints and their possible responses. You can perform live requests against our database, see exactly how to format your requests, and get a feel for what our API has to offer.

## Object Schemas

All endpoints returning a product (or products) will follow the [Product Object Schema](schema/product.md).

The `/products` endpoint provides a `links` object that follows the [Links Object Schema](schema/links.md).

## Search

The **_search** endpoint's functionality is provided through an ElasticSearch cluster. To perform searches, you'll need to familiarize yourself with a few things.

 * [ElasticSearch Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html)
 * [Product mapping](schema/es_mapping/product.md)
 * [Example queries](code/meta/es_sample_queries_product.md)

## Example Code

Example code for various languages may be found in this repo. Replace `API_KEY` with your own key where necessary. We currently don't have any examples written for API v3, but our [API v2 examples](/v2/code) should be a good starting point. Check back soon!
