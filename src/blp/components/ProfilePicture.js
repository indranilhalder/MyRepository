import React from "react";
import styles from "./ProfilePicture.css";
import ProfileImage from "../../xelpmoc-core/ProfileImage";
import editIcon from "../../general/components/img/tick.svg";
import PropTypes from "prop-types";
import Icon from "../../xelpmoc-core/Icon";
export default class ProfilePicture extends React.Component {
  onEdit() {
    if (this.props.onEdit) {
      this.props.onEdit();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.nameAndLogoHolder}>
          <div className={styles.profileLogoHolder}>
            <div
              className={this.props.edit ? styles.onEdit : styles.edit}
              onClick={() => this.onEdit()}
            >
              <Icon image={editIcon} size={18} />
            </div>
            {(this.props.firstName || this.props.lastName) && (
              <ProfileImage
                image={this.props.profileImageLink}
                size={3}
                initials={
                  this.props.firstName && !this.props.lastName
                    ? this.props.firstName.charAt(0)
                    : this.props.lastName && !this.props.firstName
                      ? this.props.lastName.charAt(0)
                      : this.props.firstName.charAt(0) +
                        this.props.lastName.charAt(0)
                }
              />
            )}
          </div>
          {!this.props.edit && (
            <div className={styles.nameOfProfileHolder}>
              {this.props.firstName} {this.props.lastName}
            </div>
          )}
        </div>
      </div>
    );
  }
}
ProfilePicture.PropTypes = {
  edit: PropTypes.bool,
  onEdit: PropTypes.func,
  profileImageLink: PropTypes.string,
  name: PropTypes.string
};
