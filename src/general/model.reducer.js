import * as modelActions from "./model.action.js";

const model = (
  state = {
    modelType: null,
    showModel: false,
    ownProps: null
  },
  action
) => {
  switch (action.type) {
    case modelActions.SHOW_MODAL: {
      return Object.assign({}, state, {
        modelType: action.modelType,
        ownProps: action.ownProps,
        showModel: true
      });
    }
    case modelActions.HIDE_MODAL: {
      return Object.assign({}, state, {
        modelType: action.modelType,
        ownProps: null,
        showModel: false
      });
    }
    default:
      return state;
  }
};

export default model;
