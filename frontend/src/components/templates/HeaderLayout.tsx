import { ReactNode, memo } from "react";
import { Header } from "../atoms/Header";

type Props = {
  children: ReactNode;
};

export const HeaderLayout = memo((props: Props) => {
  const { children } = props;

  return (
    <>
      <Header />
      {children}
    </>
  );
});