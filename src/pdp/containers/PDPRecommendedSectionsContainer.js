import { connect } from "react-redux";
import PDPRecommendedSections from "../components/PDPRecommendedSections";
import { pdpAboutBrand, getMsdRequest } from "../actions/pdp.actions.js";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    getMsdRequest: productCode => {
      dispatch(getMsdRequest(productCode));
    },
    pdpAboutBrand: productCode => {
      dispatch(pdpAboutBrand(productCode));
    }
  };
};

const mapStateToProps = state => {
  return {
    msdItems: state.productDescription.msdItems,
    aboutTheBrand: state.productDescription.aboutTheBrand
  };
};

const PDPRecommendedSectionsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(PDPRecommendedSections)
);
export default PDPRecommendedSectionsContainer;
