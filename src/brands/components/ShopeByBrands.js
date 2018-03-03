import React from "react";
import ShopeByBrandLists from "./ShopeByBrandLists.js";
import styles from "./ShopeByBrands.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
const numberOfVisibleItem = 8;
export default class ShopeByBrands extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showAll: false,
      label: `view  ${this.props.brandLists.length -
        numberOfVisibleItem} more brands`
    };
  }
  showMore() {
    this.setState({ showAll: !this.state.showAll }, () => {
      if (this.state.label === "View Less") {
        this.setState({
          label: `view  ${this.props.brandLists.length - 8} more brands`
        });
      } else {
        this.setState({ label: "View Less" });
      }
    });
  }
  handleClick(val) {
    if (this.props.onClick) {
      this.props.onClick(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>{this.props.brandHeader}</div>
        <div className={styles.bradListHolder}>
          {this.props.brandLists &&
            this.props.brandLists
              .filter((val, i) => {
                return !this.state.showAll ? i < 8 : true;
              })
              .map((val, i) => {
                return (
                  <ShopeByBrandLists
                    brandList={val.list}
                    key={i}
                    value={val.list}
                    onClick={val => this.handleClick(val)}
                  />
                );
              })}
        </div>
        {this.props.brandLists.length > 8 && (
          <div className={styles.moreListButtonHolder}>
            <div className={styles.buttonHolder}>
              <UnderLinedButton
                size="14px"
                fontFamily="regular"
                color="#ff1744"
                label={this.state.label}
                onClick={() => this.showMore()}
              />
            </div>
          </div>
        )}
      </div>
    );
  }
}
ShopeByBrands.propTypes = {
  onClick: PropTypes.func,
  brandHeader: PropTypes.string,
  brandLists: PropTypes.arrayOf(
    PropTypes.shape({
      list: PropTypes.string
    })
  )
};
