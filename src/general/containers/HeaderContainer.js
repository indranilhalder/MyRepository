import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import HeaderWrapper from "../components/HeaderWrapper";
import { setHeaderText } from "../header.actions.js";
import { showFilter, hideFilter } from "../../plp/actions/plp.actions.js";
const mapDispatchToProps = dispatch => {
  return {
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    showFilter: () => {
      dispatch(showFilter());
    },
    hideFilter: () => {
      dispatch(hideFilter());
    }
  };
};
const mapStateToProps = state => {
  return {
    headerText: state.header.text,
    isPlpFilterOpen: state.productListings.isFilterOpen,
    bagCount: state.header.bagCount
  };
};

const HeaderContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(HeaderWrapper)
);
export default HeaderContainer;
