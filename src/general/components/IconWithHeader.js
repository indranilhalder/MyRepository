import React from "react";
import styles from "./IconWithHeader.css";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import VisibilityChild from "../../home/components/VisibilityChild";
export default class IconWithHeader extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.iconHolder}>
          <VisibilityChild>
            <Icon image={this.props.image} size={30} />
          </VisibilityChild>
        </div>
        <div className={styles.textHolder}>{this.props.header}</div>
        {this.props.children}
      </div>
    );
  }
}
IconWithHeader.propTypes = {
  image: PropTypes.string,
  text: PropTypes.string
};
