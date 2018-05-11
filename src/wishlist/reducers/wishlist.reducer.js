import cloneDeep from "lodash.clonedeep";
import * as wishlistActions from "../actions/wishlist.actions";
import { SUCCESS } from "../../lib/constants";
import { CLEAR_ERROR } from "../../general/error.actions.js";
const wishlistItems = (
  state = {
    wishlistItems: [],
    name: null,
    count: null,
    loading: false,
    status: null,
    error: null
  },
  action
) => {
  let currentWishlistItems, indexToBeRemove;
  switch (action.type) {
    case CLEAR_ERROR:
      return Object.assign({}, state, {
        error: null
      });
    case wishlistActions.CREATE_WISHLIST_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });
    case wishlistActions.CREATE_WISHLIST_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error
      });
    case wishlistActions.CREATE_WISHLIST_SUCCESS:
      return Object.assign({}, state, {
        status: action.status
      });
    case wishlistActions.GET_WISH_LIST_ITEMS_REQUEST:
    case wishlistActions.ADD_PRODUCT_TO_WISH_LIST_REQUEST:
    case wishlistActions.REMOVE_PRODUCT_FROM_WISH_LIST_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });
    case wishlistActions.GET_WISH_LIST_ITEMS_FAILURE:
    case wishlistActions.ADD_PRODUCT_TO_WISH_LIST_FAILURE:
    case wishlistActions.REMOVE_PRODUCT_FROM_WISH_LIST_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        error: action.error
      });
    case wishlistActions.GET_WISH_LIST_ITEMS_SUCCESS:
      let wishListNewItems = cloneDeep(state.wishlistItems);
      let wishListName = null;
      let wishListcount = null;
      if (action.wishlist && action.wishlist.products) {
        wishListNewItems = action.wishlist.products;
        wishListName = action.wishlist.name;
        wishListcount = action.wishlist.count;
      }
      return Object.assign({}, state, {
        status: action.status,
        wishlistItems: wishListNewItems,
        name: wishListName,
        count: wishListcount,
        loading: false
      });
    case wishlistActions.ADD_PRODUCT_TO_WISH_LIST_SUCCESS:
      currentWishlistItems = cloneDeep(state.wishlistItems);
      currentWishlistItems.push(action.product);
      return Object.assign({}, state, {
        status: action.status,
        wishlistItems: currentWishlistItems,
        loading: false
      });
    case wishlistActions.REMOVE_PRODUCT_FROM_WISH_LIST_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false
      });
    default:
      return state;
  }
};
export default wishlistItems;
