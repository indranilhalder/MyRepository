export const SET_SEARCH_STRING = "SET_SEARCH_STRING";
export const SET_FILTERS = "SET_FILTERS";
export const SET_SORT = "SET_SORT";

export function setSearchString(string) {
  return {
    type: SET_SEARCH_STRING,
    string
  };
}

export function setFilters(filters) {
  return {
    type: SET_FILTERS,
    filters
  };
}

export function setSort(sort) {
  return {
    type: SET_SORT,
    sort
  };
}
