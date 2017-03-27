## OpenFood API Endpoints

## OpenFood Product Model

### Fields

TBD

### The updated_at field

The **updated_at** field in the Products Elasticsearch index is the highest date of the products, images or product_nutrients collections, for a given product.


## Retrieve a Products Listing: products#index

Returns a list of products with default paging (50 products per page). It will return paging links and an index of the Nutrient and Image includes, but not the data.

```bash
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products" -H 'Authorization: Token token="[API_KEY]"'
```

[API_KEY]

### With Includes

It is possible to add the public urls for images associated with the products, as well as detailed nutrient information. To do this, pass a **nutrients[]=** parameter with the request. Examples are below.

#### Images

```bash
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products?includes[]=images" -H 'Authorization: Token token="[API_KEY]"'
```

#### Nutrients

```bash
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products?includes[]=nutrients'" -H 'Authorization: Token token="[API_KEY]"'
```

#### Both Images and Nutrients

```bash
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products?includes[]=nutrients&includes[]=images'" -H 'Authorization: Token token="[API_KEY]"'
```

### Search by barcodes

It is possible to filter the results of **products#index** by barcodes. Filtering barcodes are passed in the array format.

```bash
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products?barcodes[]=4104420034563&barcodes[]=4104420033764'" -H 'Authorization: Token token="[API_KEY]"'
```

This filter may be combined with the **includes** feature.

```bash
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products?barcodes[]=4104420034563&barcodes[]=4104420033764&includes[]=nutrients&includes[]=images'" -H 'Authorization: Token token="[API_KEY]"'
```

#### Paging

##### Set Page Size

```
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products?page[size]=50" -H 'Authorization: Token token="[API_KEY]"'
```

#### Access a specific page

```
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products?page[size]=20&page[number]=5" -H 'Authorization: Token token="[API_KEY]"'
```


## Retrieve a single Product: products#show

### Call by Product ID

Add the product ID to the url.

```
curl -i -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products/972" -H 'Authorization: Token token="[API_KEY]"'
```

#### With Includes

The Includes feature functions the same way as for the **products#index** endpoint.

```
curl -i -g -H "Accept: application/vnd.api+json" -H 'Content-Type:application/vnd.api+json' -X GET "https://www.openfood.ch/api/v3/products/972?includes[]=nutrients&includes[]=images'" -H 'Authorization: Token token="[API_KEY]"'
```

## Search using Elasticsearch Query DSL

Full search ability is available against the **_search** endpoint. This endpoint provides access to all OpenFood Product data using the [ElasticSearch Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html).

### Limiting Fields

By default, the results from the **_search** endpoint will include all fields, included the **images** and **nutrients** nested endpoints. To limit fields, use the **includes** parameter against **_source**.

### Filtering

The **query** parameter is frequently used in searched. Full details are described in the [ElasticSearch Query DSL](https://www.elastic.co/guide/en/elasticsearch/reference/current/query-dsl.html).

### Sorting

The sort order can be specified using the **sort** feature.

### Example Queries

#### Limit fields, search and sort


```bash
curl -X POST https://www.openfood.ch/api/v3/products/_search -H 'Authorization: Token token="[API_KEY]"' -d'
{ "_source": { "includes": [ "id", "barcode","status", "quantity" ] },
        "query": { "range": { "quantity": { "gte": 50, "lte": 650 } } },
        "sort": { "quantity": { "order": "asc" }}
      }
'
```

#### Search on Nested fields

```bash
curl -X POST "https://www.openfood.ch/api/v3/products/_search" -H 'Authorization: Token token="[API_KEY]"' -d'
{ "_source": { "includes": [ "id", "barcode","status", "quantity","nutrients" ] },
  "query": {
    "bool": {
      "must": [
        { "match": { "nutrients.name_fr": "Énergie" } },
        { "match": { "nutrients.per_hundred": "242.0" } }
      ]
    }
  }
}
'
```

#### Retrieve ID and barcode for a specific barcode

```bash
curl -X POST "https://www.openfood.ch/api/v3/products/_search" -H 'Authorization: Token token="[API_KEY]"' -d'
{ "_source": { "includes": [ "id", "barcode","name_translations" ] },
  "query": { "term": { "barcode": "7611654457411" } }
}
'
```

#### Wildcard query

ID and barcode for a wildcard barcode query

```bash
curl -X POST "https://www.openfood.ch/api/v3/products/_search" -H 'Authorization: Token token="[API_KEY]"' -d'
{ "_source": { "includes": [ "id", "barcode","name_translations" ] },
  "query": { "wildcard": { "barcode": "7611*" } }
}
'
```

#### Multiple field query

ID, barcode, status, unit and quantity for a quantity > 600 and unit of ml, ordered by quantity


```bash
curl -X POST "https://www.openfood.ch/api/v3/products/_search" -H 'Authorization: Token token="[API_KEY]"' -d'
{ "_source": { "includes": [ "id", "barcode","status", "quantity", "unit" ] },
  "query": {
    "bool": {
      "must": [
        { "term": { "unit": "g" }},
        { "range": { "quantity": { "gte": 550.0, "lte": 650.0 } } }
      ]
    }
  }
}
'
```


#### Exact match search against products

```bash
curl -X POST "https://www.openfood.ch/api/v3/products/_search?pretty" -H 'Authorization: Token token="[API_KEY]"' -d'
{
    "_source": true,
    "query": {
        "constant_score" : {
            "filter" : {
                "terms" : { "barcode" : ["4104420034563", "4104420033764"]}
            }
        }
    }
}
'
```

#### Wildcard search against product names

```bash
curl -X POST "https://www.openfood.ch/api/v3/products/_search?pretty" -H 'Authorization: Token token="[API_KEY]"' -d'
{
  "_source": true,
  "query": {
    "wildcard": {
      "name_translations.en" : "Toble*"
    }
  }
}
'
```
