# ElasticSearch Sample Queries

##### Terms search against products

```json
{
  "query": {
    "terms" : {
      "barcode" : [
        "4104420034563",
        "4104420033764"
      ]
    }
  }
}
```

##### Wildcard search against product name

```json
{
  "query": {
    "wildcard": {
      "_all_names" : "*toblerone*"
    }
  }
}
```

##### Search taking advantage of language-specific analysis

```json
{
  "query": {
    "query_string": {
      "fields" : [
        "_all_translations.de"
      ],
      "query" : "Grün"
    }
  }
}
```

##### Fuzzy search against french product name, sorted by sugar content, retrieving a maximum of 20 products, transmitting only the names and barcode and sugar content.

```json
{
  "_source": {
    "includes": [
      "name_translations",
      "barcode",
      "nutrients.sugars.per_hundred"
    ]
  },
  "size": 20,
  "query": {
    "query_string": {
      "fields" : [
        "name_translations.fr"
      ],
      "query" : "Jogurt~ OR Yaourt~"
    }
  },
  "sort": "nutrients.sugars.per_hundred"
}
```

##### Find all 'coop' products that are not also 'naturaplan' products that were added to the database in 2016.

```json
{
  "query": {
    "bool": {
      "must": [
        { "match": { "name_translations.de": "coop" }}
      ],
      "must_not": [
        { "match": { "name_translations.de": "naturaplan" }}
      ],
      "filter": [
        { "range": { "created_at": { "gte": "2016-01-01" }}},
        { "range": { "created_at": { "lt": "2017-01-01" }}}
      ]
    }
  }
}
```

##### Find all products with a sugar content between 24% and 26%

```json
{
  "query": {
    "bool": {
      "must": [
        { "range": { "nutrients.sugars.per_hundred": {
          "gte": "24",
          "lt": "26"
        }}}
      ]
    }
  }
}
```
