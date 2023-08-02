import { Box, Divider, HStack, Heading, Image } from "@chakra-ui/react";

export const HowToUse = () => {
  return (
    <Box px={10} py={5}>
      <Heading size="lg">このアプリの使い方</Heading>
      <Divider my={3} />
      <Box px={3}>
        <Heading size="md">１．設備検索</Heading>
        <br />
        <HStack spacing={10}>
          <Image src="./images/find1.png" w="700px" />
          <Image src="./images/find2.png" w="700px" />
        </HStack>
        <br />
        <Heading size="md">２．設備情報修正</Heading>
        <br />
        <HStack spacing={10}>
          <Image src="./images/updateEquipment1.png" w="700px" />
          <Image src="./images/updateEquipment2.png" w="700px" />
        </HStack>
        <br />
        <br />
        <Heading size="md">３．点検計画追加（点検履歴追加も同様）</Heading>
        <br />
        <HStack spacing={10}>
          <Image src="./images/createPlan1.png" w="700px" />
          <Image src="./images/createPlan2.png" w="700px" />
        </HStack>
        <br />
        <br />
        <Heading size="md">４．点検計画修正（点検履歴修正も同様）</Heading>
        <br />
        <HStack spacing={10}>
          <Image src="./images/updatePlan1.png" w="700px" />
          <Image src="./images/updatePlan2.png" w="700px" />
        </HStack>
        <br />
        <br />
        <Heading size="md">５．点検計画削除（点検履歴削除も同様）</Heading>
        <br />
        <HStack spacing={10}>
          <Image src="./images/deletePlan1.png" w="700px" />
          <Image src="./images/deletePlan2.png" w="700px" />
        </HStack>
        <br />
        <br />
        <Heading size="md">６．設備情報・点検計画・点検履歴削除</Heading>
        <br />
        <HStack spacing={10}>
          <Image src="./images/deleteEquipment1.png" w="700px" />
          <Image src="./images/deleteEquipment2.png" w="700px" />
        </HStack>
        <br />
        <br />
        <Heading size="md">７．新規設備登録</Heading>
        <br />
        <HStack spacing={10}>
          <Image src="./images/createEquipment1.png" w="700px" />
          <Image src="./images/createEquipment2.png" w="700px" />
        </HStack>
      </Box>
    </Box>
  );
};