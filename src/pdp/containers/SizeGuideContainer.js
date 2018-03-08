import { connect } from "react-redux";
import SizeGuideMain from "../components/SizeGuideMain";
import { withRouter } from "react-router-dom";
import { getProductSizeGuide } from "../actions/pdp.actions.js";

const mapStateToProps = state => {
  console.log("SIZE GUIDE CONTAINER MAP STATE TO PROPS");
  console.log(state.productDescription);
  return {
    sizeData: {
      type: "sizeGuideWsDTO",
      status: "Success",
      imageURL:
        "//assets.tatacliq.com/medias/sys_master/images/8797255204894.jpg",
      sizeGuideList: [
        {
          dimensionSize: "Extra Small(XS)",
          dimensionList: [
            {
              dimension: "Chest/Bust",
              dimensionValue: "36",
              dimensionUnit: '"'
            },
            {
              dimension: "Shoulder",
              dimensionValue: "14",
              dimensionUnit: '"'
            },
            {
              dimension: "Waist",
              dimensionValue: "33",
              dimensionUnit: '"'
            }
          ]
        },
        {
          dimensionSize: "Small(S)",
          dimensionList: [
            {
              dimension: "Chest/Bust",
              dimensionValue: "36",
              dimensionUnit: '"'
            },
            {
              dimension: "Chest/Bust",
              dimensionValue: "36",
              dimensionUnit: '"'
            },
            {
              dimension: "Chest/Bust",
              dimensionValue: "36",
              dimensionUnit: '"'
            }
          ]
        }
      ]
    },
    loading: state.productDescription.sizeGuide.loading,
    productCode: state.productDescription.productDetails.productListingId
  };
};

const mapDispatchToProps = dispatch => {
  return {
    getSizeGuide: productCode => {
      dispatch(getProductSizeGuide(productCode));
    }
  };
};

const SizeGuideContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SizeGuideMain)
);

export default SizeGuideContainer;
