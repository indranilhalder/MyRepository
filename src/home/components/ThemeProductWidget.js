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
    const data = this.props.feedComponentData.data;
    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `url(${data.backgroundImageURL})`
        }}
      >
        <div className={styles.overlay} />
        <div className={styles.logo}>
          <Logo image={data.brandLogo} />
        </div>
        <Carousel
          header={data.title}
          buttonText="Shop all"
          seeAll={this.handleClick}
          elementWidthMobile={45}
          withFooter={false}
          isWhite={true}
        >
          {data.items &&
            data.items.map((datum, i) => {
              return (
                <ThemeProduct
                  image={datum.image}
                  label={datum.title}
                  price={
                    datum.discountedPrice
                      ? datum.discountedPrice
                      : datum.mrpPrice
                  }
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
