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
        scrollPosition: action.scrollPosition,
        ownProps: action.ownProps
      });
    case modalActions.HIDE_MODAL:
      window.scrollTo(0, state.scrollPosition);
      setTimeout(() => {
        window.scrollTo(0, state.scrollPosition);
      }, 0);

      return Object.assign({}, state, {
        modalDisplayed: false,
        modalType: null,
        scrollPosition: 0,
        ownProps: null
      });
    default:
      return state;
  }
};
export default modal;
