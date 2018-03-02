import React from "react";
import styles from "./BrandHeader.css";
import InformationHeader from "../../general/components/InformationHeader";
import searchIcon from "./img/Search.svg";
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
  onClickSearch() {
    this.setState({ searchBar: true });
  }
  onSearch(val) {
    if (this.props.onSearch) {
      this.props.onSearch(val);
    }
  }
  render() {
    let text = this.props.text;
    if (this.state.searchBar) {
      text = "";
    }

    return (
      <div className={styles.base}>
        <div className={styles.InformationHeader}>
          <InformationHeader text={text} onClick={() => this.onClickBack()} />
          {!this.state.searchBar && (
            <div
              className={styles.searchHolder}
              onClick={() => this.onClickSearch()}
            >
              <Icon image={searchIcon} size={22} />
            </div>
          )}
          {this.state.searchBar && (
            <div className={styles.searchWithInputRedHolder}>
              {/* <div className={styles.searchRedHolder}>
                <Icon image={searchRedIcon} size={22} />
              </div> */}
              <div className={styles.input}>
                <Input2
                  onChange={val => this.onSearch(val)}
                  textStyle={{ fontSize: 14 }}
                  height={33}
                  isWhite={true}
                  borderColor={"#212121"}
                  borderBottom={"0px solid #212121"}
                  leftChild={<Icon image={searchRedIcon} size={22} />}
                />
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}
BrandHeader.propTypes = {
  underlineButtonColour: PropTypes.string,
  underlineButtonLabel: PropTypes.string,
  buttonLabel: PropTypes.string,
  replaceItem: PropTypes.func,
  writeReview: PropTypes.func
};
BrandHeader.defaultProps = {
  text: "Mobile"
};
