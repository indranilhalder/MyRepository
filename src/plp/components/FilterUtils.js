import {
  CATEGORY_REGEX,
  BRAND_REGEX,
  BRAND_CAPTURE_REGEX,
  BRAND_CATEGORY_PREFIX
} from "./PlpBrandCategoryWrapper.js";
import { ARRAY_OF_SORTS } from "./Sort.js";

export const CATEGORY_URL_REGEX = /(:category:)(.*)/;
export const CATEGORY_URL_CAPTURE_REGEX = /(:category:)([MSH|msh][a-zA-Z0-9]+)(.*)/;

export const BRAND_URL_REGEX = /:brand:(.*)/;
export const TEXT_REGEX = /text=(.*)/;
const SEARCH_TEXT_BEFORE_CATEGORY = /(.*):category:/;

export function getSortFromQuery(str) {
  let sortToReturn = null;
  ARRAY_OF_SORTS.forEach(sort => {
    if (str && str.indexOf(sort) > -1) {
      sortToReturn = sort;
    }
  });
  return sortToReturn;
}

export function insertSubStringAt(str, toInsert, index) {
  const stringWithSubStringInserted = [
    str.slice(0, index),
    toInsert,
    str.slice(index)
  ].join("");
  return stringWithSubStringInserted;
}

export function createUrlFromQueryAndCategory(query, pathName, val) {
  let url;
  if (query) {
    // deal with the searchCategory case
    if (query.indexOf("searchCategory") > -1) {
      // there is a text option here
      const textParam = TEXT_REGEX.exec(query);
      url = `/search/?q=${textParam[1]}:relevance:category:${val}`;
      return url;
    }

    if (query.indexOf(":") === -1) {
      // this deals with q=text, with nothing else.
      // in this case I need to add a relevance.
      url = `/search/?q=${query}:category:${val}`;
    } else {
      // We have q = text, as well as a sort, category or brand.
      const hasCategory = CATEGORY_URL_REGEX.test(query);
      const hasBrand = BRAND_URL_REGEX.test(query);
      if (hasCategory && !hasBrand) {
        // we have an existing category
        // we want to replace this category
        const test = query.replace(CATEGORY_URL_CAPTURE_REGEX, `$1${val}$3`);

        url = `/search/?q=${test}`;
      } else if (hasBrand && !hasCategory) {
        const index = query.indexOf(":brand");
        const queryWithCategoryInserted = insertSubStringAt(
          query,
          `:category:${val}`,
          index
        );

        // in this case there is a brand, but not category.
        // we need to put the category before the brand.

        url = `/search/?q=${queryWithCategoryInserted}`;
      } else if (hasBrand && hasCategory) {
        // I know it has a brand and a category

        const test = SEARCH_TEXT_BEFORE_CATEGORY.exec(query);
        const brand = BRAND_URL_REGEX.exec(query);
        url = `/search/?q=${test[1]}:category:${val}:brand:${brand[1]}`;
      } else {
        // Now we don't have a category or brand, but we have some q value.
        // As we had the earlier if, we know that there is a sort here, but we don't know if there is a text.
        if (query.startsWith(":")) {
          url = `/search/?q=${query}:category:${val}`;
        } else {
          url = `/search/?q=${query}:category:${val}`;
        }

        // I need to check if there is a sort or not.
        //
      }
    }
  } else {
    if (CATEGORY_REGEX.test(pathName)) {
      url = `/search/?q=:category:${val}`;
    } else if (BRAND_REGEX.test(pathName)) {
      let brandId = BRAND_CAPTURE_REGEX.exec(pathName)[0];
      brandId = brandId.replace(BRAND_CATEGORY_PREFIX, "");
      url = `/search/?q=:category:${val}:brand:${brandId.toUpperCase()}`;
    } else {
      //now it is a url that looks like a static page?
      // If i hit this am I in a custom sku page?
      const splitUrl = pathName.split("/");
      const slug = splitUrl[splitUrl.length - 1];
      url = `/search/?q=:relevance:collectionIds:${slug}`;
    }
  }

  /*
If there is no sort -
  Find the FIRST instance of a brand or category.
    Take the EARLIEST index and insert the sort there.
  If there is no brand or category, then simply append :relevance to the end.
*/

  const sort = getSortFromQuery(url);
  if (!sort) {
    // find the earliest index of brand or category > 0;
    const brandIndex = url.indexOf(":brand:");
    const categoryIndex = url.indexOf(":category:");
    if (brandIndex === -1 && categoryIndex === -1) {
      // there is no brand or category
      url = `${url}:relevance`;
    } else {
      if (brandIndex === -1 && categoryIndex > -1) {
        url = insertSubStringAt(url, ":relevance", categoryIndex);
      }
      if (brandIndex > -1 && categoryIndex === -1) {
        url = insertSubStringAt(url, ":relevance", brandIndex);
      }
      if (brandIndex > -1 && categoryIndex > -1) {
        // put in the earliest.
        if (brandIndex < categoryIndex) {
          url = insertSubStringAt(url, ":relevance", brandIndex);
        }

        if (brandIndex > categoryIndex) {
          url = insertSubStringAt(url, ":relevance", categoryIndex);
        }
      }
    }
  }

  return url;
}
