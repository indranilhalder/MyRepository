import React from "react";
import styles from "./ShopCollection.css";
import Image from "../../xelpmoc-core/Image";
import Icon from "../../xelpmoc-core/Icon";
import Button from "../../general/components/Button";
import PropTypes from "prop-types";
export default class ShopCollection extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <Image image={this.props.image} lazyLoad={true} />
        </div>
        <div className={styles.textLine}>
          <div className={styles.headingHolder}>
            {this.props.logo && (
              <div className={styles.logoHolder}>
                <Icon image={this.props.logo} size={40} />
              </div>
            )}
            <div className={styles.heading}>{this.props.header}</div>
          </div>
          <div className={styles.title}>{this.props.title}</div>
          <div className={styles.buttonHolder}>
            <Button
              borderRadius={22.5}
              label={this.props.btnText}
              backgroundColor={this.props.backgroundColor}
              onClick={() => this.handleClick()}
              width={180}
              textStyle={{ color: this.props.color, fontSize: 14 }}
            />
          </div>
        </div>
      </div>
    );
  }
}
ShopCollection.propTypes = {
  onClick: PropTypes.func,
  title: PropTypes.string,
  btnText: PropTypes.string,
  backgroundColor: PropTypes.string,
  logo: PropTypes.string,
  image: PropTypes.string,
  header: PropTypes.string,
  color: PropTypes.string
};
ShopCollection.defaultProps = {
  color: "#fff",
  backgroundColor: "#ff1744"
};
