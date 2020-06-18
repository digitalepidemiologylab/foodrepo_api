## Prerequisites

R version 3.6.3

Libraries: `httr`, `jsonlite`, `rlist`, `data.table`

## Set up

In your home folder, create a `foodrepo` subfolder and put the script `utils_fr.R` inside. Then open Rstudio and run the command `file.edit("~/.Rprofile")` and add the following line in the file that pops up: `FOODREPOAPI = "thisisafakeapikeypleasegogetyoursatfoodrepodotorg"` before closing the file. If you havent generated your api key yet please visit [foodrepo website](https://www.foodrepo.org/) and do so.

## How does that work?

First make sure to source the function contained inside the script `utils_fr.R`

```
source("~/foodrepo/utils_fr.R")
```

Now lets test the first function that allows you to retrieve information for a particular barcode:

```
get_foodrepo_prod_by_barcode(barcode = "7610745672238")
```

Now another example for querying page 2 to 8 of the foodrepo database.

```
page_2_to_8 = get_all_foodrepo_items(first=2, last=8)
head(page_2_to_8)
```

And the following code queries the whole database (around 40000 items). This can take between 5 to 10min.

```
complete_database = get_all_foodrepo_items()
```
