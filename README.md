![OpenFood Logo](images/OpenFood.png?raw=true "OpenFood")

## OpenFood API

## Overview

At OpenFood we believe in open data delivered using open source technologies and open web standards. The OpenFood API allows full access to all of the content available in the OpenFood database, including images.

## License

All content, including images, is licensed under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/)


### Current Version

By default, all requests receive the v2 version of the API.

### Schema

All API access is over HTTPS, and accessed from ```https://openfood.ch```. All data is sent and received as JSON.

The OpenFood API is based on the [JSON API Specification](http://jsonapi.org/) for resources access, while the search functionality is provided through an ElasticSearch cluster. The search endpoints use ElasticSearch query language.

### Authentication

#### Obtain an API key

Sign up for a developer account on [OpenFood](https://www.openfood.ch) and visit the *API Keys* page. This key must be passed with all requests, in the header.


### Swagger Documentation

The API is documented via the [OpenAPI Specification](https://www.openapis.org/) via a [Swagger](http://swagger.io/) interface. This may be found on the [documentation page](https://www.openfood.ch/api-docs?locale=en).

The documentation may be freely viewed without an account. Using your developer API Key, you can run live queries against the API and view the generated curl statements.


### Example Code

Example code for various languages may be found in this repo. Replace [API Key] with your own key where necessary. We currently support examples for

  - JavaScript
  - Python
  - Ruby
  - Android (Java)

 ### Issues Log


 ### Contributing
