import React from "react";
import LogInButton from "./LogInButton.js";
import { mount } from "enzyme";

test("Button is rendered with window with less than 1024", () => {
  window.innerWidth = 500;
  const LoginButton = mount(<LogInButton />);
  console.log("LOGIN BUTTON TEST");
  console.log(LoginButton.text());
  expect(LoginButton.children.length).toEqual(1);
  expect(LoginButton.contains("Login"));
});

test("Button is rendered with window with greater than 1024", () => {
  window.innerWidth = 1028;
  const LoginButton = mount(<LogInButton />);
  expect(LoginButton.children.length).toEqual(1);
  expect(LoginButton.contains("Sign in"));
});
