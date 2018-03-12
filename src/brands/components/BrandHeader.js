import React from "react";
import styles from "./BrandHeader.css";
import cancelIcon from "../../general/components/img/cancel.svg";
import searchIcon from "./img/Search.svg";
import iconImageURL from "../../general/components/img/arrowBack.svg";
import searchRedIcon from "./img/SearchRed.svg";
import PropTypes from "prop-types";
import { CircleButton, Icon } from "xelpmoc-core";
import Input2 from "../../general/components/Input2.js";
export default class BrandHeader extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      searchBar: false
    };
  }
  onClickBack() {
    if (this.props.onClickBack) {
      this.props.onClickBack();
    }
  }
  onSearch(val) {
    if (this.props.onSearch) {
      this.props.onSearch(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.InformationHeader}>
          <div className={styles.backHolder} onClick={() => this.onClickBack()}>
            <Icon image={iconImageURL} size={18} />
          </div>
          {!this.state.searchBar && (
            <div className={styles.searchWithText}>
              <div className={styles.textBox}>{this.props.text}</div>
              <div
                className={styles.searchHolder}
                onClick={() => this.setState({ searchBar: true })}
              >
                <Icon image={searchIcon} size={18} />
              </div>
            </div>
          )}
          {this.state.searchBar && (
            <div className={styles.searchWithInputRedHolder}>
              <div className={styles.searchRedHolder}>
                <Icon image={searchRedIcon} size={18} />
              </div>
              <div className={styles.input}>
                <Input2
                  onChange={val => this.onSearch(val)}
                  textStyle={{ fontSize: 14 }}
                  height={33}
                  isWhite={true}
                  borderColor={"#212121"}
                  borderBottom={"0px solid #212121"}
                />
              </div>
              <div
                className={styles.crossIcon}
                onClick={() => this.setState({ searchBar: false })}
              >
                <Icon image={cancelIcon} size={18} />
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}
BrandHeader.propTypes = {
  text: PropTypes.string,
  onClickBack: PropTypes.func,
  onSearch: PropTypes.func
};
BrandHeader.defaultProps = {
  text: "Mobile"
};
