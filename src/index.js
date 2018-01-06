import React from "react";
import ReactDOM from "react-dom";
import AppContainer from "../src/general/containers/AppContainer";
import thunk from "redux-thunk";
import { Provider } from "react-redux";
import * as api from "../src/lib/apiRequest";
import { createStore, applyMiddleware, combineReducers } from "redux";
import { BrowserRouter } from "react-router-dom";
import user from "../src/auth/reducers/user.reducer";
import modal from "../src/general/modal.reducers";
import registerServiceWorker from "./registerServiceWorker";

const rootReducer = combineReducers({
  user,
  modal
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
registerServiceWorker();
