const DATE_TIME_REGEX_FORMAT_1 = /^(\d{2})\/(\d{2})\/(\d{4}) (\d{2}):(\d{2}):(\d{2})/;
const DATE_TIME_REGEX_FORMAT_2 = /^(\d{4})\/(\d{2})\/(\d{2}) (\d{2}):(\d{2}):(\d{2})/;
const DATE_TIME_REGEX_FORMAT_3 = /^(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2})/;
const DATE_TIME_REGEX_FORMAT_4 = /^(\d{2})-(\d{2})-(\d{4}) (\d{2}):(\d{2}):(\d{2})/;

const DATE_FORMAT_1 = /^(\d{2})\/(\d{2})\/(\d{4})/;
const DATE_FORMAT_2 = /^(\d{4})\/(\d{2})\/(\d{2})/;
const DATE_FORMAT_3 = /^(\d{4})-(\d{2})-(\d{2})/;
const DATE_FORMAT_4 = /^(\d{2})-(\d{2})-(\d{4})/;

export function convertDateTimeFromIndianToAmerican(str) {
  let match;

  if (
    DATE_TIME_REGEX_FORMAT_1.test(str) ||
    DATE_TIME_REGEX_FORMAT_4.test(str)
  ) {
    match = DATE_TIME_REGEX_FORMAT_1.exec(str);
    if (!match) {
      match = DATE_TIME_REGEX_FORMAT_4.exec(str);
    }

    const day = match[1];
    const month = match[2];
    const year = match[3];

    const hours = match[4];
    const minutes = match[5];
    const seconds = match[6];
    const dateTimeInAmericanFormat = `${month}/${day}/${year} ${hours}:${minutes}:${seconds}`;
    return dateTimeInAmericanFormat;
  }

  if (
    DATE_TIME_REGEX_FORMAT_2.test(str) ||
    DATE_TIME_REGEX_FORMAT_3.test(str)
  ) {
    match = DATE_TIME_REGEX_FORMAT_2.exec(str);
    if (!match) {
      match = DATE_TIME_REGEX_FORMAT_3.exec(str);
    }

    const year = match[1];
    const month = match[2];
    const day = match[3];
    const hours = match[4];
    const minutes = match[5];
    const seconds = match[6];
    const dateTimeInAmericanFormat = `${month}/${day}/${year} ${hours}:${minutes}:${seconds}`;
    return dateTimeInAmericanFormat;
  }

  if (DATE_FORMAT_1.test(str) || DATE_FORMAT_4.test(str)) {
    match = DATE_FORMAT_1.exec(str);
    if (!match) {
      match = DATE_FORMAT_4.exec(str);
    }
    const day = match[1];
    const month = match[2];
    const year = match[3];
    const dateTimeInAmericanFormat = `${month}/${day}/${year}`;
    return dateTimeInAmericanFormat;
  }

  if (DATE_FORMAT_2.test(str) || DATE_FORMAT_3.test(str)) {
    match = DATE_FORMAT_2.exec(str);
    if (!match) {
      match = DATE_FORMAT_3.exec(str);
    }

    const year = match[1];
    const month = match[2];
    const day = match[3];
    const dateTimeInAmericanFormat = `${month}/${day}/${year}`;
    return dateTimeInAmericanFormat;
  }

  return null;
}
