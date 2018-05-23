export function convertNumber(value) {
  let formattedNumber;
  formattedNumber = value && value > 0 ? Math.round(value * 100) / 100 : "0.00";
  return formattedNumber;
}
