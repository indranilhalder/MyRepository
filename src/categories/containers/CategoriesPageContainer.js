import { connect } from "react-redux";
import CategoriesPage from "../components/CategoriesPage";
import { getCategories } from "../actions/categories.actions";
const mapDispatchToProps = dispatch => {
  return {
    getCategories: () => {
      dispatch(getCategories());
    }
  };
};
const mapStateToProps = state => {
  return {
    categories: state.categories.categories
  };
};
const CategoriesPageContainer = connect(mapStateToProps, mapDispatchToProps)(
  CategoriesPage
);
export default CategoriesPageContainer;
