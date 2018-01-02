export const MOBILE_MODEL_TYPE_SHOW = "MOBILE_MODEL_TYPE_SHOW";
export const MOBILE_MODEL_TYPE_HIDE = "MOBILE_MODEL_TYPE_HIDE";

// Actions for Creating User
export function mobileModelTypeShow(modelType, ownProps) {
  return {
    type: MOBILE_MODEL_TYPE_SHOW,
    modelType,
    ownProps
  };
}

export function mobileModelTypeHide(modelType) {
  return {
    type: MOBILE_MODEL_TYPE_HIDE,
    modelType
  };
}
