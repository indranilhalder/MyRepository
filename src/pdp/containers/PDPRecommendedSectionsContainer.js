import { connect } from "react-redux";
import PDPRecommendedSections from "../components/PDPRecommendedSections";
import { getMsdRequest, pdpAboutBrand } from "../actions/pdp.actions.js";
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

const PDPRecommendedSectionsContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(PDPRecommendedSections);
export default PDPRecommendedSectionsContainer;
