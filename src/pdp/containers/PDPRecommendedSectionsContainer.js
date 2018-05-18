import { connect } from "react-redux";
import PDPRecommendedSections from "../components/PDPRecommendedSections";
import {
  pdpAboutBrand,
  getMsdRequest,
  setToOld
} from "../actions/pdp.actions.js";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    getMsdRequest: productCode => {
      dispatch(getMsdRequest(productCode));
    },
    pdpAboutBrand: productCode => {
      dispatch(pdpAboutBrand(productCode));
    },
    setToOld: () => {
      dispatch(setToOld());
    }
  };
};

const mapStateToProps = state => {
  return {
    msdItems: state.productDescription.msdItems,
    aboutTheBrand: state.productDescription.aboutTheBrand,
    visitedNewProduct: state.productDescription.visitedNewProduct
  };
};

const PDPRecommendedSectionsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(PDPRecommendedSections)
);
export default PDPRecommendedSectionsContainer;
