import { connect } from "react-redux";
import EmiModal from "../components/EmiModal";
import { withRouter } from "react-router-dom";
import { hideModal } from "../../general/modal.actions";
const mapDispatchToProps = dispatch => {
  return {
    closeModal: () => {
      dispatch(hideModal());
    }
  };
};

const mapStateToProps = state => {
  return {
    emiData: state.productDescription.emiResult
  };
};

const EmiListContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(EmiModal)
);

export default EmiListContainer;
