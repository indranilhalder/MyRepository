import React from "react";
import MediaQuery from "react-responsive";
import { default as styles } from "./Carousel.css";
import Button from "./Button";
import PropTypes from "prop-types";
export default class Carousel extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      position: 0
    };
  }
  slideBack() {
    if (this.state.position > 0) {
      const position = this.state.position - 1;
      this.setState({ position });
    }
  }
  slideForward() {
    const visibleChildren = Math.floor(100 / this.props.elementWidthDesktop);
    if (
      this.state.position <
      React.Children.count(this.props.children) - visibleChildren
    ) {
      const position = this.state.position + 1;
      this.setState({ position });
    }
  }
  render() {
    const childrenCount = React.Children.count(this.props.children);
    const visibleChildren = Math.floor(100 / this.props.elementWidthDesktop);
    const translationAmount = -(
      this.props.elementWidthDesktop * this.state.position
    );
    const transform = `translateX(${translationAmount}%)`;
    const style = {
      transform: transform
    };
    let headerClass = styles.header;
    let buttonClass = styles.button;
    let buttonColor = "#212121";
    let buttonSpace = 10;
    if (this.props.isWhite) {
      headerClass = styles.headerWhite;
      buttonClass = styles.buttonWhite;
      buttonColor = "#fff";
    }
    if (this.props.seeAll && !this.props.withFooter) {
      buttonSpace = 110;
    }

    return (
      <div className={styles.base} styles={{ color: this.props.color }}>
        <MediaQuery query="(min-device-width: 1025px)">
          <div className={headerClass}>
            <div>
              <div>{this.props.header}</div>
              {this.props.subheader && (
                <div className={styles.subheader}>{this.props.subheader}</div>
              )}
            </div>
            <div className={styles.nav}>
              {this.props.seeAll && (
                <div
                  className={buttonClass}
                  onClick={() => {
                    this.props.seeAll();
                  }}
                >
                  {this.props.buttonText}
                </div>
              )}
              {childrenCount > visibleChildren && (
                <React.Fragment>
                  <div
                    className={styles.back}
                    onClick={() => {
                      this.slideBack();
                    }}
                  />
                  <div
                    className={styles.forward}
                    onClick={() => {
                      this.slideForward();
                    }}
                  />
                </React.Fragment>
              )}
            </div>
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1024px)">
          {this.props.header && (
            <div className={headerClass} style={{ paddingRight: buttonSpace }}>
              <div>{this.props.header}</div>
              {this.props.subheader && (
                <div className={styles.subheader}>{this.props.subheader}</div>
              )}
              {this.props.seeAll &&
                !this.props.withFooter && (
                  <div className={styles.mobileButton}>
                    <Button
                      label={this.props.buttonText}
                      type="hollow"
                      color={buttonColor}
                      width={100}
                      onClick={() => {
                        this.props.seeAll();
                      }}
                    />
                  </div>
                )}
            </div>
          )}
        </MediaQuery>

        <div className={styles.sliderHolder}>
          <div className={styles.slider} style={style}>
            {this.props.children &&
              this.props.children.map((child, i) => {
                return (
                  <React.Fragment key={i}>
                    <MediaQuery query="(min-device-width: 1025px)">
                      <div
                        className={styles.element}
                        style={{ width: `${this.props.elementWidthDesktop}%` }}
                      >
                        {child}
                      </div>
                    </MediaQuery>
                    <MediaQuery query="(max-device-width: 1024px)">
                      <div
                        className={styles.element}
                        style={{ width: `${this.props.elementWidthMobile}%` }}
                      >
                        {child}
                      </div>
                    </MediaQuery>
                  </React.Fragment>
                );
              })}
          </div>
        </div>
        <MediaQuery query="(max-device-width: 1024px)">
          {this.props.seeAll &&
            this.props.withFooter && (
              <div className={styles.footer}>
                <Button
                  label={this.props.buttonText}
                  type="hollow"
                  color={buttonColor}
                  width={120}
                  onClick={() => {
                    this.props.seeAll();
                  }}
                />
              </div>
            )}
        </MediaQuery>
      </div>
    );
  }
}

Carousel.propTypes = {
  elementWidthDesktop: PropTypes.number,
  elementWidthMobile: PropTypes.number,
  buttonText: PropTypes.string,
  header: PropTypes.string,
  isWhite: PropTypes.bool,
  seeAll: PropTypes.func,
  withFooter: PropTypes.bool
};

Carousel.defaultProps = {
  elementWidthDesktop: 25,
  elementWidthMobile: 45,
  buttonText: "Shop all",
  color: "#181818",
  withFooter: true
};
