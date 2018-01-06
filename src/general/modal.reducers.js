import * as modalActions from "./modal.actions.js";

const modal = (
  state = {
    modalDisplayed: false,
    modalType: null
  },
  action
) => {
  switch (action.type) {
    case modalActions.SHOW_MODAL:
      return Object.assign({}, state, {
        modalDisplayed: true,
        modalType: action.modalType,
        ownProps: action.ownProps
      });
    case modalActions.HIDE_MODAL:
      return Object.assign({}, state, {
        modalDisplayed: false,
        modalType: null,
        ownProps: null
      });
    default:
      return state;
  }
};
export default modal;
