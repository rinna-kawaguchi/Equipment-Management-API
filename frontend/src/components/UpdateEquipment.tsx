import { Heading } from "@chakra-ui/react"
import { useNavigate } from "react-router-dom";
import { BaseButton } from "./atoms/BaseButton";

export const UpdateEquipment = () => {
  const navigate = useNavigate();

  const onClickBackDetailPage = () => navigate("/detail")
  const onClickUpdate = () => {
    alert("更新しますか？")
    navigate("/detail")}

  return (
    <div>
      <Heading>設備情報修正</Heading>
      <br />
      <br />
      <br />
      <BaseButton onClick={onClickBackDetailPage}>戻る</BaseButton>
      <BaseButton onClick={onClickUpdate}>更新</BaseButton>
    </div>
  )
}