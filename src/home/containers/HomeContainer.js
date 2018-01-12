import { connect } from "react-redux";
import { homeFeed } from "../actions/home.actions";
import Home from "../../auth/components/Login";
import { withRouter } from "react-router-dom";
const mapDispatchToProps = dispatch => {
  return {
    homeFeed: () => {
      dispatch(homeFeed());
    }
  };
};

const mapStateToProps = state => {
  return {
    data: state.home.data
  };
};

const HomeContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Home)
);

export default HomeContainer;
