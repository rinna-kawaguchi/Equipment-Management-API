import { HStack, Heading } from "@chakra-ui/react";
import { Link } from "react-router-dom";

export const Header = () => {

  return (
    <HStack background="teal.50" px={7} py={4} spacing={5}>
      <Heading size="lg">設備点検管理アプリ</Heading>
      <Link to='/search'>設備検索</Link>
      <Link to='/create'>新規設備登録</Link>
      <Link to='/how_to_use'>使い方</Link>
    </HStack>
  )
};