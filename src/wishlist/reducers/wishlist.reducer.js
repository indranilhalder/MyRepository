import cloneDeep from "lodash/cloneDeep";
import * as wishlistActions from "../actions/wishlist.actions";
import { SUCCESS } from "../../lib/constants";
const wishlistItems = (
  state = {
    wishlistItems: [],
    loading: false,
    status: null,
    error: null
  },
  action
) => {
  let currentWishlistItems, indexToBeRemove;
  switch (action.type) {
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
      return Object.assign({}, state, {
        status: action.status,
        wishlistItems: action.wishlistItems,
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
      currentWishlistItems = cloneDeep(state.wishlistItems);
      indexToBeRemove = currentWishlistItems.findIndex(item => {
        return item.ussId === action.product.ussid;
      });
      currentWishlistItems.splice(indexToBeRemove, 1);
      return Object.assign({}, state, {
        status: action.status,
        wishlistItems: currentWishlistItems,
        loading: false
      });
    default:
      return state;
  }
};
export default wishlistItems;
