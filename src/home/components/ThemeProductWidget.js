import React from "react";
import Carousel from "../../general/components/Carousel";
import ThemeProduct from "../../general/components/ThemeProduct";
import Logo from "../../general/components/Logo";
import PropTypes from "prop-types";
import styles from "./ThemeProductWidget.css";
import { PRODUCT_LISTINGS } from "../../lib/constants";
export default class ThemeProductWidget extends React.Component {
  handleClick() {
    this.props.history.push(PRODUCT_LISTINGS);
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
          {...this.props}
          header={data.title}
          buttonText={"See All"}
          seeAll={() => this.handleClick()}
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
  feedComponentData: PropTypes.shape({
    data: PropTypes.shape({
      backgroundImageURL: PropTypes.string,
      brandLogo: PropTypes.string,
      btnText: PropTypes.string,
      items: PropTypes.arrayOf(
        PropTypes.shape({
          title: PropTypes.string,
          mrpPrice: PropTypes.string,
          discountedPrice: PropTypes.string,
          imageURL: PropTypes.string
        })
      )
    })
  })
};
