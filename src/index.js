import React from "react";
import ReactDOM from "react-dom";
import modals from "./general/modal.reducers.js";
import { Provider } from "react-redux";
import { createStore, combineReducers } from "redux";
import "./index.css";
import AppContainer from "./general/containers/AppContainer";
import { BrowserRouter } from "react-router-dom";

import registerServiceWorker from "./registerServiceWorker";

const rootReducer = combineReducers({
  modals
});
let store = createStore(rootReducer);

ReactDOM.render(
  <Provider store={store}>
    <BrowserRouter>
      <AppContainer />
    </BrowserRouter>
  </Provider>,
  document.getElementById("root")
);
registerServiceWorker();
