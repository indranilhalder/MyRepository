import React from "react";
import styles from "./ConnectBaseWidget.css";
import ConnectBothWidget from "./ConnectBothWidget";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class ConnectBaseWidget extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    let data = this.props.data;

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
  data: PropTypes.shape({
    backgroundImageURL: PropTypes.string,
    description: PropTypes.string.apply,
    imageURL: PropTypes.string,
    items: PropTypes.arrayOf(
      PropTypes.shape({
        imageURL: PropTypes.string,
        title: PropTypes.string,
        description: PropTypes.string
      })
    )
  })
};
