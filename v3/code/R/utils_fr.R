library(httr)
library(jsonlite)
library(rlist)
library(data.table)
#####################################################################
#
#
#
#
get_foodrepo_prod_by_barcode = function(barcode){
  
  res = GET(
    "https://www.foodrepo.org/" , 
    path = "api/v3/products",
    query = list(excludes="images",
                 barcodes=barcode),
    add_headers(Accept ="application/json",
                Authorization = paste0("Token token=", FOODREPOAPI))
  )
  return(fromJSON(rawToChar(res$content), flatten=T))
}
#
#example for a single barcode
#
#
############################################################
#
#DL the whole data
#
# data = get_foodrepo_prod_by_barcode(barcode = "")
# current_ = data$links[[1]]
# next_ = data$links[[2]]
# last_ = data$links[[3]] 
#
#
#
get_all_foodrepo_items = function(first=NULL, last=NULL){
  #
  #get last page url
  #
  last_ = get_foodrepo_prod_by_barcode(barcode = "")$links[[3]]
  #
  pat1 = "https://www.foodrepo.org/api/v3/products\\?excludes=images&page%5Bnumber%5D="
  pat2 = "&page%5Bsize%5D=20"
  #
  if(is.null(first) & is.null(last)){
    #
    #extract last page number
    #
    first = 1

    last = gsub(pattern = pat1, replacement = "", last_)
    last = as.numeric(gsub(pattern = pat2, replacement = "", last))
  } else {
    first = first
    last = last
  }
  #
  #Queries page after page.
  #
  data = lapply(first:last, function(i){
    res = GET(url = paste0("https://www.foodrepo.org/api/v3/products?excludes=images&page%5Bnumber%5D=", i, pat2),
              add_headers(Accept ="application/json",
                          Authorization = paste0("Token token=", FOODREPOAPI)))
    return(fromJSON(rawToChar(res$content), flatten = T)[[1]])
  })
  #
  #wrap up data 
  #
  data = rbindlist(data, fill = T)
  return(data)
}







