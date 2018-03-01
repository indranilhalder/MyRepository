import React from "react";
import ProductListingsContainer from "../containers/ProductListingsContainer.js";

const CATEGORY_REGEX = /c-msh*/;
const BRAND_REGEX = /c-mbh*/;
const CAPTURE_REGEX = /c-(.*)/;

// ?searchText=:relevance:category:MSH1012100&isFilter=false&isTextSearch=false&isPwa=false&page=0&pageSize=20&typeID=all --> is an url

export default class PlpBrandCategoryWrapper extends React.Component {
  componentDidMount() {
    // this will do the check for category or brand
    // which does not happen now
    console.log(this.props);
    const brandOrCategoryId = this.props.match.params.brandOrCategoryId;
    let match;
    if (CATEGORY_REGEX.test(brandOrCategoryId)) {
      match = CAPTURE_REGEX.exec(brandOrCategoryId)[1];
    }

    if (BRAND_REGEX.test(brandOrCategoryId)) {
      match = CAPTURE_REGEX.exec(brandOrCategoryId)[1];
    }

    match = match.toUpperCase();
    console.log("match");
    console.log(match);
    // I can just assume that we need to set filters here.
    const suffix = `&isTextSearch=false&isFilter=false`;
    const filters = [{ key: "category", filters: [`${match}`] }];
    this.props.getProductListings(filters, suffix);
  }

  // from the url I construct filters

  // and execute a search

  // so this page needs a container that will supply those actions
  // getProductListings works on the search state, so I will need to update that from the url
  // then call getProductListings

  // so this thing will need setFIlters, getProductListings

  render() {
    return <ProductListingsContainer />;
  }
}

// Brand Page
// https://uat2.tataunistore.com/marketplacewebservices/v2/mpl/products/serpsearch?type=category&channel=mobile&pageSize=20&typeID=all&page=0&searchText=:relevance:brand:MBH12E00001&isFilter=false&isTextSearch=false&isPwa=false

// Category Page
// https://uat2.tataunistore.com/marketplacewebservices/v2/mpl/products/serpsearch?type=category&channel=mobile&pageSize=20&typeID=all&page=0&searchText=:relevance:category:MSH1012100&isFilter=false&isTextSearch=false&isPwa=false
