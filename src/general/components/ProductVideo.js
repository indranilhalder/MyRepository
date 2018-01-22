import React from "react";
import Video from "./Video";
import Logo from "./Logo";
import styles from "./ProductVideo.css";
export default class ProductVideo extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      playing: false
    };
  }
  render() {
    let overlayClass = styles.overlay;
    if (this.state.playing) {
      overlayClass = styles.overlayHidden;
    }
    return (
      <div className={styles.base}>
        <div className={styles.video}>
          <Video url={this.props.url} playing={this.state.playing} />
        </div>
        <div
          className={overlayClass}
          onClick={() => {
            this.setState({ playing: !this.state.playing });
          }}
        >
          <div className={styles.logoHolder}>
            <div className={styles.logo}>
              <Logo image={this.props.logo} />
            </div>
          </div>
          <div className={styles.play} />
          <div className={styles.description}>{this.props.description}</div>
        </div>
      </div>
    );
  }
}
