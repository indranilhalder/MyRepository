import {
  convertDateTimeFromIndianToAmerican,
  timeIsInBetweenRange
} from "./dateTimeUtils.js";

it("should work with dates of format YYYY-MM-DD HH:MM:SS", () => {
  let test = "2018-04-15 11:12:40"; // this is 2018 April 15th
  expect(convertDateTimeFromIndianToAmerican(test)).toBe("04/15/2018 11:12:40");

  test = "2018-04-15";
  expect(convertDateTimeFromIndianToAmerican(test)).toBe("04/15/2018");
});

it("should work with dates of format YYYY/MM/DD HH:MM:SS", () => {
  let test = "2018/04/15 11:12:20";
  expect(convertDateTimeFromIndianToAmerican(test)).toBe("04/15/2018 11:12:20");

  test = "2018/04/15";
  expect(convertDateTimeFromIndianToAmerican(test)).toBe("04/15/2018");
});

it("should work with dates of format DD-MM-YYYY HH:MM:SS", () => {
  let test = "15-04-2018 11:12:20";
  expect(convertDateTimeFromIndianToAmerican(test)).toBe("04/15/2018 11:12:20");
  test = "15-04-2018";
  expect(convertDateTimeFromIndianToAmerican(test)).toBe("04/15/2018");
});

it("should work with dates of format DD/MM/YYYY HH:MM:SS", () => {
  let test = "15/04/2018 11:12:20";
  expect(convertDateTimeFromIndianToAmerican(test)).toBe("04/15/2018 11:12:20");
  test = "15/04/2018";
  expect(convertDateTimeFromIndianToAmerican(test)).toBe("04/15/2018");
});
