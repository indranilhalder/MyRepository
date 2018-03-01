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
import ProductFeatures from "./ProductFeatures";
import RatingAndTextLink from "./RatingAndTextLink";
import AllDescription from "./AllDescription";
import DeliveryInformation from "../../general/components/DeliveryInformations.js";
import styles from "./ProductDescriptionPage.css";

const DELIVERY_TEXT = "Delivery Options For";
const PIN_CODE = "110011";
export default class PdpElectronics extends React.Component {
  render() {
    const productData = {
      type: "productDetailMobileWsData",
      APlusContent: {
        productContent: [
          {
            key: "Section2B",
            value: {
              imageList: [
                "https://assets.tatacliq.com/medias/sys_master/images/10130933284894.jpg"
              ]
            }
          },
          {
            key: "Section1A",
            value: {
              imageList: [
                "https://assets.tatacliq.com/medias/sys_master/images/MP000000000572025_1.jpeg"
              ]
            }
          },
          {
            key: "Section3B",
            value: {
              textList: [
                "<h3>Capture Life Like Images</h3><p>The iPhone is the most popular camera in the world. Now Apple has re-engineered the iPhone 7 camera by adding optical image stabilisation, an f/1.8 aperture and a six-element lens to make it even better for shooting photos and videos in low light. With advanced new features like wide colour capture, your photos and Live Photos will look even more vibrant.</p>"
              ]
            }
          },
          {
            key: "Section2A",
            value: {
              textList: [
                "<h3>An All-New Home Button</h3><p>The Home button on iPhone 7 is an advanced solid-state button designed to be durable, responsive and pressure sensitive. Working in tandem with the new Taptic Engine, it gives you precise tactile feedback as you press. And it's even customisable. Welcome home.</p>"
              ]
            }
          },
          {
            key: "Section1B",
            value: {
              textList: [
                "<h3>Innovative Design</h3><p>iPhone 7 reaches a new level of innovation and precision. With a new unibody design that's seamless to touch, iPhone 7 feels as amazing as it looks. With iPhone 7, Apple introduces a beautiful black with a matt finish and a deep, high-gloss jet black shade. The 4.7-inch model is constructed with incredibly strong 7000 Series aluminium - also available in signature silver, gold and rose gold finishes.</p>"
              ]
            }
          },
          {
            key: "Section5A",
            value: {
              imageList: [
                "https://assets.tatacliq.com/medias/sys_master/images/10130933710878.jpg"
              ]
            }
          },
          {
            key: "Section6B",
            value: {
              textList: [
                "<h4>Longest Battery Life</h4><p>With the A10 Fusion chip, this year you'll get more time between charges than ever before. Take advantage of up to two more hours on iPhone 7 and up to one more hour on iPhone 7 Plus than the previous generation. Do more with the extended battery life and never run out of juice when you really need to get going.</p>"
              ]
            }
          },
          {
            key: "Section8C",
            value: {
              videoList: ["https://www.youtube.com/embed/La4HRfL5tV4"]
            }
          },
          {
            key: "Section6A",
            value: {
              textList: [
                "<h4>iPhone - Now in Stereo</h4><p>For the first time, iPhone comes with stereo speakers, delivering twice the audio output of iPhone 6s and increased dynamic range. So whether you're listening to music, watching videos or making speakerphone calls, iPhone 7 lets you crank it up. Enjoy crystal clear playback of your music and calls.</p>"
              ]
            }
          },
          {
            key: "Section3A",
            value: {
              imageList: [
                "https://assets.tatacliq.com/medias/sys_master/images/MP000000000572029_3.jpeg"
              ]
            }
          },
          {
            key: "Section5C",
            value: {
              imageList: [
                "https://assets.tatacliq.com/medias/sys_master/images/MP000000000572029_7.jpeg"
              ]
            }
          },
          {
            key: "Section4A",
            value: {
              textList: ["<h2>FEATURES</h2>"]
            }
          },
          {
            key: "Section6C",
            value: {
              textList: [
                "<h4>Retina HD Display</h4><p>Almost everything you experience with your iPhone 7 Plus comes to life on its display. It's where you look at the photos, messages, news, and countless other things that make up your day. The iPhone 7 Plus display uses the same colour space as the digital cinema industry, so what you see will be noticeably more brilliant and vibrant.</p>"
              ]
            }
          },
          {
            key: "Section5B",
            value: {
              imageList: [
                "https://assets.tatacliq.com/medias/sys_master/images/MP000000000572029_6.jpeg"
              ]
            }
          },
          {
            key: "Section9A",
            value: {
              textList: ["<h4>Apple - Introducing iPhone 7</h4>"]
            }
          },
          {
            key: "Section8B",
            value: {
              videoList: ["https://www.youtube.com/embed/sbios0u2Px8"]
            }
          },
          {
            key: "Section9C",
            value: {
              textList: ["<h4>iPhone 7 - Dive - Apple</h4>"]
            }
          },
          {
            key: "Section7A",
            value: {
              textList: ["<h2>Related Videos</h2>"]
            }
          },
          {
            key: "Section9B",
            value: {
              textList: ["<h4>iPhone 7 - Design</h4>"]
            }
          },
          {
            key: "Section8A",
            value: {
              videoList: ["https://www.youtube.com/embed/Q6dsRpVyyWs"]
            }
          }
        ],
        temlateName: "MP000000000572025_Apple iPhone 7 256 GB (Gold)_1"
      },
      allOOStock: false,
      brandInfo:
        "A status symbol and a pioneer in the technology industry, Apple represents a unique combination of performance and style. On Tata CliQ, you will find a wide range of Apple signature products like iPhones, iPads and Macbooks at your disposal.",
      brandName: "Apple",
      classifications: [
        {
          groupName: "Technical Features",
          specifications: [
            {
              key: "Performance Features",
              value:
                "A10 Fusion chip with 64-bit Architecture and Embedded M10 Motion Coprocessor"
            },
            {
              key: "Sensors",
              value:
                "Accelerometer ambient light sensor three-axis gyro sensor digital compass proximity sensor"
            }
          ]
        },
        {
          groupName: "General Features",
          specifications: [
            {
              key: "Sim Size",
              value: "Nano Sim"
            },
            {
              key: "In the Box",
              value:
                "iPhone with iOS 10, EarPods with Lightning Connector, Lightning to 3.5mm Headphone Jack Adapter, Lightning to USB Cable, USB Power Adapter, Manual"
            },
            {
              key: "Operating System",
              value: "iOS 10"
            },
            {
              key: "Color Family",
              value: "Gold"
            }
          ]
        },
        {
          groupName: "Camera",
          specifications: [
            {
              key: "Front Camera",
              value: "7 MP FaceTime HD Camera"
            },
            {
              key: "Primary Camera",
              value: "12 MP"
            },
            {
              key: "Video Recording",
              value: "1080p@60FPS"
            },
            {
              key: "Connectivity",
              value: "4G"
            },
            {
              key: "Flash",
              value: "Quad-LED True Tone flash"
            }
          ]
        },
        {
          groupName: "Display Features",
          specifications: [
            {
              key: "Screen Size (cm)",
              value: "11.93 cm"
            },
            {
              key: "Display Features",
              value:
                "1334x750 pixel Resolution, Multi Touch Display with IPS Technology"
            },
            {
              key: "Resolution",
              value: "1334 x 750 pixels"
            }
          ]
        },
        {
          groupName: "Memory and storage",
          specifications: [
            {
              key: "Memory",
              value: "256 GB"
            }
          ]
        },
        {
          groupName: "Dimensions",
          specifications: [
            {
              key: "Product Depth (cm)",
              value: "0.71"
            },
            {
              key: "Product Height (cm)",
              value: "13.83"
            },
            {
              key: "Weight",
              value: "138"
            },
            {
              key: "Product Width (cm)",
              value: "6.71"
            }
          ]
        },
        {
          groupName: "Warranty",
          specifications: [
            {
              key: "Warranty Description",
              value: "1 Year"
            },
            {
              key: "Warranty Summary",
              value: "Manufacturer Warranty"
            }
          ]
        }
      ],
      deliveryModesATP: [
        {
          key: "home-delivery",
          value: "Delivered in 3-6 business days."
        },
        {
          key: "express-delivery",
          value: "Delivered in 1-2 business days."
        }
      ],
      details: [
        {
          key: "Feature 1",
          value: "2 GB RAM and 256 GB Internal Memory"
        },
        {
          key: "Feature 2",
          value: "4.7-inch Retina HD Display"
        },
        {
          key: "Feature 3",
          value: "12 Megapixel Camera with 5x Digital Zoom"
        },
        {
          key: "Feature 4",
          value: "Splash, Water and Dust Resistant"
        }
      ],
      discount: "20",
      eligibleDeliveryModes: [
        {
          code: "home-delivery",
          displayCost: "₹0.00",
          name: "Home Delivery"
        },
        {
          code: "home-delivery",
          displayCost: "₹0.00",
          name: "Home Delivery"
        }
      ],
      fulfillmentType: "tship",
      galleryImagesList: [
        {
          galleryImages: [
            {
              key: "product",
              value:
                "//img.tatacliq.com/images/437Wx649H/MP000000000572025_437Wx649H_20161115201717.jpeg"
            },
            {
              key: "thumbnail",
              value:
                "//img.tatacliq.com/images/97Wx144H/MP000000000572025_97Wx144H_20161115201717.jpeg"
            },
            {
              key: "searchPage",
              value:
                "//img.tatacliq.com/images/252Wx374H/MP000000000572025_252Wx374H_20161115201717.jpeg"
            },
            {
              key: "superZoom",
              value:
                "//img.tatacliq.com/images/1348Wx2000H/MP000000000572025_1348Wx2000H_20161115201717.jpeg"
            },
            {
              key: "cartIcon",
              value:
                "//img.tatacliq.com/images/97Wx144H/MP000000000572025_97Wx144H_20161115201717.jpeg"
            },
            {
              key: "zoom",
              value:
                "//img.tatacliq.com/images/437Wx649H/MP000000000572025_437Wx649H_20161115201717.jpeg"
            },
            {
              key: "cartPage",
              value:
                "//img.tatacliq.com/images/113Wx168H/MP000000000572025_113Wx168H_20161115201717.jpeg"
            }
          ],
          mediaType: "Image"
        },
        {
          galleryImages: [
            {
              key: "product",
              value:
                "//img.tatacliq.com/images/437Wx649H/MP000000000572025_437Wx649H_20161115201712.jpeg"
            },
            {
              key: "thumbnail",
              value:
                "//img.tatacliq.com/images/97Wx144H/MP000000000572025_97Wx144H_20161115201712.jpeg"
            },
            {
              key: "searchPage",
              value:
                "//img.tatacliq.com/images/252Wx374H/MP000000000572025_252Wx374H_20161115201712.jpeg"
            },
            {
              key: "superZoom",
              value:
                "//img.tatacliq.com/images/1348Wx2000H/MP000000000572025_1348Wx2000H_20161115201712.jpeg"
            },
            {
              key: "cartIcon",
              value:
                "//img.tatacliq.com/images/97Wx144H/MP000000000572025_97Wx144H_20161115201712.jpeg"
            },
            {
              key: "zoom",
              value:
                "//img.tatacliq.com/images/437Wx649H/MP000000000572025_437Wx649H_20161115201712.jpeg"
            },
            {
              key: "cartPage",
              value:
                "//img.tatacliq.com/images/113Wx168H/MP000000000572025_113Wx168H_20161115201712.jpeg"
            }
          ],
          mediaType: "Image"
        },
        {
          galleryImages: [
            {
              key: "product",
              value:
                "//img.tatacliq.com/images/437Wx649H/MP000000000572025_437Wx649H_20161115201710.jpeg"
            },
            {
              key: "thumbnail",
              value:
                "//img.tatacliq.com/images/97Wx144H/MP000000000572025_97Wx144H_20161115201710.jpeg"
            },
            {
              key: "searchPage",
              value:
                "//img.tatacliq.com/images/252Wx374H/MP000000000572025_252Wx374H_20161115201710.jpeg"
            },
            {
              key: "superZoom",
              value:
                "//img.tatacliq.com/images/1348Wx2000H/MP000000000572025_1348Wx2000H_20161115201710.jpeg"
            },
            {
              key: "cartIcon",
              value:
                "//img.tatacliq.com/images/97Wx144H/MP000000000572025_97Wx144H_20161115201710.jpeg"
            },
            {
              key: "zoom",
              value:
                "//img.tatacliq.com/images/437Wx649H/MP000000000572025_437Wx649H_20161115201710.jpeg"
            },
            {
              key: "cartPage",
              value:
                "//img.tatacliq.com/images/113Wx168H/MP000000000572025_113Wx168H_20161115201710.jpeg"
            }
          ],
          mediaType: "Image"
        },
        {
          galleryImages: [
            {
              key: "product",
              value:
                "//img.tatacliq.com/images/437Wx649H/MP000000000572025_437Wx649H_20161115201714.jpeg"
            },
            {
              key: "thumbnail",
              value:
                "//img.tatacliq.com/images/97Wx144H/MP000000000572025_97Wx144H_20161115201714.jpeg"
            },
            {
              key: "searchPage",
              value:
                "//img.tatacliq.com/images/252Wx374H/MP000000000572025_252Wx374H_20161115201714.jpeg"
            },
            {
              key: "superZoom",
              value:
                "//img.tatacliq.com/images/1348Wx2000H/MP000000000572025_1348Wx2000H_20161115201714.jpeg"
            },
            {
              key: "cartIcon",
              value:
                "//img.tatacliq.com/images/97Wx144H/MP000000000572025_97Wx144H_20161115201714.jpeg"
            },
            {
              key: "zoom",
              value:
                "//img.tatacliq.com/images/437Wx649H/MP000000000572025_437Wx649H_20161115201714.jpeg"
            },
            {
              key: "cartPage",
              value:
                "//img.tatacliq.com/images/113Wx168H/MP000000000572025_113Wx168H_20161115201714.jpeg"
            }
          ],
          mediaType: "Image"
        }
      ],
      isCOD: "N",
      isEMIEligible: "Y",
      isOnlineExclusive: "N",
      isProductNew: "N",
      knowMore: [
        {
          knowMoreItem:
            "An order, once placed, can be cancelled until the seller processes it."
        },
        {
          knowMoreItem:
            "This product can be returned within 7 day(s) of delivery,subject to the Return Policy."
        },
        {
          knowMoreItem:
            "For any other queries, do reach out to CliQ Care at 90291 08282 or hello@tatacliq.com"
        }
      ],
      knowMoreEmail: "hello@tatacliq.com",
      knowMorePhoneNo: "90291 08282",
      maxQuantityAllowed: "5",
      mrp: "₹80000.00",
      otherSellers: [
        {
          USSID: "100073AppleiPhone7256GBGold",
          availableStock: "0",
          buyBoxWeightage: "-6.33E-4",
          deliveryModesATP: [
            {
              key: "home-delivery",
              value: "Delivered in 3-6 business days."
            },
            {
              key: "express-delivery",
              value: "Delivered in 1-2 business days."
            }
          ],
          eligibleDeliveryModes: [
            {
              code: "home-delivery",
              displayCost: "₹0.00",
              name: "Home Delivery"
            }
          ],
          fullfillmentType: "tship",
          isCOD: "N",
          isEMIEligible: "Y",
          replacement: "0",
          returnPolicy: "7",
          sellerAssociationstatus: "Y",
          sellerId: "100073",
          sellerMOP: "₹63300.00",
          sellerMRP: "₹80000.00",
          sellerName: "VTM SLP"
        },
        {
          USSID: "124240iPhone7256GBGold",
          availableStock: "25",
          buyBoxWeightage: "-6.3999E-4",
          deliveryModesATP: [
            {
              key: "home-delivery",
              value: "Delivered in 3-6 business days."
            },
            {
              key: "express-delivery",
              value: "Delivered in 1-2 business days."
            }
          ],
          eligibleDeliveryModes: [
            {
              code: "home-delivery",
              displayCost: "₹0.00",
              name: "Home Delivery"
            }
          ],
          fullfillmentType: "tship",
          isCOD: "N",
          isEMIEligible: "Y",
          replacement: "0",
          returnPolicy: "7",
          sellerAssociationstatus: "Y",
          sellerId: "124240",
          sellerMOP: "₹63999.00",
          sellerMRP: "₹80000.00",
          sellerName: "Mobilehub"
        },
        {
          USSID: "100058199228",
          availableStock: "1",
          buyBoxWeightage: "-6.5999E-4",
          deliveryModesATP: [
            {
              key: "home-delivery",
              value: "Delivered in 3-6 business days."
            },
            {
              key: "express-delivery",
              value: "Delivered in 1-2 business days."
            }
          ],
          eligibleDeliveryModes: [
            {
              code: "express-delivery",
              displayCost: "₹0.00",
              name: "Express Delivery"
            },
            {
              code: "click-and-collect",
              displayCost: "₹0.00",
              name: "Click and Collect"
            },
            {
              code: "home-delivery",
              displayCost: "₹0.00",
              name: "Home Delivery"
            }
          ],
          fullfillmentType: "sship",
          isCOD: "N",
          isEMIEligible: "Y",
          replacement: "0",
          returnPolicy: "7",
          sellerAssociationstatus: "Y",
          sellerId: "100058",
          sellerMOP: "₹65999.00",
          sellerMRP: "₹80000.00",
          sellerName: "Croma - A Tata Enterprise"
        },
        {
          USSID: "123924AppleiPhone7256GBGold",
          availableStock: "0",
          buyBoxWeightage: "-8.0E-4",
          deliveryModesATP: [
            {
              key: "home-delivery",
              value: "Delivered in 3-6 business days."
            },
            {
              key: "express-delivery",
              value: "Delivered in 1-2 business days."
            }
          ],
          eligibleDeliveryModes: [
            {
              code: "home-delivery",
              displayCost: "₹0.00",
              name: "Home Delivery"
            }
          ],
          fullfillmentType: "tship",
          isCOD: "N",
          isEMIEligible: "Y",
          replacement: "0",
          returnPolicy: "7",
          sellerAssociationstatus: "Y",
          sellerId: "123924",
          sellerMOP: "₹80000.00",
          sellerMRP: "₹80000.00",
          sellerName: "CAYMANSQUARE"
        }
      ],
      productCategory: "iPhone",
      productCategoryId: "MSH1210102",
      productDescription:
        "The Apple iPhone 7 features a 4.7-inch Retina HD display that delivers a crisp and clear visual output. It runs on iOS 10 that takes care of all its functions. With the 256GB built-in memory, this gold coloured phone lets you easily store all your multimedia files. Capture the world around you with the 12-megapixel camera with  /1.8 aperture and digital zoom of up to 5x",
      productListingId: "MP000000000572025",
      productName: "Apple iPhone 7 256 GB (Gold)",
      rootCategory: "Electronics",
      sellerAssociationstatus: "N",
      sharedText:
        "Wow!Check out this amazing find http://www.tatacliq.com/apple-iphone-7-256-gb-gold/p-mp000000000572025 . Like or  comment to tell me what you think, or share for warm fuzzies.",
      styleNote:
        "The Apple iPhone 7 features a 4.7-inch Retina HD display that delivers a crisp and clear visual output. It runs on iOS 10 that takes care of all its functions. With the 256GB built-in memory, this gold coloured phone lets you easily store all your multimedia files. Capture the world around you with the 12-megapixel camera with  /1.8 aperture and digital zoom of up to 5x",
      warranty: ["1 Year", "Manufacturer Warranty"],
      winningSellerAvailableStock: "25",
      winningSellerMOP: "₹63999.00",
      winningSellerName: "Sancell",
      winningUssID: "123869iPhone7256GBGold"
    };
    const mobileGalleryImages1 = [
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
    const mobileGalleryImages = productData.galleryImagesList
      .map(galleryImageList => {
        console.log(galleryImageList);
        return galleryImageList.galleryImages;
      })
      .map(galleryImages => {
        console.log(galleryImages);
        return galleryImages;
      })
      .map(galleryImage => {
        console.log(galleryImage);
        return galleryImage;
      })
      .filter(image => {
        console.log(image);
        return image.key === "product";
      });
    console.log(mobileGalleryImages);
    return (
      <PdpFrame>
        <ProductGalleryMobile>
          {mobileGalleryImages1.map((val, idx) => {
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
        {productData.productOfferPromotion && (
          <OfferCard
            endTime={productData.productOfferPromotion[0].validTill.date}
            heading={productData.productOfferPromotion[0].promotionTitle}
            description={productData.productOfferPromotion[0].promotionDetail}
            onClick={this.goToCouponPage}
          />
        )}
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
        {productData.details && (
          <div className={styles.details}>
            <ProductDetails data={productData.details} />
          </div>
        )}
        <div className={styles.separator}>
          <RatingAndTextLink
            onClick={this.goToReviewPage}
            averageRating={productData.averageRating}
            numberOfReview={productData.productReviewsCount}
          />
        </div>
        {productData.productFeatures && (
          <div className={styles.details}>
            <ProductFeatures features={productData.productFeatures} />
          </div>
        )}
        {productData.APlusContent && (
          <AllDescription
            productContent={productData.APlusContent.productContent}
          />
        )}
      </PdpFrame>
    );
  }
}
