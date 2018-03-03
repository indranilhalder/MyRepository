import React from "react";
import PdpElectronics from "./PdpElectronics";
import ProductDescriptionPage from "./ProductDescriptionPage";
import {
  PRODUCT_DESCRIPTION_PRODUCT_CODE,
  PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
} from "../../lib/constants";
// prettier-ignore
const typeComponentMapping = {
  "Electronics": props => <PdpElectronics {...props} />,
  "FashionJewellery":props => <ProductDescriptionPage {...props} />,
  "Clothing":props => <ProductDescriptionPage {...props} />
};
export default class ProductDescriptionPageWrapper extends React.Component {
  componentDidMount() {
    if (this.props.match.path === PRODUCT_DESCRIPTION_PRODUCT_CODE) {
      this.props.getProductDescription(this.props.match.params[0]);
      this.props.getMsdRequest(this.props.match.params[0]);
    } else if (
      this.props.match.path === PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE
    ) {
      this.props.getProductDescription(this.props.match.params[2]);
      this.props.getMsdRequest(this.props.match.params[2]);
    } else {
      //need to show error page
    }
  }

  renderRootCategory = datumType => {
    return (
      <React.Fragment>
        {typeComponentMapping[datumType]({ ...this.props })}
      </React.Fragment>
    );
  };

  render() {
    if (this.props.productDetails) {
      return (
        <div>
          {this.renderRootCategory(this.props.productDetails.rootCategory)}
        </div>
      );
    } else {
      return null;
    }
  }
}
