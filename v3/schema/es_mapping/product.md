# ElasticSearch Product Mapping

## Simple

### Overview

##### The mapping is very very similar to the [Product schema](/v3/schema/product.md).
* Keys mentioned in that schema but NOT mapped in ElasticSearch are ~~struck through~~ in the mapping below.
* Meta keys that are not a part of the schema but are mapped are **bold** in the mapping below.

##### Language analysis

* All translations are accessible through their normal path as text that has been analyzed in the appropriate language. This means that a search for `walking` in any field ending with `_translations.en` will find results that contain `walked` or `walks` too.
* If you need exact results disregarding all word inflections in any language, each translation has a `raw` meta field (e.g. `ingredients_translations.de.raw`).

##### Other

* The `_all` meta field is not used.

#### Mapping

* `id`: `long`
* `barcode`: `keyword`
* `name_translations`
  * `de`: `text` (Analyzed in German)
  * `de.raw`: `text` (Analyzed in Standard)
  * `fr`: `text` (Analyzed in French)
  * `fr.raw`: `text` (Analyzed in Standard)
  * `it`: `text` (Analyzed in Italian)
  * `it.raw`: `text` (Analyzed in Standard)
  * `en`: `text` (Analyzed in English)
  * `en.raw`: `text` (Analyzed using Standard)
* ~~`display_name_translations` : NOT MAPPED~~
* `ingredients_translations`
  * `de`: `text` (Analyzed in German)
  * `de.raw`: `text` (Analyzed in Standard)
  * `fr`: `text` (Analyzed in French)
  * `fr.raw`: `text` (Analyzed in Standard)
  * `it`: `text` (Analyzed in Italian)
  * `it.raw`: `text` (Analyzed in Standard)
  * `en`: `text` (Analyzed in English)
  * `en.raw`: `text` (Analyzed using Standard)
* `origin_translations`
  * `de`: `text` (Analyzed in German)
  * `de.raw`: `text` (Analyzed in Standard)
  * `fr`: `text` (Analyzed in French)
  * `fr.raw`: `text` (Analyzed in Standard)
  * `it`: `text` (Analyzed in Italian)
  * `it.raw`: `text` (Analyzed in Standard)
  * `en`: `text` (Analyzed in English)
  * `en.raw`: `text` (Analyzed using Standard)
* `status`: `keyword`
* `quantity`: `float`
* `unit`: `keyword`
* `portion_quantity`: `float`
* `portion_unit`: `keyword`
* `alcohol_by_volume`: `float`
* `images`: `nested`
  * `categories`: `keyword`
  * `thumb`: `keyword`
  * `medium`: `keyword`
  * `large`: `keyword`
  * `xlarge`: `keyword`
* `nutrients`
  * [`<nutrient_key_1>`](/v3/schema/product_nutrients.md#context)
    * `name_translations`
      * `de`: `text` (Analyzed in German)
      * `de.raw`: `text` (Analyzed in Standard)
      * `fr`: `text` (Analyzed in French)
      * `fr.raw`: `text` (Analyzed in Standard)
      * `it`: `text` (Analyzed in Italian)
      * `it.raw`: `text` (Analyzed in Standard)
      * `en`: `text` (Analyzed in English)
      * `en.raw`: `text` (Analyzed using Standard)
    * ~~`unit`: NOT MAPPED~~
    * `per_hundred`: `float`
    * `per_portion`: `float`
    * `per_day`: `float`
  * `<nutrient_key_2>`...
* `created_at`: `date`
* `updated_at`: `date`
* **\_all_names**: `text` <small>(Meta field containing all values of `names_translations`)</small>
* **\_all_text**: `text` <small>(Meta field containing all values of type `text`)</small>
* **\_all_text_translations** <small>(Meta field with each key containing everything from the corresponding key in `names_translations`, `origins_translations`, and `ingredients_translations`)</small>
  * `de`: `text` (Analyzed in German)
  * `de.raw`: `text` (Analyzed in Standard)
  * `fr`: `text` (Analyzed in French)
  * `fr.raw`: `text` (Analyzed in Standard)
  * `it`: `text` (Analyzed in Italian)
  * `it.raw`: `text` (Analyzed in Standard)
  * `en`: `text` (Analyzed in English)
  * `en.raw`: `text` (Analyzed using Standard)


## Advanced

```json
{
  "dynamic" : "false",
  "_all" : {
    "enabled" : false
  },
  "dynamic_templates" : [
    {
      "nutrient_unit" : {
        "path_match" : "nutrients.*.unit",
        "mapping" : {
          "index" : "no",
          "type" : "keyword"
        }
      }
    },
    {
      "nutrient_value" : {
        "path_match" : "nutrients.*.per_*",
        "mapping" : {
          "type" : "float"
        }
      }
    },
    {
      "name_translations_en" : {
        "path_match" : "name_translations.en",
        "mapping" : {
          "analyzer" : "english",
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.en"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "_meta_translations_en" : {
        "path_match" : "_*_translations.en",
        "mapping" : {
          "analyzer" : "english",
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "translations_en" : {
        "path_match" : "*_translations.en",
        "mapping" : {
          "analyzer" : "english",
          "copy_to" : [
            "_all_text",
            "_all_text_translations.en"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "name_translations_fr" : {
        "path_match" : "name_translations.fr",
        "mapping" : {
          "analyzer" : "french",
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.fr"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "_meta_translations_fr" : {
        "path_match" : "_*_translations.fr",
        "mapping" : {
          "analyzer" : "french",
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "translations_fr" : {
        "path_match" : "*_translations.fr",
        "mapping" : {
          "analyzer" : "french",
          "copy_to" : [
            "_all_text",
            "_all_text_translations.fr"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "name_translations_de" : {
        "path_match" : "name_translations.de",
        "mapping" : {
          "analyzer" : "german",
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.de"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "_meta_translations_de" : {
        "path_match" : "_*_translations.de",
        "mapping" : {
          "analyzer" : "german",
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "translations_de" : {
        "path_match" : "*_translations.de",
        "mapping" : {
          "analyzer" : "german",
          "copy_to" : [
            "_all_text",
            "_all_text_translations.de"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "name_translations_it" : {
        "path_match" : "name_translations.it",
        "mapping" : {
          "analyzer" : "italian",
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.it"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "_meta_translations_it" : {
        "path_match" : "_*_translations.it",
        "mapping" : {
          "analyzer" : "italian",
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "translations_it" : {
        "path_match" : "*_translations.it",
        "mapping" : {
          "analyzer" : "italian",
          "copy_to" : [
            "_all_text",
            "_all_text_translations.it"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "name_translations_*" : {
        "path_match" : "name_translations.*",
        "mapping" : {
          "analyzer" : "standard",
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.*"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "_meta_translations_*" : {
        "path_match" : "_*_translations.*",
        "mapping" : {
          "analyzer" : "standard",
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    },
    {
      "translations_*" : {
        "path_match" : "*_translations.*",
        "mapping" : {
          "analyzer" : "standard",
          "copy_to" : [
            "_all_text",
            "_all_text_translations.*"
          ],
          "fields" : {
            "raw" : {
              "analyzer" : "standard",
              "type" : "text"
            }
          },
          "type" : "text"
        }
      }
    }
  ],
  "properties" : {
    "_all_names" : {
      "type" : "text"
    },
    "_all_text" : {
      "type" : "text"
    },
    "_all_text_translations" : {
      "dynamic" : "true",
      "properties" : {
        "de" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "analyzer" : "german"
        },
        "en" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "analyzer" : "english"
        },
        "fr" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "analyzer" : "french"
        },
        "it" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "analyzer" : "italian"
        }
      }
    },
    "alcohol_by_volume" : {
      "type" : "float"
    },
    "barcode" : {
      "type" : "keyword"
    },
    "created_at" : {
      "type" : "date"
    },
    "id" : {
      "type" : "long"
    },
    "images" : {
      "type" : "nested",
      "properties" : {
        "categories" : {
          "type" : "keyword"
        },
        "large" : {
          "type" : "keyword"
        },
        "medium" : {
          "type" : "keyword"
        },
        "thumb" : {
          "type" : "keyword"
        },
        "xlarge" : {
          "type" : "keyword"
        }
      }
    },
    "ingredients_translations" : {
      "dynamic" : "true",
      "properties" : {
        "de" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_text",
            "_all_text_translations.de"
          ],
          "analyzer" : "german"
        },
        "en" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_text",
            "_all_text_translations.en"
          ],
          "analyzer" : "english"
        },
        "fr" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_text",
            "_all_text_translations.fr"
          ],
          "analyzer" : "french"
        },
        "it" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_text",
            "_all_text_translations.it"
          ],
          "analyzer" : "italian"
        }
      }
    },
    "name_translations" : {
      "dynamic" : "true",
      "properties" : {
        "de" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.de"
          ],
          "analyzer" : "german"
        },
        "en" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.en"
          ],
          "analyzer" : "english"
        },
        "fr" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.fr"
          ],
          "analyzer" : "french"
        },
        "it" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_names",
            "_all_text",
            "_all_text_translations.it"
          ],
          "analyzer" : "italian"
        }
      }
    },
    "nutrients" : {
      "dynamic" : "true",
      "properties" : {
        "biotin" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "calcium" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "carbohydrates" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "cholesterol" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "energy" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "energy_kcal" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "fat" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "fiber" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "folic_acid" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "fructose" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "glucose" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "iodine" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "iron" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "lactose" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "magnesium" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "monounsaturated_fatty_acids" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "omega-3_fatty_acids" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "omega-6_fatty_acids" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "phosphorus" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "polyols" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "polyunsaturated_fatty_acids" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "protein" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "provitamin_a_-carotene" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "saccharose" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "salt" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "saturated_fat" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "selenium" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "sodium" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "sugars" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_a" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_b12_cobalamin" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_b1_thiamin" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_b2_riboflavin" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_b3_niacin" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_b5_panthothenic_acid" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_b6_pyridoxin" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_c_ascorbic_acid" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_d_cholacalciferol" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_e_tocopherol" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "vitamin_k" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        },
        "zinco" : {
          "properties" : {
            "name_translations" : {
              "properties" : {
                "de" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.de"
                  ],
                  "analyzer" : "german"
                },
                "en" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.en"
                  ],
                  "analyzer" : "english"
                },
                "fr" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.fr"
                  ],
                  "analyzer" : "french"
                },
                "it" : {
                  "type" : "text",
                  "fields" : {
                    "raw" : {
                      "type" : "text",
                      "analyzer" : "standard"
                    }
                  },
                  "copy_to" : [
                    "_all_text",
                    "_all_text_translations.it"
                  ],
                  "analyzer" : "italian"
                }
              }
            },
            "per_day" : {
              "type" : "float"
            },
            "per_hundred" : {
              "type" : "float"
            },
            "per_portion" : {
              "type" : "float"
            },
            "unit" : {
              "type" : "keyword",
              "index" : false
            }
          }
        }
      }
    },
    "origin_translations" : {
      "dynamic" : "true",
      "properties" : {
        "de" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_text",
            "_all_text_translations.de"
          ],
          "analyzer" : "german"
        },
        "en" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_text",
            "_all_text_translations.en"
          ],
          "analyzer" : "english"
        },
        "fr" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_text",
            "_all_text_translations.fr"
          ],
          "analyzer" : "french"
        },
        "it" : {
          "type" : "text",
          "fields" : {
            "raw" : {
              "type" : "text",
              "analyzer" : "standard"
            }
          },
          "copy_to" : [
            "_all_text",
            "_all_text_translations.it"
          ],
          "analyzer" : "italian"
        }
      }
    },
    "portion_quantity" : {
      "type" : "float"
    },
    "portion_unit" : {
      "type" : "keyword"
    },
    "quantity" : {
      "type" : "float"
    },
    "status" : {
      "type" : "keyword"
    },
    "unit" : {
      "type" : "keyword"
    },
    "updated_at" : {
      "type" : "date"
    }
  }
}
```
