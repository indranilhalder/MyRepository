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

const PRODUCT_CODE_REGEX = /(.*)/;
export default class ProductDescriptionPageWrapper extends React.Component {
  componentDidMount() {
    console.log("IN COMPONENT DID MOUNT");
    console.log(this.props.match);

    this.props.getProductDescription(this.props.match.params[2]);
    this.props.getMsdRequest(this.props.match.params[2]);
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
