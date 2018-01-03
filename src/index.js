import React from "react";
import ReactDOM from "react-dom";
import modals from "./reducers/modals.reducer.js";
import { Provider } from "react-redux";
import { createStore, combineReducers } from "redux";
import "./index.css";
import AppContainer from "./containers/AppContainer";

import registerServiceWorker from "./registerServiceWorker";

const rootReducer = combineReducers({
  modals
});
let store = createStore(rootReducer);

ReactDOM.render(
  <Provider store={store}>
    <AppContainer />
  </Provider>,
  document.getElementById("root")
);
registerServiceWorker();
