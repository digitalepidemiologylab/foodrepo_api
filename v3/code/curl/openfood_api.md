# OpenFood API version 3

## Curl Example Code

### Product Endpoints

#### Get Product by ID ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/findProductById))

* Get a product whose ID you know (e.g. `2663`)
```bash
$ curl -X GET "https://www.openfood.ch/api/v3/products/2663" -H 'Content-Type: application/vnd.api+json' -H 'Accept: application/json' --compressed -H 'Authorization: Token token="API_KEY"'
```

#### List of Products ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/listProducts))

Supports query parameters for paging, filtering by barcode, and excluding fields that you may not interested in (such as `nutrients` or `ingredients_translations`).

* Get all products, starting at page 1:
```bash
$ curl -X GET "https://www.openfood.ch/api/v3/products" -H 'Content-Type: application/vnd.api+json' -H 'Accept: application/json' --compressed -H 'Authorization: Token token="API_KEY"'
```

* Get a product whose barcode you know (e.g. `7610046125730`)
```bash
$ curl -X GET "https://www.openfood.ch/api/v3/products?barcodes=7610046125730" -H 'Content-Type: application/vnd.api+json' -H 'Accept: application/json' --compressed -H 'Authorization: Token token="API_KEY"'
```

#### Search for Products ([Try it!](https://www.openfood.ch/api-docs/swaggers/v3#!/default/searchProducts))
Advanced search using ElasticSearch Query DSL in the request data. See [the 'Search' section of the main API v3 README](/v3/README.md#search) for in depth explanations and example queries.

```bash
$ curl -X POST https://www.openfood.ch/api/v3/products/_search?pretty -H 'Content-Type: application/vnd.api+json' -H 'Accept: application/json' -H 'Authorization: Token token="API_KEY"' --compressed -d '
{
  "query": {
    "wildcard": {
      "_all_names" : "*toblerone*"
    }
  }
}
'
```
