import React from "react";
import PdpElectronics from "./PdpElectronics";
import PdpApparel from "./PdpApparel";
import ProductDescriptionPage from "./ProductDescriptionPage";
// prettier-ignore
const typeComponentMapping = {
  "Electronics": props => <PdpElectronics {...props} />,
  "FashionJewellery":props => <ProductDescriptionPage {...props} />,
  "Clothing":props => <PdpApparel {...props} />
};
export default class ProductDescriptionPageWrapper extends React.Component {
  componentDidMount() {
    this.props.getProductDescription(this.props.match.params[2]);
  }

  renderRootCategory = (datumType, productDetails, history) => {
    return (
      <React.Fragment>
        {typeComponentMapping[datumType](productDetails, history)}
      </React.Fragment>
    );
  };

  render() {
    if (this.props.productDetails) {
      return (
        <div>
          {this.renderRootCategory(
            this.props.productDetails.rootCategory,
            this.props.productDetails,
            this.props.history
          )}
        </div>
      );
    } else {
      return null;
    }
  }
}
