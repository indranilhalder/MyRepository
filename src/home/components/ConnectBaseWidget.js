import React from "react";
import styles from "./ConnectBaseWidget.css";
import ConnectBothWidget from "./ConnectBothWidget";
import Connect from "./img/Connect_Small.svg";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class ConnectBaseWidget extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    console.log(this.props);
    let data = this.props.data.data;

    return (
      <div
        className={styles.base}
        style={{ backgroundImage: `url(${data.backgroundImageURL})` }}
      >
        <div className={styles.header}>
          <div className={styles.iconBase}>
            <div className={styles.iconHolder}>
              <Icon image={data.imageURL} size={50} />
            </div>
            <div className={styles.text}>Connect</div>
          </div>
          <div className={styles.heading}>{data.description}</div>
        </div>
        {data.items &&
          data.items.map((datum, i) => {
            return (
              <ConnectBothWidget
                key={i}
                title={datum.title}
                description={datum.description}
                imageURL={datum.imageURL}
              />
            );
          })}
        <div className={styles.buttonBox}>
          <div
            className={styles.button}
            onClick={() => {
              this.handleClick();
            }}
          >
            know more
          </div>
        </div>
      </div>
    );
  }
}
ConnectBaseWidget.propTypes = {
  text: PropTypes.string,
  image: PropTypes.string,
  heading: PropTypes.string,
  onClick: PropTypes.func,
  data: PropTypes.arrayOf(
    PropTypes.shape({
      title: PropTypes.string,
      description: PropTypes.string
    })
  )
};
