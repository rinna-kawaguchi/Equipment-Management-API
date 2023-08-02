import { memo, useCallback } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

import { ConfirmModal } from "../../atoms/ConfirmModal";
import { useMessage } from "../../../hooks/useMessage";
import { History } from "../../../types/History";

type Props = {
  selectedHistory: History | null;
  isOpen: boolean;
  onClose: () => void;
  onHistoryDelete: (deletedHistories: Array<History>) => void;
};

export const DeleteHistoryConfirmModal = memo((props: Props) => {
  const { selectedHistory, isOpen, onClose, onHistoryDelete } = props;
  const { showMessage } = useMessage();
  const { id } = useParams();

  // Spring BootのAPIを叩いて指定したIDの点検履歴を削除する
  const deleteHistoryExec = useCallback(async () => {
    let res = await axios.delete(`http://localhost:8080/histories/${selectedHistory?.checkHistoryId}`)
      .catch(() => showMessage({ title: "点検履歴の削除に失敗しました。", status: "error" }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: response, status: "success" });
    }
    axios.get<Array<History>>(`http://localhost:8080/equipments/${id}/histories`)
      .then((res) => onHistoryDelete(res.data));
    onClose();
  }, [selectedHistory]);

  return (
    <ConfirmModal isOpen={isOpen} onClose={onClose} onClickExec={deleteHistoryExec}>
      点検履歴を削除しますか？
    </ConfirmModal>
  );
});