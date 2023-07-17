import { Heading } from "@chakra-ui/react"
import { BaseButton } from "./atoms/BaseButton"
import { useNavigate } from "react-router-dom";

export const EquipmentDetail = () => {
  const navigate = useNavigate();

  const onClickBackFindPage = () => navigate("/find")
  const onClickUpdatePage = () => navigate("/update")

  const onClickDelete = () => {
    alert("削除しますか？");
  }

  return (
    <div>
      <Heading>設備情報詳細</Heading>
      <br />
      <br />
      <br />
      <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
      <BaseButton onClick={onClickUpdatePage}>修正</BaseButton>
      <BaseButton onClick={onClickDelete}>削除</BaseButton>
    </div>
  )
}