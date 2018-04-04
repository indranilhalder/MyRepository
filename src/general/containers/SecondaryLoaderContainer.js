import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import SecondaryLoader from "../components/SecondaryLoader";

const mapStateToProps = state => {
  return {
    display: state.secondaryLoader.display
  };
};

const SecondaryLoaderContainer = withRouter(
  connect(mapStateToProps)(SecondaryLoader)
);
export default SecondaryLoaderContainer;
