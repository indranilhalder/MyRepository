import React from "react";
import PdpLink from "./PdpLink";
import { sellers, winningSellerText } from "./ProductDescriptionPage.css";
export default class OtherSellersLink extends React.Component {
  getValidSellerCount = () => {
    const validSellersCount = this.props.otherSellers
      ? this.props.otherSellers.filter(val => {
          return parseInt(val.availableStock, 10) > 0;
        }).length
      : 0;
    return validSellersCount;
  };
  renderLink = () => {
    return (
      <div className={sellers}>
        Sold by{" "}
        <span className={winningSellerText}>{this.props.winningSeller}</span>
        {this.getValidSellerCount() !== 0 && (
          <React.Fragment>
            {" "}
            and {this.getValidSellerCount()} other seller(s)
          </React.Fragment>
        )}
      </div>
    );
  };
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    if (this.props.winningSeller) {
      return (
        <PdpLink
          noLink={this.getValidSellerCount() === 0}
          onClick={this.handleClick}
        >
          {this.renderLink()}
        </PdpLink>
      );
    } else {
      return null;
    }
  }
}
