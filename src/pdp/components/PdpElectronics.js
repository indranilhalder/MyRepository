import React from "react";
import PdpFrame from "./PdpFrame";
import ProductDetailsMainCard from "./ProductDetailsMainCard";
import { Image } from "xelpmoc-core";
import ProductGalleryMobile from "./ProductGalleryMobile";
import ColourSelector from "./ColourSelector";
import SizeSelector from "./SizeSelector";
import OfferCard from "./OfferCard";
import styles from "./ProductDescriptionPage.css";
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
      }
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
          <div className={styles.info}>
            {productData.emiInfo.emiText}
            <span className={styles.link} onClick={this.showEmiModal}>
              View Plans
            </span>
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
      </PdpFrame>
    );
  }
}
