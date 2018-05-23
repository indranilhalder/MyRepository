import React from "react";
import ReactDOM from "react-dom";
import AppContainer from "../src/general/containers/AppContainer";
import thunk from "redux-thunk";
import { Provider } from "react-redux";
import { createStore, combineReducers, applyMiddleware } from "redux";
import "./index.css";
import { BrowserRouter } from "react-router-dom";
import user from "../src/auth/reducers/user.reducer";
import * as api from "../src/lib/apiRequest";
import modal from "../src/general/modal.reducers";
import toast from "../src/general/toast.reducers";
import secondaryLoader from "../src/general/secondaryLoader.reducers";
import feed from "../src/home/reducers/feed.reducer";
import search from "../src/search/reducers/search.reducer";
import registerServiceWorker from "./registerServiceWorker";
import productListings from "./plp/reducers/plp.reducer";
import productDescription from "./pdp/reducers/pdp.reducer";
import categoryDefault from "./clp/reducers/clp.reducer";
import brandDefault from "./blp/reducers/blp.reducer";
import profile from "./account/reducers/account.reducer";
import header from "../src/general/header.reducers.js";
import icid from "../src/general/icid.reducer.js";
import wishlistItems from "./wishlist/reducers/wishlist.reducer";
import auth from "./auth/reducers/auth.reducer";
import cart from "./cart/reducers/cart.reducer";
import Toast from "./general/components/Toast";
import delay from "lodash.delay";
import { TOAST_DELAY } from "./general/toast.actions";
import "intersection-observer";

const rootReducer = combineReducers({
  auth,
  user,
  modal,
  feed,
  productListings,
  productDescription,
  search,
  secondaryLoader,
  toast,
  cart,
  brandDefault,
  categoryDefault,
  profile,
  wishlistItems,
  header,
  icid
});

let store = createStore(
  rootReducer,
  applyMiddleware(
    thunk.withExtraArgument({
      api
    })
  )
);

ReactDOM.render(
  <Provider store={store}>
    <BrowserRouter>
      <AppContainer />
    </BrowserRouter>
  </Provider>,
  document.getElementById("root")
);

const displayToastFunc = message => {
  ReactDOM.render(
    <Toast data={message} autoRemove={true} />,
    document.getElementById("service-worker-toast-root")
  );
  delay(() => {
    document.getElementById("service-worker-toast-root").innerHTML = "";
  }, TOAST_DELAY);
};
registerServiceWorker(displayToastFunc);
