import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import HelpDetails from "../components/HelpDetails";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = dispatch => {
  return {
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};

const HelpDetailsContainer = withRouter(
  connect(null, mapDispatchToProps)(HelpDetails)
);

export default HelpDetailsContainer;
