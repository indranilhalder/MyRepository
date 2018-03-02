import React from "react";
import PdpElectronics from "./PdpElectronics";
import ProductDescriptionPage from "./ProductDescriptionPage";
// prettier-ignore
const typeComponentMapping = {
  "Electronics": props => <PdpElectronics {...props} />,
  "FashionJewellery":props => <ProductDescriptionPage {...props} />,
  "Clothing":props => <ProductDescriptionPage {...props} />
};
export default class ProductDescriptionPageWrapper extends React.Component {
  componentDidMount() {
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
