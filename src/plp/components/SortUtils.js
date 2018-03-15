import { getSortFromQuery, insertSubStringAt } from "./FilterUtils.js";
import {
  CATEGORY_REGEX,
  BRAND_REGEX,
  CAPTURE_REGEX
} from "./PlpBrandCategoryWrapper";

/*
What do I want to do?
  I have a url. I apply a sort.

  I want to generate the correct url.


  What are my cases?

  1. There is no q.
    If there is no q, this means that we are dealing with a brand or category page that fell back to a PLP.
    So we need to display a q=:<SORT_VALUE>:category:${}
    q=:<SORT_VALUE>:brand:{<BRAND_VALUE}
  2. There is q
  There is already a sort
    If there is already a sort, life is easy, we simply replace the sort.
  There is no sort
    Now we have a few cases
      q=:text:category:<>
      q=:text:brand:<>
      q=:category:<>
      q=:brand:<>
      q=:text

      If there is a category or brand, we need to find that and prepend the sort.

      if there is no category or brand, then we know there is text and we need to append.
*/

export function applySortToUrl(query, url, sortValue) {
  let newQuery = "";
  let newUrl = `/search/?q=`;
  if (query.length === 0) {
    // dealing with a brand or category landing page.

    if (CATEGORY_REGEX.test(url)) {
      const match = CAPTURE_REGEX.exec(url);
      newQuery = `:${sortValue}:category:${match[1].toUpperCase()}`;
    }
    if (BRAND_REGEX.test(url)) {
      const match = CAPTURE_REGEX.exec(url);
      newQuery = `:${sortValue}:category:${match[1].toUpperCase()}`;
    }
  } else {
    const existingSort = getSortFromQuery(query);

    if (existingSort) {
      const indexOfSort = query.indexOf(existingSort);
      newQuery = query.replace(`${existingSort}`, "");
      newQuery = insertSubStringAt(newQuery, sortValue, indexOfSort);
    } else {
      const indexOfCategory = query.indexOf(":category");
      const indexOfBrand = query.indexOf(":brand");
      sortValue = `:${sortValue}`;

      if (indexOfBrand > -1 && indexOfCategory > -1) {
        const difference = indexOfCategory - indexOfBrand;
        // if difference > 0, then indexOfCategory comes later than indexOfBrand
        if (difference > 0) {
          newQuery = insertSubStringAt(query, sortValue, indexOfBrand);
        } else {
          newQuery = insertSubStringAt(query, sortValue, indexOfCategory);
        }
      } else if (indexOfBrand > -1 && indexOfCategory === -1) {
        newQuery = insertSubStringAt(query, sortValue, indexOfBrand);
      } else if (indexOfBrand === -1 && indexOfCategory > -1) {
        newQuery = insertSubStringAt(query, sortValue, indexOfCategory);
      } else {
        // no category or brand, and also no existing sort.
        // this is the q=:text case.
        // here we simply append the sort value

        newQuery = `${query}${sortValue}`;
      }
    }
  }

  newUrl = `${newUrl}${newQuery}`;
  return newUrl;
}
