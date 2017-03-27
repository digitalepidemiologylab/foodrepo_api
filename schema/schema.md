### Products Schema

The same schema is used for **products#index**, **products#show** and **products#_search**.

```json
{
  "products" : {
    "mappings" : {
      "product" : {
        "_all" : {
          "analyzer" : "searchkick_index"
        },
        "dynamic_templates" : [ {
          "string_template" : {
            "mapping" : {
              "include_in_all" : true,
              "ignore_above" : 30000,
              "index" : "not_analyzed",
              "type" : "string",
              "fields" : {
                "analyzed" : {
                  "index" : "analyzed",
                  "type" : "string"
                }
              }
            },
            "match" : "*",
            "match_mapping_type" : "string"
          }
        } ],
        "properties" : {
          "alcool_by_volume" : {
            "type" : "double"
          },
          "barcode" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "created_at" : {
            "type" : "date",
            "format" : "strict_date_optional_time||epoch_millis"
          },
          "id" : {
            "type" : "long"
          },
          "images" : {
            "properties" : {
              "categories" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              },
              "large" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              },
              "medium" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              },
              "thumb" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              },
              "xlarge" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              }
            }
          },
          "ingredients_de" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "ingredients_en" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "ingredients_fr" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "ingredients_it" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "name_de" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "name_en" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "name_fr" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "name_it" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "nutrients" : {
            "properties" : {
              "id" : {
                "type" : "long"
              },
              "name_de" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              },
              "name_en" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              },
              "name_fr" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              },
              "name_it" : {
                "type" : "string",
                "index" : "not_analyzed",
                "fields" : {
                  "analyzed" : {
                    "type" : "string"
                  }
                },
                "include_in_all" : true,
                "ignore_above" : 30000
              },
              "per_day" : {
                "type" : "double"
              },
              "per_hundred" : {
                "type" : "double"
              },
              "per_portion" : {
                "type" : "double"
              }
            }
          },
          "origin_de" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "origin_en" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "origin_fr" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "origin_it" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "portion_quantity" : {
            "type" : "double"
          },
          "portion_unit" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "quantity" : {
            "type" : "double"
          },
          "remarks" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "status" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "unit" : {
            "type" : "string",
            "index" : "not_analyzed",
            "fields" : {
              "analyzed" : {
                "type" : "string"
              }
            },
            "include_in_all" : true,
            "ignore_above" : 30000
          },
          "updated_at" : {
            "type" : "date",
            "format" : "strict_date_optional_time||epoch_millis"
          },
          "user_id" : {
            "type" : "long"
          }
        }
      },
      "_default_" : {
        "_all" : {
          "analyzer" : "searchkick_index"
        },
        "dynamic_templates" : [ {
          "string_template" : {
            "mapping" : {
              "include_in_all" : true,
              "ignore_above" : 30000,
              "index" : "not_analyzed",
              "type" : "string",
              "fields" : {
                "analyzed" : {
                  "index" : "analyzed",
                  "type" : "string"
                }
              }
            },
            "match" : "*",
            "match_mapping_type" : "string"
          }
        } ]
      }
    }
  }
}
```
