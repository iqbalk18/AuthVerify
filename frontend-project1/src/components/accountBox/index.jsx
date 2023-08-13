import React, { useState } from "react";
import styled from "styled-components";
import { LoginForm } from "./loginForm";
import { motion } from "framer-motion";
import { AccountContext } from "./accountContext";
import { SignupForm } from "./signupForm";

const BoxContainer = styled.div`
  width: 600px; /* Lebar box container diperbesar */
  min-height: 700px; /* Tinggi box container diperbesar */
  display: flex;
  flex-direction: column;
  align-items: center; /* Container di tengah */
  border-radius: 25px; /* Ubah radius border */
  background-color: #fff;
  box-shadow: 0 0 2px rgba(15, 15, 15, 0.28);
  position: relative;
  overflow: hidden;
  margin-top: 20px; /* Margin atas ditambahkan */
`;

const TopContainer = styled.div`
  width: 100%;
  height: 400px; /* Tinggi top container diperbesar */
  display: flex;
  flex-direction: column;
  align-items: center; /* Container di tengah */
  justify-content: flex-end;
  padding: 0 1.8em;
  padding-bottom: 5em;
`;

const BackDrop = styled(motion.div)`
  width: 220%; /* Lebar backdrop diperkecil */
  height: 800px; /* Tinggi backdrop diperbesar */
  position: absolute;
  display: flex;
  flex-direction: column;
  border-radius: 50%;
  transform: rotate(60deg);
  top: -510px; 
  left: -290px; 
  background: rgb(241, 196, 15);
  background: linear-gradient(
    58deg,
    rgba(33, 150, 243, 1) 20%, 
    rgba(41, 98, 255, 1) 100% 
  );
`;

const HeaderContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center; /* Header text di tengah */
  margin-top: 40px; /* Margin atas ditambahkan */
`;

const HeaderText = styled.h2`
  font-size: 40px; /* Ukuran font header text diperbesar */
  font-weight: 600;
  line-height: 1.24;
  color: #fff;
  z-index: 10;
  margin: 0;
`;

const SmallText = styled.h5`
  color: #fff;
  font-weight: 500;
  font-size: 16px; /* Ukuran font small text diperbesar */
  z-index: 10;
  margin: 0;
  margin-top: 12px; /* Margin atas diperbesar */
`;

const InnerContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center; /* Form di tengah */
  padding: 0 3em; /* Padding horizontal diperbesar */
`;

const backdropVariants = {
  expanded: {
    width: "233%",
    height: "1500px", /* Tinggi backdrop diperbesar */
    borderRadius: "20%",
    transform: "rotate(60deg)",
  },
  collapsed: {
    width: "220%", /* Lebar backdrop diperkecil */
    height: "800px", /* Tinggi backdrop diperbesar */
    borderRadius: "50%",
    transform: "rotate(60deg)",
  },
};

const expandingTransition = {
  type: "spring",
  duration: 2.3,
  stiffness: 30,
};

export function AccountBox(props) {
  const [isExpanded, setExpanded] = useState(false);
  const [active, setActive] = useState("signin");

  const playExpandingAnimation = () => {
    setExpanded(true);
    setTimeout(() => {
      setExpanded(false);
    }, expandingTransition.duration * 1000 - 1500);
  };

  const switchToSignup = () => {
    playExpandingAnimation();
    setTimeout(() => {
      setActive("signup");
    }, 400);
  };

  const switchToSignin = () => {
    playExpandingAnimation();
    setTimeout(() => {
      setActive("signin");
    }, 400);
  };

  const contextValue = { switchToSignup, switchToSignin };

  return (
    <AccountContext.Provider value={contextValue}>
      <BoxContainer>
        <TopContainer>
          <BackDrop
            initial={false}
            animate={isExpanded ? "expanded" : "collapsed"}
            variants={backdropVariants}
            transition={expandingTransition}
          />
          {active === "signin" && (
            <HeaderContainer>
              <HeaderText>Welcome</HeaderText>
              <HeaderText>Back</HeaderText>
              <SmallText>Please sign-in to continue!</SmallText>
            </HeaderContainer>
          )}
          {active === "signup" && (
            <HeaderContainer>
              <HeaderText>Create</HeaderText>
              <HeaderText>Account</HeaderText>
              <SmallText>Please sign-up to continue!</SmallText>
            </HeaderContainer>
          )}
        </TopContainer>
        <InnerContainer>
          {active === "signin" && <LoginForm />}
          {active === "signup" && <SignupForm />}
        </InnerContainer>
      </BoxContainer>
    </AccountContext.Provider>
  );
}
