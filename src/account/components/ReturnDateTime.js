import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import SelectReturnDate from "./SelectReturnDate";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import PropTypes from "prop-types";
import styles from "./ReturnDateTime.css";
const data = {
  deliveryAddress: {
    addressType: "Home",
    billingAddress: false,
    city: "Dwarka",
    country: {
      isocode: "IN",
      name: "India"
    },
    defaultAddress: false,
    editable: false,
    firstName: "Nidhi",
    formattedAddress: "Hsshsh, Dwarka, 110001",
    id: "8816409346071",
    lastName: "Upreti",
    line1: "Hsshsh",
    phone: "8484864644",
    postalCode: "110001",
    shippingAddress: true,
    state: "Delhi",
    town: "Dwarka",
    visibleInAddressBook: true
  },
  deliveryAddressesList: [
    {
      addressType: "Home",
      billingAddress: false,
      city: "Gurgaon",
      country: {
        isocode: "IN",
        name: "India"
      },
      defaultAddress: true,
      editable: false,
      firstName: "Mr. Nidhi",
      formattedAddress: "Gurgaon sector 49, Gurgaon, 110001",
      id: "8817588862999",
      landmark: "cyber park",
      lastName: "upreti",
      line1: "Gurgaon sector 49",
      phone: "8126679706",
      postalCode: "110001",
      shippingAddress: true,
      state: "Haryana",
      town: "Gurgaon",
      visibleInAddressBook: true
    },
    {
      addressType: "Home",
      billingAddress: false,
      city: "jsjsjsjsjsjsj",
      country: {
        isocode: "IN",
        name: "India"
      },
      defaultAddress: false,
      editable: false,
      firstName: "Mr. hahshs",
      formattedAddress: "Bananas, jsjsjsjsjsjsj, 258025",
      id: "8817590927383",
      landmark: "bans",
      lastName: "ahhaha",
      line1: "Bananas",
      phone: "2580258025",
      postalCode: "258025",
      shippingAddress: true,
      state: "bans",
      town: "jsjsjsjsjsjsj",
      visibleInAddressBook: true
    }
  ],
  productRichAttrOfQuickDrop: "yes",
  returnDates: ["24-03-2018", "25-03-2018", "26-03-2018"],
  returnEntry: {
    orderEntries: [
      {
        entryNumber: 4,
        product: {
          availableForPickup: false,
          baseProduct: "MP000000000113800",
          code: "MP000000000113801",
          name: "Infants Boys Clothing Shirts",
          purchasable: true,
          url: "/infants-boys-clothing-shirts/p-mp000000000113801"
        },
        quantity: 1,
        totalPrice: {
          currencyIso: "INR",
          value: 778
        }
      }
    ]
  },
  returnLogisticsAvailability: true,
  returnReasonDetailsList: [
    {
      code: "01",
      reasonDescription: "The product I received was damaged"
    },
    {
      code: "02",
      reasonDescription: "The product delivered is faulty"
    },
    {
      code: "03",
      reasonDescription: "I'm not happy with the product quality"
    },
    {
      code: "04",
      reasonDescription: "I was sent the wrong product"
    },
    {
      code: "05",
      reasonDescription: "The product is missing a part"
    },
    {
      code: "06",
      reasonDescription: "My order took too long to reach me"
    },
    {
      code: "07",
      reasonDescription:
        "The product doesn't look like what I saw on the website"
    },
    {
      code: "08",
      reasonDescription: "What I ordered doesn't fit me well"
    }
  ],
  returnStoreDetailsList: [
    {
      active: "Y",
      address: {
        billingAddress: false,
        city: "New Delhi",
        country: {
          isocode: "IN",
          name: "India"
        },
        defaultAddress: false,
        editable: false,
        formattedAddress: "Mark Road streettt, Easat street, 110001",
        id: "8811100798999",
        line1: "Mark Road streettt",
        line2: "Easat street",
        postalCode: "110001",
        shippingAddress: false,
        visibleInAddressBook: true
      },
      clicknCollect: "Y",
      displayName: "Bamboo",
      geoPoint: {
        latitude: 28.6327,
        longitude: 77.2196
      },
      isReturnable: "Y",
      managerName: "Bamboo",
      mplClosingTime: "21:00",
      mplOpeningTime: "11:00",
      mplWorkingDays: "1,2,3,4,5,6,0",
      name: "273570-273572",
      orderAcceptanceTAT: 0,
      orderCutoffTimeED: "20:00",
      orderCutoffTimeHD: "16:00",
      orderProcessingTAT: 0,
      parkingAvailable: "Y",
      phoneNo0: "04421465464",
      returnAddress1: "Mark Road streettt",
      returnAddress2: "Easat street",
      returnCity: "New Delhi",
      returnPin: "110001",
      returnState: "30",
      returnstoreID: "273570-273572",
      sellerId: "273570",
      slaveId: "273570-273572",
      storeContactNumber: "04425525451"
    }
  ],
  returnTimeSlots: [
    "10:00 AM-01:00 PM",
    "01:00 PM-03:00 PM",
    "03:00 PM-06:00 PM"
  ],
  selfCourierDocumentLink:
    "http://uat2.tataunistore.com:80/my-account/returns/returnFileDownload?orderCode=180314-000-111548&transactionId=273570000120027",
  sellerRichAttrOfQuickDrop: "yes"
};

export default class ReturnDateTime extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedDate: this.props.selectedDate ? this.props.selectedDate : null,
      selectedTime: this.props.selectedTime ? this.props.selectedTime : null
    };
  }
  handleCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  handleDateSelect(val) {
    this.setState({ selectedDate: val }, () => {
      if (this.props.onDateSelect) {
        this.props.onDateSelect(val);
      }
    });
  }
  handleTimeSelect(val) {
    this.setState({ selectedTime: val }, () => {
      if (this.props.onTimeSelect) {
        this.props.onTimeSelect(val);
      }
    });
  }
  render() {
    return (
      <ReturnsFrame
        headerText="Select pickup date"
        onCancel={() => this.handleCancel()}
      >
        <div className={styles.cardOffset}>
          <div className={styles.content}>
            <OrderReturnAddressDetails
              addressType={data.deliveryAddress.addressType}
              address={data.deliveryAddress.formattedAddress}
              subAddress={`${data.deliveryAddress.state} ${
                data.deliveryAddress.city
              } ${data.deliveryAddress.postalCode}`}
            />
          </div>
        </div>
        <div className={styles.cardOffset}>
          <div className={styles.header}>Select return date</div>
          {data.returnDates.map(val => {
            return (
              <SelectReturnDate
                label={val}
                selectItem={() => {
                  this.handleDateSelect(val);
                }}
                selected={val === this.state.selectedDate}
              />
            );
          })}
        </div>

        <div className={styles.card}>
          <div className={styles.header}>Select return time</div>
          {this.state.selectedDate &&
            data.returnTimeSlots.map(val => {
              return (
                <SelectReturnDate
                  label={val}
                  selected={val === this.state.selectedTime}
                  selectItem={() => {
                    this.handleTimeSelect(val);
                  }}
                />
              );
            })}
        </div>
      </ReturnsFrame>
    );
  }
}
ReturnDateTime.propTypes = {
  onCancel: PropTypes.func,
  onDateSelect: PropTypes.func,
  onTimeSelect: PropTypes.func,
  selectedDate: PropTypes.string,
  selectedTime: PropTypes.string
};
