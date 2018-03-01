import React from "react";
import PdpFrame from "./PdpFrame";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import { Image } from "xelpmoc-core";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ColourSelector from "./ColourSelector";
import SizeSelector from "./SizeSelector";
import OfferCard from "./OfferCard";
import PdpLink from "./PdpLink";
import ProductDetails from "./ProductDetails";
import RatingAndTextLink from "./RatingAndTextLink";
import DeliveryInformation from "../../general/components/DeliveryInformations.js";
import styles from "./ProductDescriptionPage.css";

const DELIVERY_TEXT = "Delivery Options For";
const PIN_CODE = "110011";
export default class PdpElectronics extends React.Component {
  render() {
    const mobileGalleryImages = [
      {
        value:
          "https://i.pinimg.com/564x/5f/f6/42/5ff642df7ed892a1614fe179e6a1a255.jpg"
      },
      {
        value:
          "https://i.pinimg.com/564x/5f/f6/42/5ff642df7ed892a1614fe179e6a1a255.jpg"
      },
      {
        value:
          "https://i.pinimg.com/564x/5f/f6/42/5ff642df7ed892a1614fe179e6a1a255.jpg"
      }
    ];
    const productData = {
      otherSellersText:
        "<p>Sold directly by <b>Westside</b> and 9 other sellers</p>",
      averageRating: 4.2,
      productReviewsCount: 50,
      emiInfo: { emiText: "Emi available on this product" },
      productOfferPromotion: [
        {
          promoID: "1234",
          promotionTitle: "Limited time offer",
          promotionDetail:
            "<p>Enter coupon code <b>EXTRA 20<b> at checkout for an extra 20% off on sales price</p>",
          validTill: {
            formattedDate: "25th Nov 2018",
            date: "2018-11-25 01:01:17"
          },
          showTimer: true
        },
        {
          promoID: "1235",
          promotionTitle: "HDFC",
          promotionDetail:
            "<p>Get 20% off (upto Rs. 1000)on payment via HDFC Bank Credit/Debit card</p>",
          validTill: {
            formattedDate: "25th Nov 2018",
            date: "2018-11-25 01:01:17"
          },
          showTimer: false
        },
        {
          promoID: "123443",
          promotionTitle: "Limited time offer",
          promotionDetail:
            "<p>Enter coupon code <b>Festive 20<b> at checkout for an extra 20% off on sales price</p>",
          validTill: {
            formattedDate: "25th Nov 2019",
            date: "2018-11-25 01:01:17"
          },
          showTimer: false
        },
        {
          promoID: "123423",
          promotionTitle: "Limited time offer",
          promotionDetail:
            "<p>Enter coupon code <b>EXTRA 20<b> at checkout for an extra 20% off on sales price</p>",
          validTill: {
            formattedDate: "25th Nov 2018",
            date: "2018-11-25 01:01:17"
          },
          showTimer: false
        }
      ],
      variantOptions: {
        showColor: true,
        colorlink: [
          {
            color: "AQUA",
            hexCode: "#123456",
            showSize: true,
            sizelink: [
              {
                size: "8GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "16GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "32GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "64GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "128GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "168GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              }
            ]
          },
          {
            color: "red",
            hexCode: "green",
            selected: true,
            showSize: true,
            sizelink: [
              {
                size: "8GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "16GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "32GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "64GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "128GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "168GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              }
            ]
          },
          {
            color: "green",
            hexCode: "blue",
            showSize: false,
            sizelink: [
              {
                size: "8GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "16GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "32GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "64GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "128GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "168GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              }
            ]
          },
          {
            color: "blue",
            hexCode: "red",
            showSize: true,
            sizelink: [
              {
                size: "8GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "16GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "32GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "64GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "128GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              },
              {
                size: "168GB",
                productCode: "MP000000000000123",
                producturl:
                  "/home-decor-home-fragrances-diffusers/p-mp000000000113716",
                isAvailable: true
              }
            ]
          }
        ]
      },
      eligibleDeliveryModes: [
        {
          code: "express-delivery",
          displayCost: "₹0.00",
          name: "Express Delivery",
          timeline: "Delivered in 3-6 business days."
        },
        {
          code: "home-delivery",
          displayCost: "₹0.00",
          name: "Home Delivery",
          timeline: "Delivered in 3-6 business days."
        },
        {
          code: "click-and-collect",
          displayCost: "₹0.00",
          name: "Click and Collect",
          timeline: "Delivered in 3-6 business days."
        }
      ],
      productDetails: [
        {
          title: "Product Description",
          details:
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ultricies laoreet sapien, a malesuada lorem porttitor ac. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas"
        },
        {
          title: "Size and Fit",
          details:
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ultricies laoreet sapien, a malesuada lorem porttitor ac. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas"
        },
        {
          title: "Composition and Care",
          details:
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ultricies laoreet sapien, a malesuada lorem porttitor ac. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas"
        },
        {
          title: "Shipping and Free Returns",
          details:
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ultricies laoreet sapien, a malesuada lorem porttitor ac. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas"
        }
      ]
    };
    return (
      <PdpFrame>
        <ProductGalleryMobile>
          {mobileGalleryImages.map((val, idx) => {
            return <Image image={val.value} key={idx} />;
          })}
        </ProductGalleryMobile>
        <ProductDetailsMainCard
          productName={"Beats by Dre"}
          productDescription={"Studio3 Wireless Headphones"}
          price={"32,800"}
          discountPrice={"29,620"}
          averageRating={4.2}
        />
        {productData.emiInfo && (
          <div className={styles.separator}>
            <div className={styles.info}>
              {productData.emiInfo.emiText}
              <span className={styles.link} onClick={this.showEmiModal}>
                View Plans
              </span>
            </div>
          </div>
        )}
        <OfferCard
          endTime={productData.productOfferPromotion[0].validTill.date}
          heading={productData.productOfferPromotion[0].promotionTitle}
          description={productData.productOfferPromotion[0].promotionDetail}
          onClick={this.goToCouponPage}
        />
        {productData.variantOptions &&
          productData.variantOptions.showColor && (
            <React.Fragment>
              <ColourSelector
                data={productData.variantOptions.colorlink}
                selected={productData.variantOptions.colorlink
                  .filter(option => {
                    return option.selected;
                  })
                  .map(value => {
                    return value.color;
                  })}
                updateColour={val => {}}
                getProductSpecification={this.props.getProductSpecification}
              />
              <SizeSelector
                showSizeGuide={this.props.showSizeGuide}
                data={productData.variantOptions.colorlink
                  .filter(option => {
                    return option.selected;
                  })
                  .map(value => {
                    return value.sizelink;
                  })}
              />
            </React.Fragment>
          )}
        {productData.eligibleDeliveryModes &&
          productData.eligibleDeliveryModes.map((val, idx) => {
            return (
              <DeliveryInformation
                key={idx}
                header={val.name}
                placedTime={val.timeline}
                type={val.code}
                onClick={() => this.renderAddressModal()}
                deliveryOptions={DELIVERY_TEXT}
                label={PIN_CODE}
              />
            );
          })}
        <div className={styles.separator}>
          <PdpLink onClick={this.goToSellerPage}>
            <div
              className={styles.sellers}
              dangerouslySetInnerHTML={{
                __html: productData.otherSellersText
              }}
            />
          </PdpLink>
        </div>
        <div className={styles.details}>
          <ProductDetails data={productData.productDetails} />
        </div>
        <div className={styles.separator}>
          <RatingAndTextLink
            onClick={this.goToReviewPage}
            averageRating={productData.averageRating}
            numberOfReview={productData.productReviewsCount}
          />
        </div>
      </PdpFrame>
    );
  }
}
