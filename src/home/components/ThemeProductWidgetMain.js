import React from "react";
import Carousel from "../../general/components/Carousel";
import ThemeProductWidget from "../../general/components/ThemeProductWidget";
import Logo from "../../general/components/Logo";
import PropTypes from "prop-types";
import styles from "./ThemeProductWidgetMain.css";
export default class ThemeProductWidgetMain extends React.Component {
  render() {
    return (
      <div
        className={styles.base}
        style={{
          backgroundColor: this.props.backgroundColor,
          backgroundImage: `url(${this.props.backgroundImage})`
        }}
      >
        <div className={styles.logo}>
          <Logo image={this.props.logo} />
        </div>
        <Carousel
          header={this.props.header}
          buttonText="Shop all"
          seeAll={this.props.seeAll}
          elementWidthMobile={45}
          withFooter={false}
          isWhite={true}
        >
          {this.props.data &&
            this.props.data.map((datum, i) => {
              return (
                <ThemeProductWidget
                  image={datum.image}
                  label={datum.label}
                  price={datum.price}
                  key={i}
                  isWhite={true}
                />
              );
            })}
        </Carousel>
      </div>
    );
  }
}
ThemeProductWidgetMain.propTypes = {
  header: PropTypes.string,
  label: PropTypes.string,
  price: PropTypes.string,
  image: PropTypes.string,
  backgroundColor: PropTypes.string,
  logo: PropTypes.string,
  isWhite: PropTypes.bool,
  elementWidthMobile: PropTypes.number
};
ThemeProductWidgetMain.defaultProps = {
  header: "New arrivals"
};
