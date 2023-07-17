import { Heading } from "@chakra-ui/react"
import { BaseButton } from "./atoms/BaseButton"
import { useNavigate } from "react-router-dom";

export const CreateEquipment = () => {
  const navigate = useNavigate();

  const onClickBackFindPage = () => navigate("/find")
  const onClickCreateEquipment = () => {
    alert("設備を登録しますか？")
    navigate("/detail")
  }

  return (
    <div>
      <Heading>新規設備登録</Heading>
      <br />
      <br />
      <br />
      <BaseButton onClick={onClickBackFindPage}>戻る</BaseButton>
      <BaseButton onClick={onClickCreateEquipment}>登録</BaseButton>
    </div>
  )
}