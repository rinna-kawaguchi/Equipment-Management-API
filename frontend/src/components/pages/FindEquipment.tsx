import { Box, HStack, Heading } from "@chakra-ui/react";
import axios from "axios";
import { useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BaseButton } from "../atoms/BaseButton";
import { SearchInput } from "../molecules/SearchInput";
import { SearchResult } from "../organisms/SearchResult";
import { Equipment } from "../../types/Equipment";

export const FindEquipment = () => {
  const [name, setName] = useState("");
  const [number, setNumber] = useState("");
  const [location, setLocation] = useState("");
  const [deadline, setDeadline] = useState("");
  const [equipments, setEquipments] = useState<Array<Equipment>>([]);

  const navigate = useNavigate();

  // 設備登録画面に遷移する
  const onClickCreatePage = () => navigate("/create");

  // SearchInputで入力された内容を設備情報の各項目に渡す
  const handleSearchCondition = useCallback((inputName: string, inputNumber: string, inputLocation: string, inputDeadline: string) => {
    setName(inputName);
    setNumber(inputNumber);
    setLocation(inputLocation);
    setDeadline(inputDeadline);
  }, []);

  // Spring BootのAPIを叩いて、前段で入力した条件に合致する設備情報を取得する。
  const onClickFindEquipment = useCallback(() => {
    axios.get<Array<Equipment>>(`http://localhost:8080/equipments?name=${name}&number=${number}&location=${location}&deadline=${deadline}`)
      .then((res) => setEquipments(res.data));
  }, [name, number, location, deadline]);

  // 新規設備登録ボタン、検索条件入力欄、検索結果の表示
  return (
    <Box padding={5}>
      <HStack spacing={10}>
        <Heading>設備検索</Heading>
        <BaseButton onClick={onClickCreatePage}>新規設備登録</BaseButton>
      </HStack>
      <br />
      <br />
      <SearchInput onEquipmentSearch={handleSearchCondition} />
      <br />
      <BaseButton onClick={onClickFindEquipment}>検索</BaseButton>
      <br />
      <br />
      <br />
      <SearchResult equipments={equipments} />
    </Box>
  );
};