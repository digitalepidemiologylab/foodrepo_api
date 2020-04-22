# Product Object

An OpenFood product.

### Context

Any endpoint that returns a Product in its JSON response.

### Schema

* `id`
  * Type: `long`
  * Description: The ID used by the OpenFood database for this Product.
* `barcode`
  * Type: `string`
  * Description: The Product barcode.
* `name_translations`
  * Type: [`Optional Translations Object`](optional_translations.md)
  * Description: The names of the Product, translated into any languages provided by the manufacturer.
* `display_name_translations`
  * Type: [`Translations Object`](translations.md)
  * Description: The names of the Product in all supported languages, falling back to another language or the barcode if necessary.
* `ingredients_translations`
  * Type [`Optional Translations Object`](optional_translations.md)
  * Description: The ingredients of the Product, translated into any languages provided by the manufacturer.
* `origin_translations`
  * Type [`Optional Translations Object`](optional_translations.md)
  * Description: The origin(s) of the Product, translated into any languages provided by the manufacturer.
* `status`
  * Type: `string`
  * Description: For internal use only.
* `quantity`
  * Type: `float`
  * Description: The dry net weight or volume of the Product. To be used in conjunction with `unit`.
* `unit`
  * Type: `string`
  * Description: The unit used by `quantity` to measure the weight or volume of the product. Usually `g` or `ml`.
* `portion_quantity`
  * Type: `float` (optional)
  * Description: The dry net weight or volume of a serving size of the Product. To be used in conjunction with `portion_unit`.
* `portion_unit` (optional)
  * Type: `string`
  * Description: The unit used by `portion_quantity` to measure the weight or volume of a serving size of the product. Usually `g` or `ml`.
* `hundred_unit`
  * Type: `string`
  * Description: The unit used for the "per hundred" in product nutrients (`g` or `ml`).
* `alcohol_by_volume`
  * Type: `float` (optional)
  * Description: The percentage of alcohol in the Product.
* `images`
  * Type: `object`
  * `categories`
    * Type: `Array[string]`
    * Description: image categories like "Front" or "Ingredients List"
  * `thumb`
    * Type: `string`
    * Description: URL for this image's thumbnail. Max width/height of 200px.
  * `medium`
    * Type: `string`
    * Description: URL for a medium-sized version of this image.
  * `large`
    * Type: `string`
    * Description: URL for a large version of this image.
  * `xlarge`
    * Type: `string`
    * Description: URL for an extra large version of this image.
* `nutrients`
  * Type: [`Product Nutrients Object`](product_nutrients.md)
  * Description: The Product's Nutrients as described by the manufacturer.
* `created_at`
  * Type: `string`
  * Description: UTC DateTime at which this Product was added to the OpenFood database.
* `updated_at`
  * Type `string`
  * Description: UTC DateTime at which this Product (or one of its subordinate objects, including `images` `nutrients`) was last modified.
