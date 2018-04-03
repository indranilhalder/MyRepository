import React from "react";
import FilterContainer from "../containers/FilterContainer";
import ProductGrid from "./ProductGrid";
import PlpMobileFooter from "./PlpMobileFooter";
import styles from "./Plp.css";
import throttle from "lodash/throttle";
import MDSpinner from "react-md-spinner";
const SUFFIX = `&isTextSearch=false&isFilter=false`;

export default class Plp extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showFilter: props.showFilter
    };
  }

  toggleFilter = () => {
    this.setState({ showFilter: !this.state.showFilter });
    if (this.props.onFilter) {
      this.props.onFilter(!this.state.showFilter);
    }
  };

  componentWillReceiveProps(nextProps) {
    this.setState({ showFilter: nextProps.showFilter });
  }
  onApply = val => {
    this.toggleFilter();
    if (this.props.onApply) {
      this.props.onApply(val);
    }
  };

  handleScroll = () => {
    return throttle(() => {
      if (!this.state.showFilter) {
        const windowHeight =
          "innerHeight" in window
            ? window.innerHeight
            : document.documentElement.offsetHeight;
        const body = document.body;
        const html = document.documentElement;
        const docHeight = Math.max(
          body.scrollHeight,
          body.offsetHeight,
          html.clientHeight,
          html.scrollHeight,
          html.offsetHeight
        );
        const windowBottom = windowHeight + window.pageYOffset;
        if (windowBottom >= docHeight) {
          window.scrollBy(0, -200);
          this.props.paginate(this.props.pageNumber + 1, SUFFIX);
        }
      }
    }, 2000);
  };

  componentWillUnmount() {
    window.removeEventListener("scroll", this.throttledScroll);
  }

  componentDidMount() {
    this.throttledScroll = this.handleScroll();
    window.addEventListener("scroll", this.throttledScroll);
  }
  componentDidUpdate(prevProps) {
    if (this.props.productListings !== null) {
      this.props.setHeaderText(
        `${this.props.match.params.slug}(${
          this.props.productListings.pagination.totalResults
        })`
      );
    }
  }
  backPage = () => {
    if (this.state.showFilter) {
      this.setState({ showFilter: !this.state.showFilter });
    } else {
      this.props.history.goBack();
    }
  };
  onSortClick = () => {
    if (this.props.showSort) {
      this.props.showSort();
    }
  };

  renderLoader() {
    return (
      <div>
        <MDSpinner />
      </div>
    );
  }

  render() {
    console.log(this.props);
    let filterClass = styles.filter;

    if (this.props.loading && !this.props.isFilter) {
      return this.renderLoader();
    }

    return (
      this.props.productListings && (
        <div className={styles.base}>
          <div className={styles.main}>
            <ProductGrid
              history={this.props.history}
              data={this.props.productListings.searchresult}
              totalResults={this.props.productListings.pagination.totalResults}
            />
          </div>
          <FilterContainer
            backPage={this.backPage}
            showFilter={this.state.showFilter}
          />
          <div className={styles.footer}>
            <PlpMobileFooter
              onFilter={this.toggleFilter}
              onSort={this.onSortClick}
            />
          </div>
        </div>
      )
    );
  }
}
