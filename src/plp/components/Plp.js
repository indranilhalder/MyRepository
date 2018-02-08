import React from "react";

import ProductGrid from "./ProductGrid";
import PlpMobileFooter from "./PlpMobileFooter";
import InformationHeader from "../../general/components/InformationHeader";
import styles from "./Plp.css";
import { PRODUCT_FILTER_ROUTER } from "../../lib/constants";
export default class Plp extends React.Component {
  toggleFilter = () => {
    this.props.history.push(PRODUCT_FILTER_ROUTER);
  };
  onApply = val => {
    this.toggleFilter();
    if (this.props.onApply) {
      this.props.onApply(val);
    }
  };
  handleBackClick = () => {
    if (this.props.onBack) {
      this.props.onBack();
    }
  };
  onSortClick = () => {
    if (this.props.showSort) {
      this.props.showSort();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.pageHeader}>
          <InformationHeader
            onClick={this.handleBackClick}
            text="Product listing"
          />
        </div>
        <div className={styles.main}>
          <ProductGrid
            history={this.props.history}
            data={this.props.searchresult}
          />
        </div>
        <div className={styles.filter}>
          <InformationHeader onClick={this.toggleFilter} text="Refine by" />
        </div>
        <div className={styles.footer}>
          <PlpMobileFooter
            onFilter={this.toggleFilter}
            onSort={this.onSortClick}
          />
        </div>
      </div>
    );
  }
}
