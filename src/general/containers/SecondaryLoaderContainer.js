import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import SecondaryLoader from "../components/SecondaryLoader";

// const mapDispatchToProps = dispatch => {
//   return {
//     setHeaderText: text => {
//       dispatch(setHeaderText(text));
//     }
//   };
// };
const mapStateToProps = state => {
  console.log(state);
  return {
    display: state.secondaryLoader.display
  };
};

const SecondaryLoaderContainer = withRouter(
  connect(mapStateToProps)(SecondaryLoader)
);
export default SecondaryLoaderContainer;
