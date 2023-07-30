import { HStack, Modal, ModalBody, ModalCloseButton, ModalContent, ModalOverlay, Text, VStack } from "@chakra-ui/react";
import { BaseButton } from "./BaseButton";
import { ReactNode } from "react";

type Props = {
  isOpen: boolean;
  onClose: () => void;
  children: ReactNode;
  onClickExec: () => void;
};

export const ConfirmModal = (props: Props) => {
  const { isOpen, onClose, children, onClickExec } = props;

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="xs">
      <ModalOverlay />
      <ModalContent py={2}>
        <ModalCloseButton />
        <ModalBody mx={4}>
          <VStack spacing={4}>
            <Text>{children}</Text>
            <HStack>
              <BaseButton onClick={onClickExec}>はい</BaseButton>
              <BaseButton onClick={onClose}>いいえ</BaseButton>
            </HStack>
          </VStack>
        </ModalBody>
      </ModalContent>
    </Modal>
  );
};