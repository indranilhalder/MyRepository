import * as secondaryLoaderActions from "./secondaryLoader.actions.js";

const secondaryLoader = (
  state = {
    display: false
  },
  action
) => {
  switch (action.type) {
    case secondaryLoaderActions.SHOW_LOADER:
      return Object.assign({}, state, {
        display: true
      });
    case secondaryLoaderActions.HIDE_LOADER:
      return Object.assign({}, state, {
        display: false
      });
    default:
      return state;
  }
};
export default secondaryLoader;
