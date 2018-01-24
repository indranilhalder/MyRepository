import React from "react";
import Grid from "../../general/components/Grid";
import ProductModule from "../../general/components/ProductModule";
import PropTypes from "prop-types";
import { Icon, CircleButton } from "xelpmoc-core";
import styles from "./FlashSale.css";
export default class FlashSale extends React.Component {
  render() {
    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `url(${this.props.backgroundImage})`
        }}
      >
        <div className={styles.header}>
          <div className={styles.headingText}>{this.props.headingText}</div>
          <div className={styles.offerTime}>
            {this.props.offerTime}
            <div className={styles.clock}>
              <CircleButton
                size={20}
                color={"transparent"}
                // timer icon
                icon={<Icon image={this.props.icon} />}
              />
            </div>
          </div>
        </div>
        <div className={styles.subheader}>{this.props.subHeader}</div>
        <Grid offset={25}>
          {this.props.data &&
            this.props.data.map((datum, i) => {
              return (
                <ProductModule
                  key={i}
                  isWhite={true}
                  productImage={datum.image}
                  title={datum.title}
                  price={datum.price}
                  description={datum.description}
                />
              );
            })}
        </Grid>
      </div>
    );
  }
}
FlashSale.propTypes = {
  offerTime: PropTypes.string,
  headingText: PropTypes.string,
  subHeader: PropTypes.string,
  icon: PropTypes.string,
  backgroundImage: PropTypes.string,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      title: PropTypes.string,
      description: PropTypes.string,
      price: PropTypes.string
    })
  )
};
FlashSale.defaultProps = {
  headingText: "Exclusive offers",
  subHeader: "Grab these offers for a limited time only!"
};
