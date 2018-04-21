const DUMMY_CATEGORY_VALUE = "msh121000";
const ANOTHER_DUMMY_CATEGORY_VALUE = "msh121100";

const DUMMY_L1_CATEGORY_VALUE = "msh12";
const DUMMY_L2_CATEGORY_VALUE = "msh12";

const DUMMY_BRAND_VALUE = "mbh1210";

import { applySortToUrl } from "./SortUtils.js";
import { ARRAY_OF_SORTS } from "./Sort.js";

it("should work with category landing page", () => {
  let url = `/electronics-mobile-phones/c-${ANOTHER_DUMMY_CATEGORY_VALUE}`;
  let endUrl = applySortToUrl("", url, ARRAY_OF_SORTS[3]);
  expect(endUrl).toEqual(
    `/search/?q=:${
      ARRAY_OF_SORTS[3]
    }:category:${ANOTHER_DUMMY_CATEGORY_VALUE.toUpperCase()}`
  );
});

it("should work with brand landing page", () => {
  let url = `/electronics-mobile-phones/c-${DUMMY_BRAND_VALUE}`;
  let endUrl = applySortToUrl("", url, ARRAY_OF_SORTS[3]);
  expect(endUrl).toEqual(
    `/search/?q=:${
      ARRAY_OF_SORTS[3]
    }:category:${DUMMY_BRAND_VALUE.toUpperCase()}`
  );
});

it("should work with search/?q=<text>", () => {
  let endUrl = applySortToUrl("shirt", "search/?q=shirt", ARRAY_OF_SORTS[3]);
  expect(endUrl).toEqual(`/search/?q=shirt:${ARRAY_OF_SORTS[3]}`);
});

it("should work with search/?q=<text>:sort", () => {
  let endUrl = applySortToUrl(
    "shirt:relevance",
    "search/?q=shirt:relevance",
    ARRAY_OF_SORTS[3]
  );

  expect(endUrl).toEqual(`/search/?q=shirt:${ARRAY_OF_SORTS[3]}`);
});

it("should work with search/?q=:sort", () => {
  let endUrl = applySortToUrl(
    `:${ARRAY_OF_SORTS[0]}`,
    `/search/?q=${ARRAY_OF_SORTS[0]}`,
    ARRAY_OF_SORTS[3]
  );
  expect(endUrl).toEqual(`/search/?q=:${ARRAY_OF_SORTS[3]}`);
});

it("should work with /search/?q=:sort:category:<SOME CATEGORY>", () => {
  let endUrl = applySortToUrl(
    `:${ARRAY_OF_SORTS[0]}:category:${DUMMY_CATEGORY_VALUE}`,
    `/search/?q=:${ARRAY_OF_SORTS[0]}:category:${DUMMY_CATEGORY_VALUE}`,
    ARRAY_OF_SORTS[3]
  );

  expect(endUrl).toEqual(
    `/search/?q=:${ARRAY_OF_SORTS[3]}:category:${DUMMY_CATEGORY_VALUE}`
  );
});

it("should work with /search/?q=text:sort:category:<SOME_CATEGORY>", () => {
  let endUrl = applySortToUrl(
    `shirt:${ARRAY_OF_SORTS[0]}:category:${DUMMY_CATEGORY_VALUE}`,
    `/search/?q=shirt:${ARRAY_OF_SORTS[0]}:category:${DUMMY_CATEGORY_VALUE}`,
    ARRAY_OF_SORTS[3]
  );

  expect(endUrl).toEqual(
    `/search/?q=shirt:${ARRAY_OF_SORTS[3]}:category:${DUMMY_CATEGORY_VALUE}`
  );
});

it("should work with /search/?q=:category:<SOME_CATEGORY>", () => {
  let endUrl = applySortToUrl(
    `:category:${DUMMY_CATEGORY_VALUE}`,
    `/search/?q=:category:${DUMMY_CATEGORY_VALUE}`,
    ARRAY_OF_SORTS[2]
  );

  expect(endUrl).toEqual(
    `/search/?q=:${ARRAY_OF_SORTS[2]}:category:${DUMMY_CATEGORY_VALUE}`
  );
});

it("should work with /search/?q=:brand:<SOME BRAND>", () => {
  let endUrl = applySortToUrl(
    `:brand:${DUMMY_BRAND_VALUE}`,
    `/search/?q=:brand:${DUMMY_BRAND_VALUE}`,
    ARRAY_OF_SORTS[3]
  );

  expect(endUrl).toEqual(
    `/search/?q=:${ARRAY_OF_SORTS[3]}:brand:${DUMMY_BRAND_VALUE}`
  );
});

it("should work with /search/?q=text:brand:<SOME BRAND>", () => {
  let endUrl = applySortToUrl(
    `text:brand:${DUMMY_BRAND_VALUE}`,
    `/search/?q=text:brand:${DUMMY_BRAND_VALUE}`,
    ARRAY_OF_SORTS[3]
  );

  expect(endUrl).toEqual(
    `/search/?q=text:${ARRAY_OF_SORTS[3]}:brand:${DUMMY_BRAND_VALUE}`
  );
});

it("should work with /search/?q=text:category:<SOME_CATEGORY", () => {
  let endUrl = applySortToUrl(
    `text:category:${DUMMY_CATEGORY_VALUE}`,
    `/search/?q=text:category:${DUMMY_CATEGORY_VALUE}`,
    ARRAY_OF_SORTS[5]
  );

  expect(endUrl).toEqual(
    `/search/?q=text:${ARRAY_OF_SORTS[5]}:category:${DUMMY_CATEGORY_VALUE}`
  );
});

it("should work with /search/?q=text:category:<SOME_CATEGORY:brand:<SOME_BRAND>", () => {
  let endUrl = applySortToUrl(
    `text:category:${DUMMY_CATEGORY_VALUE}:brand:${DUMMY_BRAND_VALUE}`,
    `/search/?q=text:category:${DUMMY_CATEGORY_VALUE}:brand:${DUMMY_BRAND_VALUE}`,
    ARRAY_OF_SORTS[2]
  );

  expect(endUrl).toEqual(
    `/search/?q=text:${
      ARRAY_OF_SORTS[2]
    }:category:${DUMMY_CATEGORY_VALUE}:brand:${DUMMY_BRAND_VALUE}`
  );
});
