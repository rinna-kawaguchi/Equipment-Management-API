import { Button } from "@chakra-ui/react";
import { FC, ReactNode, memo } from "react";

type Props = {
  children: ReactNode;
  onClick: () => void;
};

export const BaseButton: FC<Props> = memo((props) => {
  const { children, onClick } = props;
  return (
    <Button colorScheme="teal" variant={"outline"} onClick={onClick} >
      {children}
    </Button>
  );
});