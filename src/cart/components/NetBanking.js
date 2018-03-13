import React from "react";
import GridSelect from "../../general/components/GridSelect";
import BankSelect from "./BankSelect";
import PropTypes from "prop-types";

export default class NetBanking extends React.Component {
  handleSelect(val) {
    if (this.props.onSelect) {
      this.props.onSelect(val);
    }
  }
  render() {
    return (
      <GridSelect
        limit={1}
        offset={30}
        elementWidthMobile={33.3333}
        onSelect={val => this.handleSelect(val)}
        selected={this.props.selected}
      >
        {this.props.bankList &&
          this.props.bankList.map((val, i) => {
            return (
              <BankSelect image={val.image} value={val.bankCode} key={i} />
            );
          })}
      </GridSelect>
    );
  }
}
NetBanking.propTypes = {
  bankList: PropTypes.arrayOf(
    PropTypes.shape({ image: PropTypes.string, value: PropTypes.string })
  ),
  onSelect: PropTypes.func,
  selected: PropTypes.arrayOf(PropTypes.string)
};
