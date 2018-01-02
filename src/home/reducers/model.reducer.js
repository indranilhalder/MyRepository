import * as modelActions from "../actions/model.action";

const model = (
  state = {
    type: null,
    visibility: false,
    ownProps: null
  },
  action
) => {
  switch (action.type) {
    case modelActions.MOBILE_MODEL_TYPE_SHOW: {
      return Object.assign({}, state, {
        modelType: action.modelType,
        visibility: action.visibility,
        ownProps: action.ownProps
      });
    }
    case modelActions.MOBILE_MODEL_TYPE_HIDE: {
      return Object.assign({}, state, {
        modelType: action.modelType,
        visibility: false,
        ownProps: null
      });
    }
    default:
      return state;
  }
};

export default model;
