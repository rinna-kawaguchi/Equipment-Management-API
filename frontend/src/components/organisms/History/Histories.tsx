import { Box, HStack, Table, TableContainer, Tbody, Td, Th, Thead, Tr, useBoolean } from "@chakra-ui/react";
import { memo, useCallback } from "react";
import { BaseButton } from "../../atoms/BaseButton";
import { History } from "../../../types/History";
import { useSelectHistory } from "../../../hooks/useSelectHistory";
import { UpdateHistoryModal } from "./UpdateHistoryModal";
import { DeleteHistoryConfirmModal } from "./DeleteHistoryConfirmModal";

type Props = {
  histories: Array<History>;
  onHistoryUpdate: (updatedHisotries: Array<History>) => void;
  onHistoryDelete: (deletedHistories: Array<History>) => void;
};

export const Histories = memo((props: Props) => {
  const { histories, onHistoryUpdate, onHistoryDelete } = props;
  const { onSelectHistory, selectedHistory } = useSelectHistory();

  const [updateHistoryFlag, setUpdateHistoryFlag] = useBoolean();
  const [deleteHistoryFlag, setDeleteHistoryFlag] = useBoolean();

  // useSelectHistoryのカスタムフック内のonSelectHistory関数で点検履歴を特定しUpdateHistoryModalを表示する
  const onClickUpdateHistoryModal = useCallback((checkHistoryId: number) => {
    onSelectHistory({ checkHistoryId: checkHistoryId, histories: histories });
    setUpdateHistoryFlag.on();
  }, [histories, onSelectHistory]);

  // useSelectHistoryのカスタムフック内のonSelectHistory関数で点検履歴を特定しDeleteHistoryConfirmModalを表示する
  const onClickDeleteHistory = useCallback((checkHistoryId: number) => {
    onSelectHistory({ checkHistoryId: checkHistoryId, histories: histories });
    setDeleteHistoryFlag.on();
  }, [histories, onSelectHistory]);

  return (
    <Box>
      <TableContainer width={900}>
        <Table variant='simple'>
          <Thead>
            <Tr>
              <Th width={250}>実施日</Th>
              <Th width={250}>点検種別</Th>
              <Th width={200}>点検結果</Th>
              <Th></Th>
            </Tr>
          </Thead>
          <Tbody>
            {histories.map((history) => (
              <Tr key={history.checkHistoryId}>
                <Td>{history.implementationDate}</Td>
                <Td >{history.checkType}</Td>
                <Td>{history.result}</Td>
                <Td>
                  <HStack>
                    <BaseButton onClick={() => onClickUpdateHistoryModal(history.checkHistoryId)}>
                      修正
                    </BaseButton>
                    <BaseButton onClick={() => onClickDeleteHistory(history.checkHistoryId)}>
                      削除
                    </BaseButton>
                  </HStack>
                </Td>
              </Tr>
            ))}
          </Tbody>
        </Table>
      </TableContainer>
      <UpdateHistoryModal selectedHistory={selectedHistory} isOpen={updateHistoryFlag}
        onClose={setUpdateHistoryFlag.off} onHistoryUpdate={onHistoryUpdate} />
      <DeleteHistoryConfirmModal selectedHistory={selectedHistory} isOpen={deleteHistoryFlag} onClose={setDeleteHistoryFlag.off} onHistoryDelete={onHistoryDelete} />
    </Box>
  );
});