import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import ReturnToStore from "../components/ReturnToStore";
const mapDispatchToProps = dispatch => {
  return {
    getAllStores: () => {
      return false;
    }
  };
};

const mapStateToProps = state => {
  return {
    state
  };
};

const ReturnToStoreContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ReturnToStore)
);
export default ReturnToStoreContainer;
