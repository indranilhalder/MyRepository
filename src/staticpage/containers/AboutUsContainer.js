import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import AboutUs from "../components/AboutUs.js";
import { getAboutUsDetails } from "../actions/staticPage.actions";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = dispatch => {
  return {
    getAboutUsDetails: () => {
      dispatch(getAboutUsDetails());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};

const mapStateToProps = state => {
  return {
    aboutUs: state.staticPage.aboutUs,
    aboutUsStatus: state.staticPage.aboutUsStatus,
    aboutUsLoading: state.staticPage.aboutUsLoading
  };
};

const AboutUsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AboutUs)
);

export default AboutUsContainer;
