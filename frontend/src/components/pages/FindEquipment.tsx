import { Box, Divider, Heading } from "@chakra-ui/react";
import axios from "axios";
import { memo, useCallback, useEffect, useState } from "react";
import { BaseButton } from "../atoms/BaseButton";
import { SearchInput } from "../molecules/SearchInput";
import { SearchResult } from "../organisms/SearchResult";
import { Equipment } from "../../types/Equipment";

export const FindEquipment = memo(() => {
  const [name, setName] = useState("");
  const [number, setNumber] = useState("");
  const [location, setLocation] = useState("");
  const [deadline, setDeadline] = useState("");
  const [equipments, setEquipments] = useState<Array<Equipment>>([]);

  // Spring BootのAPIを叩いて、全ての設備情報を取得する。
  useEffect(() => {
    axios.get<Array<Equipment>>("http://localhost:8080/equipments")
      .then((res) => setEquipments(res.data));
  }, []);

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
    <Box px={10} py={5}>
      <Heading size="lg">設備検索</Heading>
      <Divider my={3} />
      <Box px={3}>
        <Heading size='md'>検索条件入力</Heading>
        <Divider my={3} />
        <SearchInput onEquipmentSearch={handleSearchCondition} />
        <br />
        <BaseButton onClick={onClickFindEquipment}>検索</BaseButton>
        <br />
        <br />
        <br />
        <Heading size='md'>検索結果</Heading>
        <Divider my={3} />
        <SearchResult equipments={equipments} />
      </Box>
    </Box>
  );
});