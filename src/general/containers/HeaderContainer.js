import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import HeaderWrapper from "../components/HeaderWrapper";
import { setHeaderText, clearHeaderText } from "../header.actions.js";
const mapDispatchToProps = dispatch => {
  return {
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    clearHeaderText: () => {
      dispatch(clearHeaderText());
    }
  };
};
const mapStateToProps = state => {
  return {
    headerText: state.header.text
  };
};

const HeaderContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(HeaderWrapper)
);
export default HeaderContainer;
