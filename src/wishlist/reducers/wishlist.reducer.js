import cloneDeep from "lodash/cloneDeep";
import * as wishlistActions from "../actions/wishlist.actions";
import { SUCCESS } from "../../lib/constants";
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
      console.log("GET WISH LIST ITEMS FAILURE");
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        error: action.error
      });
    case wishlistActions.GET_WISH_LIST_ITEMS_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        wishlistItems: action.wishlist.products,
        name: action.wishlist.name,
        count: action.wishlist.count,
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
        return item.winningUssID === action.product.winningUssID;
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
