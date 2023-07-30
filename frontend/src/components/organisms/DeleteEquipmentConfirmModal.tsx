import { memo } from "react";
import axios from "axios";
import { useNavigate, useParams } from "react-router-dom";

import { ConfirmModal } from "../atoms/ConfirmModal";
import { useMessage } from "../../hooks/useMessage";

type Props = {
  isOpen: boolean;
  onClose: () => void;
};

export const DeleteEquipmentConfirmModal = memo((props: Props) => {
  const { isOpen, onClose } = props;
  const { showMessage } = useMessage();
  const { id } = useParams();

  const navigate = useNavigate();

  // Spring BootのAPIを叩いて指定した設備IDの設備情報、点検計画、点検履歴を削除する。その後設備検索画面に遷移する。
  const deleteEquipmentExec = async () => {
    let res = await axios.delete(`http://localhost:8080/equipments/${id}/plans`)
      .then(() => axios.delete(`http://localhost:8080/equipments/${id}/histories`))
      .then(() => axios.delete(`http://localhost:8080/equipments/${id}`))
      .catch(() => showMessage({
        title: "設備情報、点検計画、点検履歴の削除に失敗しました。", status: "error"
      }));
    if (res) {
      const response: string = res.data.message;
      showMessage({ title: `${response}。設備検索画面に戻ります。`, status: "success" });
    }
    navigate("/find");

  };

  return (
    <ConfirmModal isOpen={isOpen} onClose={onClose} onClickExec={deleteEquipmentExec}>
      この設備情報、点検計画、点検履歴を削除しますか？
    </ConfirmModal>
  );
});