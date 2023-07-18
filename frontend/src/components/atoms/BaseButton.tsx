import { Button } from "@chakra-ui/react";
import { FC, ReactNode } from "react";

type Props = {
  children: ReactNode;
  onClick: () => void;
}

export const BaseButton:FC<Props> = (props) => {
  const { children, onClick } = props;
  return (
    <Button colorScheme="teal" variant={"outline"} onClick={onClick} >
      {children}
    </Button>
      )
}