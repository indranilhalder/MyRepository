export const SHOW_MODAL = "SHOW_MODAL";
export const HIDE_MODAL = "HIDE_MODAL";

// Actions for Creating User
export function showModal(modelType, ownProps) {
  return {
    type: SHOW_MODAL,
    modelType,
    ownProps
  };
}

export function hideModal(modelType) {
  return {
    type: HIDE_MODAL,
    modelType
  };
}
