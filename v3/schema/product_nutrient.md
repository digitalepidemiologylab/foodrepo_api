# Product Nutrient Object

One of a Product's Nutrients as described by the manufacturer.

### Schema

* `name_translations`
  * Type: [`Translations Object`](translations.md)
  * Description: The names of the Nutrient in all supported languages.
* `unit`
  * Type: `string`
  * Description: The unit that the `per_hundred` and `per_portion` values use.
* `per_hundred`
  * Type: `float`
  * Description: The amount (using `unit`) of this Nutrient that can be found in one hundred of the Product's `unit` (which may not be the same as the Nutrient's!)
* `per_portion`
  * Type: `float`
  * Description: The amount (using `unit`) of this Nutrient that can be found in a serving size of the Product. (using its `portion_unit`, which may not be the same as the Nutrient's `unit`!)
* `per_day`
  * Type: `float`
  * Description: The percentage of the Recommended Daily Intake for this Nutrient.
