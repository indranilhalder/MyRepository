import { connect } from "react-redux";
import CategoriesPage from "../components/CategoriesPage";
import { getCategories } from "../actions/clp.actions";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = dispatch => {
  return {
    getCategories: () => {
      dispatch(getCategories());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};
const mapStateToProps = state => {
  return {
    categories: state.categoryDefault.categories
  };
};
const CategoriesPageContainer = connect(mapStateToProps, mapDispatchToProps)(
  CategoriesPage
);
export default CategoriesPageContainer;
