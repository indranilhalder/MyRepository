import React from "react";
import styles from "./SearchHeader.css";
import cancelIcon from "../../general/components/img/cancel.svg";
import searchIcon from "./img/search.svg";
import iconImageURL from "../../general/components/img/arrowBack.svg";
import searchRedIcon from "./img/searchRed.svg";
import searchwhiteIcon from "./img/searchwhite.svg";
import PropTypes from "prop-types";
import Icon from "../../xelpmoc-core/Icon";
import Input2 from "../../general/components/Input2.js";
import companyLogo from "../../general/components/img/group.svg";
export default class SearchHeader extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      autoFocus: true,
      isWhite: true,
      isRed: false
    };
  }
  onClickBack() {
    if (this.props.onClickBack) {
      this.props.onClickBack();
    }
  }
  onTypedSearch(val) {
    if (this.props.onSearch) {
      this.props.onSearch(val);
    }
    if (val.length > 0) {
      this.setState({ isWhite: false, isRed: true });
    }
    if (val.length === 0) {
      this.setState({ isWhite: true, isRed: false });
    }
  }
  redirectToHome() {
    if (this.props.redirectToHome) {
      this.props.redirectToHome();
    }
  }
  searchString = () => {
    if (this.props.onSearchString) {
      this.props.onSearchString(this.props.searchString);
    }
  };
  handleKeyUp = val => {
    if (val === "Enter") {
      this.searchString();
    }
  };

  onClickIcon() {
    this.props.onSearchOrCloseIconClick();
    this.setState({ isWhite: true, isRed: false });
  }
  render() {
    let search = searchIcon;
    if (this.props.display) {
      search = cancelIcon;
    }
    let InformationHeader = styles.InformationHeader;
    if (this.props.isGoBack && this.props.isLogo) {
      InformationHeader = styles.logoPresentStyle;
    }
    let logoHolder = styles.logoHolder;
    if (this.props.isGoBack) {
      logoHolder = styles.iconWithLogo;
    }
    return (
      <div className={styles.base}>
        <div className={InformationHeader}>
          {this.props.isGoBack &&
            !this.props.display && (
              <div
                className={styles.backHolder}
                onClick={() => this.onClickBack()}
              >
                <Icon image={iconImageURL} size={16} />
              </div>
            )}
          {this.props.isLogo &&
            !this.props.display && (
              <div className={logoHolder} onClick={() => this.redirectToHome()}>
                <Icon image={companyLogo} size={35} />
              </div>
            )}
          <div
            className={styles.searchHolder}
            onClick={() => this.onClickIcon()}
          >
            <Icon image={search} size={16} />
          </div>
          {!this.props.display && (
            <div className={styles.searchWithText}>
              <h1 className={styles.textBox}>{this.props.text}</h1>
            </div>
          )}
          {this.props.display && (
            <div className={styles.searchWithInputRedHolder}>
              <div
                className={styles.searchRedHolder}
                onClick={() => {
                  this.searchString();
                }}
              >
                {this.state.isWhite && (
                  <Icon image={searchwhiteIcon} size={16} />
                )}
                {this.state.isRed && <Icon image={searchRedIcon} size={16} />}
              </div>
              <div className={styles.input}>
                <Input2
                  onChange={val => this.onTypedSearch(val)}
                  placeholder="What are you looking for?"
                  textStyle={{ fontSize: 14 }}
                  height={30}
                  isWhite={true}
                  borderColor={"#212121"}
                  borderBottom={"0px solid #212121"}
                  onKeyUp={event => this.handleKeyUp(event.key)}
                  autoFocus={this.state.autoFocus}
                />
              </div>
            </div>
          )}
        </div>
      </div>
    );
  }
}
SearchHeader.propTypes = {
  text: PropTypes.string,
  onClickBack: PropTypes.func,
  onSearch: PropTypes.func,
  canGoBack: PropTypes.bool,
  redirectToHome: PropTypes.func
};
SearchHeader.defaultProps = {
  text: "Mobile",
  canGoBack: true
};
