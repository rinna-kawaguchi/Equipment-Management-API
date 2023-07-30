import { Box, FormControl, FormLabel, HStack, Input } from "@chakra-ui/react";
import { memo } from "react";
import { Equipment } from "../../types/Equipment";

type Props = {
  selectedEquipment: Equipment | null;
};

export const EquipmentInformation = memo((props: Props) => {
  const { selectedEquipment } = props;

  return (
    <HStack spacing={10}>
      <Box>
        <FormControl>
          <FormLabel>設備名称</FormLabel>
          <Input value={selectedEquipment?.name} width={"400px"} placeholder="設備名称" />
        </FormControl>
      </Box>
      <Box>
        <FormControl>
          <FormLabel>設備番号</FormLabel>
          <Input value={selectedEquipment?.number} width={"400px"} placeholder="設備番号" />
        </FormControl>
      </Box>
      <Box>
        <FormControl>
          <FormLabel>設置場所</FormLabel>
          <Input value={selectedEquipment?.location} width={"400px"} placeholder="設置場所" />
        </FormControl>
      </Box>
    </HStack>
  );
});