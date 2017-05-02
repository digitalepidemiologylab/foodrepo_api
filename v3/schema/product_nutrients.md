# Product Nutrients Object

A Product's Nutrients as described by the manufacturer.

### Context

This object is a bit special in that it's to be used as an array because the keys don't matter **except** when searching using the [\_search endpoint](https://www.openfood.ch/api-docs/swaggers/v3#!/default/searchProducts).

Each `<nutrient_key>` is simply a lowercase underscored version of the Nutrient's English name with all special characters removed. For example, the Nutrient named "Energy (kCal)" will have the key `energy_kcal`. All keys are optional.

### Schema

* `<nutrient_key>`
  * Type: [`Product Nutrient Object`](product_nutrient.md)
  * Description: A Nutrient of a Product.
