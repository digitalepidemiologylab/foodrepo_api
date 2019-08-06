![The Open Food Repo Logo](../images/logo-foodrepo.svg?sanitize=true "Food Repo")

## Food Repo API v2

## Deprecation notice

API version 2 is now deprecated. Please consider using [version 3 of our API](/v3/README.md).

## Overview

At Food Repo we believe in open data delivered using open source technologies and open web standards. The Food Repo API allows full access to all of the content available in the Food Repo database, including images.

For general information about our data license, how to contribute, and more, see [here](/README.md).

## Schema

All API access is over HTTPS, and accessed from ```https://www.foodrepo.org```. All data is sent and received as JSON.

The Food Repo API is based on the [JSON API Specification](http://jsonapi.org/) for resources access, while the search functionality is provided through an ElasticSearch cluster. The search endpoints use ElasticSearch query language.

## Authentication

### Developer API Key

Sign up for a developer account on [Food Repo](https://www.foodrepo.org) and visit the *API Keys* page. This key must be passed with all requests, in the header.

You may request multiple API keys, and they may be deactivated or deleted at anytime by you. Once a key is deactivated or deleted it cannot be reused.

### Provide the API key in the Request headers

Each request made against the Food Repo API must include your API key. The key must be passed against a Token header, like:

```
Authorization: Token token="[API_KEY]"
```

All traffic accessing OpenFood must travel across HTTPS.

## Swagger Documentation

The API is documented via the [OpenAPI Specification](https://www.openapis.org/) using a [Swagger](http://swagger.io/) interface. This may be found on our website's [API v2 documentation page](https://www.foodrepo.org/api-docs/swaggers/v2).

The documentation may be freely viewed without an account. Using your developer API Key, you can run live queries against the API and view the generated curl statements.

Endpoints examples can be found [here](code/curl/foodrepo_api.md)


## Search Endpoints

Search endpoints, which contain the **_search** string are served using ElasticSearch. As such the full [ElasticSearch Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html) may be applied to Food Repo data.

Examples of search can be found [here](code/curl/foodrepo_api.md)


## Example Code

Example code for various languages may be found in this repo. Replace [API Key] with your own key where necessary. We currently support examples for

  - [Curl](code/curl/foodrepo_api.md)
  - [JavaScript](code/js/index.html)
  - [Python](code/python/foodrepo_api.py)
  - [Ruby](code/ruby/foodrepo_api.rb)
  - [Android](code/android/)
