import { memo, useCallback } from "react";
import { Divider, Heading, Table, TableContainer, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";
import { Link } from "react-router-dom";

import { Equipment } from "../../types/Equipment";

type Props = {
  equipments: Array<Equipment>;
};

export const SearchResult = memo((props: Props) => {
  const { equipments } = props;

  // １ヶ月後の日付をyyyy-mm-dd形式に変換する
  const formatOneMonthAhead = useCallback((dt: Date) => {
    var y = dt.getFullYear();
    var m = ('00' + (dt.getMonth() + 2)).slice(-2);
    var d = ('00' + dt.getDate()).slice(-2);
    return (y + '-' + m + '-' + d);
  }, []);

  // propsで渡された設備リストを検索結果として表示する。点検期限が１ヶ月以内の場合は赤く表示する。
  return (
    <>
      <Heading size='lg'>検索結果</Heading>
      <Divider my={3} />
      <TableContainer>
        <Table variant='simple'>
          <Thead>
            <Tr>
              <Th>設備名称</Th>
              <Th>設備番号</Th>
              <Th>設置場所</Th>
              <Th>点検種別</Th>
              <Th>点検期限</Th>
            </Tr>
          </Thead>
          <Tbody>
            {equipments?.map((equipment) => (
              <Tr key={equipment.checkPlanId}>
                <Td color={"blue"}>
                  <Link to={`/update/${equipment.equipmentId}`} state={{ id: equipment.equipmentId }}>{equipment.name}</Link>
                </Td>
                <Td >{equipment.number}</Td>
                <Td>{equipment.location}</Td>
                <Td>{equipment.checkType}</Td>
                <Td style={{ color: formatOneMonthAhead(new Date()) >= equipment.deadline ? "red" : "black" }}>
                  {equipment.deadline}
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
    </>
  );
});