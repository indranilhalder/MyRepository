import { connect } from "react-redux";
import PDPRecommendedSections from "../components/PDPRecommendedSections";

const mapStateToProps = state => {
  return {
    msdItems: state.productDescription.msdItems,
    aboutTheBrand: state.productDescription.aboutTheBrand
  };
};

const PDPRecommendedSectionsContainer = connect(mapStateToProps, null)(
  PDPRecommendedSections
);
export default PDPRecommendedSectionsContainer;
