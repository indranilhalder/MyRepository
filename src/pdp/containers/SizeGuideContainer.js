import { connect } from "react-redux";
import SizeGuideMain from "../components/SizeGuideMain";
import { withRouter } from "react-router-dom";

const mapStateToProps = state => {
  return {
    sizeData: state.productDescription.sizeGuide
  };
};

const SizeGuideContainer = withRouter(connect(mapStateToProps)(SizeGuideMain));

export default SizeGuideContainer;
