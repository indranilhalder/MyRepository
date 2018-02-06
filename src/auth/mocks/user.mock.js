export const userDetails = {
  type: "mplUserLoginResultWsDto",
  access_token: "d2470a48-e71e-41b7-b6b2-a083af3d8c08",
  token_type: "bearer",
  expires_in: 43199,
  scope: "basic",
  status: "Success",
  customerId: "1000119003",
  isTemporaryPassword: "Y",
  isSocialMedia: true,
  customerInfo: {
    type: "getCustomerDetailDto",
    status: "Success",
    dateOfBirth: "22/11/1992",
    emailID: "surajk@dewsolutions.in",
    firstName: "Suraj",
    gender: "Male",
    lastName: "Kumar",
    mobileNumber: "7503721061",
    loginId: "d2470a48-e71e-41b7-b6b2-a083af3d8c08"
  }
};

export const userDetailsFailure = {
  status: "Failure",
  errorCode: "B00001",
  message: "Cannot read property 'username' of undefined"
};

export const userDetailsLoginFailure = {
  status: "Failure",
  errorCode: "B00001",
  message: "Cannot read property 'loginId' of undefined"
};
