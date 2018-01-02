import * as modelActions from "../actions/model.action";

const model = (
  state = {
    modelType: null,
    showModel: false,
    ownProps: null
  },
  action
) => {
  switch (action.type) {
    case modelActions.MOBILE_MODEL_TYPE_SHOW: {
      return Object.assign({}, state, {
        modelType: action.modelType,
        ownProps: action.ownProps,
        showModel: true
      });
    }
    case modelActions.MOBILE_MODEL_TYPE_HIDE: {
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
