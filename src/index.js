import React from "react";
import ReactDOM from "react-dom";
import SignUpContainer from "../src/auth/containers/SignUpContainer";
import thunk from "redux-thunk";
import { Provider } from "react-redux";
import * as api from "../src/lib/apiRequest";
import { createStore, applyMiddleware, combineReducers } from "redux";
import { BrowserRouter } from "react-router-dom";
import user from "../src/auth/reducers/user.reducer";

const rootReducer = combineReducers({
  user
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
      <SignUpContainer />
    </BrowserRouter>
  </Provider>,
  document.getElementById("root")
);
