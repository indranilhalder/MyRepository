import React from "react";
import PropTypes from "prop-types";
import styles from "./Grid.css";
import MediaQuery from "react-responsive";

export default class Grid extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div
          className={styles.gridHolder}
          style={{
            padding: `${this.props.offset / 2}px`
          }}
        >
          {this.props.children &&
            this.props.children.map((child, i) => {
              console.log(child.props);
              return (
                <React.Fragment key={i}>
                  <MediaQuery query="(min-device-width: 1025px)">
                    <div
                      className={styles.element}
                      style={{
                        width: child.props.gridWidthDesktop
                          ? `${child.props.gridWidthDesktop}%`
                          : `${this.props.elementWidthDesktop}%`,
                        padding: `${this.props.offset / 2}px`
                      }}
                    >
                      {child}
                    </div>
                  </MediaQuery>
                  <MediaQuery query="(max-device-width: 1024px)">
                    <div
                      className={styles.element}
                      style={{
                        width: child.props.gridWidthMobile
                          ? `${child.props.gridWidthMobile}%`
                          : `${this.props.elementWidthMobile}%`,
                        padding: `${this.props.offset / 2}px`
                      }}
                    >
                      {child}
                    </div>
                  </MediaQuery>
                </React.Fragment>
              );
            })}
        </div>
      </div>
    );
  }
}
Grid.propTypes = {
  elementWidthDesktop: PropTypes.number,
  elementWidthMobile: PropTypes.number,
  offset: PropTypes.number
};

Grid.defaultProps = {
  elementWidthDesktop: 25,
  elementWidthMobile: 50,
  offset: 10
};
