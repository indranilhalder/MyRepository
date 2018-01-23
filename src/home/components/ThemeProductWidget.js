import React from "react";
import Carousel from "../../general/components/Carousel";
import ThemeProduct from "../../general/components/ThemeProduct";
import Logo from "../../general/components/Logo";
import PropTypes from "prop-types";
import styles from "./ThemeProductWidget.css";
export default class ThemeProductWidget extends React.Component {
  handleClick() {
    if (this.props.seeAll) {
      this.props.seeAll();
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `url(${this.props.backgroundImage})`
        }}
      >
        <div className={styles.overlay} />
        <div className={styles.logo}>
          <Logo image={this.props.logo} />
        </div>
        <Carousel
          header={this.props.header}
          buttonText="Shop all"
          seeAll={this.handleClick}
          elementWidthMobile={45}
          withFooter={false}
          isWhite={true}
        >
          {this.props.data &&
            this.props.data.map((datum, i) => {
              return (
                <ThemeProduct
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
ThemeProductWidget.propTypes = {
  header: PropTypes.string,
  backgroundImage: PropTypes.string,
  logo: PropTypes.string,
  seeAll: PropTypes.func,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      label: PropTypes.string,
      price: PropTypes.string,
      image: PropTypes.string
    })
  )
};
ThemeProductWidget.defaultProps = {
  header: "New arrivals"
};
